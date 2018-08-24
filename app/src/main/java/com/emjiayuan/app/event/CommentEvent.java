package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.Post;

public class CommentEvent {
    private Post post;
    private int  position;
    private int  commentposition;

    public CommentEvent(Post post,int position ,int commentposition) {
        this.post = post;
        this.position = position;
        this.commentposition = commentposition;
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

    public int getCommentposition() {
        return commentposition;
    }

    public void setCommentposition(int commentposition) {
        this.commentposition = commentposition;
    }
}
