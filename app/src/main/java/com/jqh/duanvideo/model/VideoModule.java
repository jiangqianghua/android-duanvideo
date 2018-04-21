package com.jqh.duanvideo.model;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public class VideoModule {

    private int userId ;

    private int worksId;

    private String avater ;

    private String mMediaUlr;

    private int comentNum ;

    private int sendNum ;

    private int likeNum ;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getmMediaUlr() {
        return mMediaUlr;
    }

    public void setmMediaUlr(String mMediaUlr) {
        this.mMediaUlr = mMediaUlr;
    }

    public int getComentNum() {
        return comentNum;
    }

    public void setComentNum(int comentNum) {
        this.comentNum = comentNum;
    }

    public int getSendNum() {
        return sendNum;
    }

    public void setSendNum(int sendNum) {
        this.sendNum = sendNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getWorksId() {
        return worksId;
    }

    public void setWorksId(int worksId) {
        this.worksId = worksId;
    }
}
