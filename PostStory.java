package com.lms.admin.lms;

/**
 * Created by Admin on 01-03-2018.
 */

public class PostStory {
    private String time, image, textMsg, username, profileImg, date;

    public PostStory(String date, String time, String textMsg, String image, String username) {
        this.time = time;
        this.image = image;
        this.textMsg = textMsg;
        this.username = username;
        this.profileImg = image;
        this.date = date;
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
}
