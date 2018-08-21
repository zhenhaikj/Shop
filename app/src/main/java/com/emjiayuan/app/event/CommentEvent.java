package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.Post;

public class CommentEvent {
    private Post post;
    private int  position;

    public CommentEvent(Post post, int position) {
        this.post = post;
        this.position = position;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
