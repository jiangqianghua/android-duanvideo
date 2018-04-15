package com.jqh.duanvideo.model;

/**
 * Created by jiangqianghua on 18/4/14.
 */

public class UserModule {

    private String id;
    private String name ;
    private String age ;
    private int gender ;
    private String address ;
    private int praiseNum ; // 被点赞数量
    private int concernNum; //  关注的人数
    private int fansNum;    // 粉丝睡昂
    private int worksNum ;  // 作品数量
    private int likeNum ;   // 喜欢的数量
    private String avater ;// 头像

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getConcernNum() {
        return concernNum;
    }

    public void setConcernNum(int concernNum) {
        this.concernNum = concernNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getWorksNum() {
        return worksNum;
    }

    public void setWorksNum(int worksNum) {
        this.worksNum = worksNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }
}
