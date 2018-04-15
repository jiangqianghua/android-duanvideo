package com.jqh.duanvideo.model;

/**
 * Created by jiangqianghua on 18/4/15.
 */

public class CommentModule {

    private int id ;

    private String avater ;

    private String userName ;

    private String commentContent ;

    private String date ;

    private int likeNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
