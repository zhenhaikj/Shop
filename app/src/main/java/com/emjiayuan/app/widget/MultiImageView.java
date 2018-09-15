package com.emjiayuan.app.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.emjiayuan.app.R;
import com.emjiayuan.app.Utils.DensityUtil;
import com.emjiayuan.app.entity.PhotoInfo;

import java.util.List;


/**
 * @ClassName MultiImageView.java
 * @author shoyu
 * @version 
 * @Description: 显示1~N张图片的View
 */

public class MultiImageView extends LinearLayout {
	public static int MAX_WIDTH = 0;

	// 照片的Url列表
	private List<PhotoInfo> imagesList;

	/** 长度 单位为Pixel **/
	private int pxOneMaxWandH;  // 单张图最大允许宽高
	private int pxMoreWandH = 0;// 多张图的宽高
	private int pxImagePadding = DensityUtil.dp2px(getContext(), 3);// 图片间的间距

	private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

	private LayoutParams onePicPara;
	private LayoutParams morePara, moreParaColumnFirst;
	private LayoutParams rowPara;
	private ColorFilterImageView imageView;

	private OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener onItemClickListener){
		mOnItemClickListener = onItemClickListener;
	}

	public MultiImageView(Context context) {
		super(context);
	}

	public MultiImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setList(List<PhotoInfo> lists) throws IllegalArgumentException {
		if(lists==null){
			throw new IllegalArgumentException("imageList is null...");
		}
		imagesList = lists;
		
		if(MAX_WIDTH > 0){
			pxMoreWandH = (MAX_WIDTH - pxImagePadding*2 )/3; //解决右侧图片和内容对不齐问题
			pxOneMaxWandH = MAX_WIDTH * 2 / 3;
			initImageLayoutParams();
		}

		initView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(MAX_WIDTH == 0){
			int width = measureWidth(widthMeasureSpec);
			if(width>0){
				MAX_WIDTH = width;
				if(imagesList!=null && imagesList.size()>0){
					setList(imagesList);
				}
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			// result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
			// + getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private void initImageLayoutParams() {
		int wrap = LayoutParams.WRAP_CONTENT;
		int match = LayoutParams.MATCH_PARENT;

		onePicPara = new LayoutParams(wrap, wrap);

		moreParaColumnFirst = new LayoutParams(pxMoreWandH, pxMoreWandH);
		morePara = new LayoutParams(pxMoreWandH, pxMoreWandH);
		morePara.setMargins(pxImagePadding, 0, 0, 0);

		rowPara = new LayoutParams(match, wrap);
	}

	// 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
	private void initView() {
		this.setOrientation(VERTICAL);
		this.removeAllViews();
		if(MAX_WIDTH == 0){
			//为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
			addView(new View(getContext()));
			return;
		}
		
		if (imagesList == null || imagesList.size() == 0) {
			return;
		}

		if (imagesList.size() == 1) {
			addView(createImageView(0, false));
		} else {
			int allCount = imagesList.size();
			if(allCount == 4){
				MAX_PER_ROW_COUNT = 2;
			}else{
				MAX_PER_ROW_COUNT = 3;
			}
			int rowCount = allCount / MAX_PER_ROW_COUNT
					+ (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
			for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
				LinearLayout rowLayout = new LinearLayout(getContext());
				rowLayout.setOrientation(LinearLayout.HORIZONTAL);

				rowLayout.setLayoutParams(rowPara);
				if (rowCursor != 0) {
					rowLayout.setPadding(0, pxImagePadding, 0, 0);
				}

				int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
						: allCount % MAX_PER_ROW_COUNT;//每行的列数
				if (rowCursor != rowCount - 1) {
					columnCount = MAX_PER_ROW_COUNT;
				}
				addView(rowLayout);

				int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
				for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
					int position = columnCursor + rowOffset;
					rowLayout.addView(createImageView(position, true));
				}
			}
		}
	}

	private View createImageView(final int position, final boolean isMultiImage) {
		final PhotoInfo photoInfo = imagesList.get(position);
//		View view=LayoutInflater.from(getContext()).inflate(R.layout.sq_icon_item,null);
		imageView = new ColorFilterImageView(getContext());
//		imageView = view.findViewById(R.id.cfimg);
//		ImageView btnVideo = view.findViewById(R.id.btnVideo);
		if (photoInfo!=null) {
//		final ImageView imageView = new ColorFilterImageView(getContext());
//		imageView.setBackgroundColor(getResources().getColor(R.color.im_font_color_text_hint));
//		if (!photoInfo.equals(imageView.getTag(R.id.tag_group))){

//			Glide.with(getContext()).load(photoInfo).into(new SimpleTarget<Drawable>() {
//				@Override
//				public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
			if (isMultiImage) {
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreParaColumnFirst : morePara);
			} else {
				imageView.setAdjustViewBounds(true);
				imageView.setScaleType(ImageView.ScaleType.FIT_START);
				//imageView.setMaxHeight(pxOneMaxWandH);

//				int expectW = photoInfo.w;
//				int expectH = photoInfo.h;
//						int expectW = resource.getIntrinsicWidth();
//						int expectH = resource.getIntrinsicHeight();
						int expectW = 200;
						int expectH = 200;

				if (expectW == 0 || expectH == 0) {
					imageView.setLayoutParams(onePicPara);
				} else {
					int actualW = 0;
					int actualH = 0;
					float scale = ((float) expectH) / ((float) expectW);
					if (expectW > pxOneMaxWandH) {
						actualW = pxOneMaxWandH;
						actualH = (int) (actualW * scale);
					} else if (expectW < pxMoreWandH) {
						actualW = pxMoreWandH;
						actualH = (int) (actualW * scale);
					} else {
						actualW = expectW;
						actualH = expectH;
					}
					imageView.setLayoutParams(new LayoutParams(actualW, actualH));
				}
			}

//					imageView.setId(photoInfo.hashCode());
			imageView.setOnClickListener(new ImageOnClickListener(position));
//					imageView.setImageDrawable(resource);
//					imageView.setTag(R.id.tag_group,photoInfo);
//				}
//			});
//			if (!"".equals(photoInfo.getVideoUrl())){
////				btnVideo.setVisibility(VISIBLE);
//				Glide.with(getContext()).load(photoInfo.getVideoUrl()+"?imageView2/1/w/160/h/160/q/85|imageslim").into(imageView);
//			}else{
//				btnVideo.setVisibility(GONE);
				Glide.with(getContext()).load(photoInfo.url+"?imageView2/1/w/240/h/240/q/75|imageslim").into(imageView);
//			}
//		}
		}
		return imageView;
	}

	private class ImageOnClickListener implements View.OnClickListener{

		private int position;
		public ImageOnClickListener(int position){
			this.position = position;
		}

		@Override
		public void onClick(View view) {
			if(mOnItemClickListener != null){
				mOnItemClickListener.onItemClick(view, position);
			}
		}
	}

	public interface OnItemClickListener{
		public void onItemClick(View view, int position);
	}
}