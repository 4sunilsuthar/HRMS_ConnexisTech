package com.lms.admin.lms;

/**
 * Created by Admin on 01-03-2018.
 */

public class PostStory {
    private String time, image, titleMsg, textMsg, username, profileImg, date;

    public PostStory(String date, String titleMsg, String textMsg, String image, String username, String profileImg) {
//        this.time = time;
        this.image = image;
        this.titleMsg = titleMsg;
        this.textMsg = textMsg;
        this.username = username;
        this.profileImg = profileImg;
        this.date = date;
    }

    public String getTitleMsg() {
        return titleMsg;
    }

    public void setTitleMsg(String titleMsg) {
        this.titleMsg = titleMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PostStory{" +
                "time='" + time + '\'' +
                ", image='" + image + '\'' +
                ", textMsg='" + textMsg + '\'' +
                ", username='" + username + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
