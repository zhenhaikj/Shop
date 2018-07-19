package com.emjiayuan.app.entity;

import java.util.ArrayList;

/**
 * 帖子
 * Created by cyl on 2018年5月10日 09:46:26.
 */
public class Post {
    private String name;
    private String label;
    private String content;
    private String time;
    private int zan_count;
    private int pl_count;
    private int icon;
    private ArrayList list;

    public Post(String name, String label, String content, String time, int zan_count, int pl_count, int icon, ArrayList list) {
        this.name = name;
        this.label = label;
        this.content = content;
        this.time = time;
        this.zan_count = zan_count;
        this.pl_count = pl_count;
        this.icon = icon;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getZan_count() {
        return zan_count;
    }

    public void setZan_count(int zan_count) {
        this.zan_count = zan_count;
    }

    public int getPl_count() {
        return pl_count;
    }

    public void setPl_count(int pl_count) {
        this.pl_count = pl_count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }
}
