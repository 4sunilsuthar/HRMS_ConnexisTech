package com.lms.admin.lms;

import android.util.Log;

/**
 * Created by Admin on 20-03-2018.
 */

public class PendingLeaveRequest {
    private String request_id, emp_name, leaveType, fromDate, toDate, totalDays, subject, description, profileImg, requestDate;


    PendingLeaveRequest(String request_id, String emp_name, String leaveType, String fromDate, String toDate, String totalDays, String subject, String description, String profileImg, String requestDate) {
        this.request_id = request_id;
        this.emp_name = emp_name;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalDays = totalDays;
        this.subject = subject;
        this.description = description;
        this.profileImg = profileImg;
        this.requestDate = requestDate;
    }

    public String getRequest_id() {
        Log.e("PLR", "returned request id is :" + request_id);
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
