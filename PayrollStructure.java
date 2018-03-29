package com.lms.admin.lms;

/**
 * Created by Admin on 29-03-2018.
 */

public class PayrollStructure {
    private String title, vlaue;

    public PayrollStructure(String title, String vlaue) {
        this.title = title;
        this.vlaue = vlaue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVlaue() {
        return vlaue;
    }

    public void setVlaue(String vlaue) {
        this.vlaue = vlaue;
    }
}
