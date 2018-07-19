package com.emjiayuan.app.Utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 数字和文字处理
 * Created by zst on 2017/6/28.
 */

public class DigitUtil {

    /**
     * 手机号中间四位隐藏
     * @param phone
     */
    public static String phoneHide(String phone) {
        String phoneHide = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return phoneHide;
    }

    /**
     * 身份证中间8位隐藏
     * 隐藏出生年月
     * @param idCard
     */
    public static String idCardHide(String idCard) {
        String idCardHide = idCard.replaceAll("(\\d{6})\\d{8}(\\w{4})","$1*****$2");
        return idCardHide;
    }

    /**
     * 汉子转拼音
     * @param src
     * @return
     */
    public static String getPinYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        // System.out.println(t1.length);
        String[] t2 = new String[t1.length];
        // System.out.println(t2.length);
        // 设置汉字拼音输出的格式
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i =0; i < t0; i++) {
                // 判断能否为汉字字符
                // System.out.println(t1[i]);
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
//                    t2 = PinyinHelper.toHanyuPinyinStringArray("天".t, t3);// 将汉字的几种全拼都存到t2数组中
                        t4 += t2[0]+" ";// 取出该汉字全拼的第一种读音并连接到字符串t4后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串t4后
                    t4 += Character.toString(t1[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return t4;
    }

    /**
     * 返回拼音首字母大写
     * @param str
     * @return
     */
    public static String getPinYinFirst(String str) {
        String pinyin = getPinYin(str);
        return pinyin.substring(0,1).toUpperCase();
    }
}
