package com.lms.admin.lms;

/**
 * Created by Admin on 13-03-2018.
 */

public class SpinnerEmpDetails {

    private String name, profileUrl, empId;

    SpinnerEmpDetails(String name, String profileUrl, String empId) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}

