package com.emjiayuan.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 帖子
 * Created by cyl on 2018年5月10日 09:46:26.
 */
public class Post implements Serializable{


    /**
     * id : 100
     * userid : 1
     * category : 0
     * content : 需要一个单身面匠，地址：辽宁省，大石桥市，一天半袋或者多半袋，工资：4000元，愿意者与本人联系，15649823670
     * pic : http://121.41.13.116/ymjy/admin/upload/20170618/1497755056.jpeg
     * pic2 : null
     * views : null
     * video : null
     * type : 1
     * zan : 1
     * pinglun : 1
     * res_type : 0
     * status : 1
     * posttime : 1497755062
     * visit_num : 0
     * username : 110
     * nickname : 伊穆家园
     * headimg : http://qiniu.emjiayuan.com/upload_api/ems/2018080615874969636
     * weibotype : 热门发布
     * pasttime : 2017年06月18日 11点04分
     * date_day : 06.18
     * date_year : 2017
     * showtype : 1
     * images : ["http://121.41.13.116/ymjy/admin/upload/20170618/1497755056.jpeg"]
     * isoneimage : true
     * zanlist : [{"name":"穆萨","username":"18867857836","nickname":"穆萨","headimg":"http://qiniu.emjiayuan.com/upload_api/ems/2018080619196831808"}]
     * replylist : [{"id":"66","userid":"3338","wid":"100","replyrid":"0","replyname":"","replytype":null,"content":"你是哪里人？","createtime":"1497783057","status":"1","isread":"0","username":"17697149101","nickname":"176****9101","headimg":"http://121.41.13.116/ymjy/admin/upload/20170618/1497782613.jpeg","replyusername":null,"replynickname":null,"replyheadimg":null,"pasttime":"2017年06月18日 18点50分"}]
     * iszan : 0
     * isreplymore : 1
     */

    private String id;
    private String userid;
    private String category;
    private String content;
    private String pic;
    private String pic2;
    private String views;
    private String video;
    private String type;
    private String zan;
    private String pinglun;
    private String res_type;
    private String status;
    private String posttime;
    private String visit_pv;
    private String visit_uv;
    private String username;
    private String nickname;
    private String headimg;
    private String weibotype;
    private String pasttime;
    private String date_day;
    private String date_year;
    private String address;
    private String audio;
    private int showtype;
    private boolean isoneimage;
    private int iszan;
    private int isreplymore;
    private int position;
    private List<String> images;
    private List<ZanlistBean> zanlist;
    private List<ReplylistBean> replylist;
    private boolean expand=false;
    private boolean isPlaying=false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public String getVisit_pv() {
        return visit_pv;
    }

    public void setVisit_pv(String visit_pv) {
        this.visit_pv = visit_pv;
    }

    public String getVisit_uv() {
        return visit_uv;
    }

    public void setVisit_uv(String visit_uv) {
        this.visit_uv = visit_uv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getPinglun() {
        return pinglun;
    }

    public void setPinglun(String pinglun) {
        this.pinglun = pinglun;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String res_type) {
        this.res_type = res_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getWeibotype() {
        return weibotype;
    }

    public void setWeibotype(String weibotype) {
        this.weibotype = weibotype;
    }

    public String getPasttime() {
        return pasttime;
    }

    public void setPasttime(String pasttime) {
        this.pasttime = pasttime;
    }

    public String getDate_day() {
        return date_day;
    }

    public void setDate_day(String date_day) {
        this.date_day = date_day;
    }

    public String getDate_year() {
        return date_year;
    }

    public void setDate_year(String date_year) {
        this.date_year = date_year;
    }

    public int getShowtype() {
        return showtype;
    }

    public void setShowtype(int showtype) {
        this.showtype = showtype;
    }

    public boolean isIsoneimage() {
        return isoneimage;
    }

    public void setIsoneimage(boolean isoneimage) {
        this.isoneimage = isoneimage;
    }

    public int getIszan() {
        return iszan;
    }

    public void setIszan(int iszan) {
        this.iszan = iszan;
    }

    public int getIsreplymore() {
        return isreplymore;
    }

    public void setIsreplymore(int isreplymore) {
        this.isreplymore = isreplymore;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ZanlistBean> getZanlist() {
        return zanlist;
    }

    public void setZanlist(List<ZanlistBean> zanlist) {
        this.zanlist = zanlist;
    }

    public List<ReplylistBean> getReplylist() {
        return replylist;
    }

    public void setReplylist(List<ReplylistBean> replylist) {
        this.replylist = replylist;
    }

    public static class ZanlistBean implements Serializable{
        /**
         * name : 穆萨
         * username : 18867857836
         * nickname : 穆萨
         * headimg : http://qiniu.emjiayuan.com/upload_api/ems/2018080619196831808
         */

        private String name;
        private String username;
        private String nickname;
        private String headimg;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }
    }

    public static class ReplylistBean implements Serializable{
        /**
         * id : 66
         * userid : 3338
         * wid : 100
         * replyrid : 0
         * replyname : 
         * replytype : null
         * content : 你是哪里人？
         * createtime : 1497783057
         * status : 1
         * isread : 0
         * username : 17697149101
         * nickname : 176****9101
         * headimg : http://121.41.13.116/ymjy/admin/upload/20170618/1497782613.jpeg
         * replyusername : null
         * replynickname : null
         * replyheadimg : null
         * pasttime : 2017年06月18日 18点50分
         */

        private String id;
        private String userid;
        private String wid;
        private String replyrid;
        private String replyname;
        private String replytype;
        private String content;
        private String createtime;
        private String status;
        private String isread;
        private String username;
        private String nickname;
        private String headimg;
        private String replyusername;
        private String replynickname;
        private String replyheadimg;
        private String pasttime;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getWid() {
            return wid;
        }

        public void setWid(String wid) {
            this.wid = wid;
        }

        public String getReplyrid() {
            return replyrid;
        }

        public void setReplyrid(String replyrid) {
            this.replyrid = replyrid;
        }

        public String getReplyname() {
            return replyname;
        }

        public void setReplyname(String replyname) {
            this.replyname = replyname;
        }

        public String getReplytype() {
            return replytype;
        }

        public void setReplytype(String replytype) {
            this.replytype = replytype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsread() {
            return isread;
        }

        public void setIsread(String isread) {
            this.isread = isread;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public String getReplyusername() {
            return replyusername;
        }

        public void setReplyusername(String replyusername) {
            this.replyusername = replyusername;
        }

        public String getReplynickname() {
            return replynickname;
        }

        public void setReplynickname(String replynickname) {
            this.replynickname = replynickname;
        }

        public String getReplyheadimg() {
            return replyheadimg;
        }

        public void setReplyheadimg(String replyheadimg) {
            this.replyheadimg = replyheadimg;
        }

        public String getPasttime() {
            return pasttime;
        }

        public void setPasttime(String pasttime) {
            this.pasttime = pasttime;
        }
    }

    public boolean hasFavort(){
        if(zanlist!=null && zanlist.size()>0){
            return true;
        }
        return false;
    }

    public boolean hasComment(){
        if(replylist!=null && replylist.size()>0){
            return true;
        }
        return false;
    }
}
