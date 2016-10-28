package com.rajeshsaini.dmr.demo.models;

/**
 * Created by DMRSAINI on 3/15/2016.
 */
public class CallDetails {
    private String mobile;
    private String date;
    private String time;
    private String type;
    private String duration;
    private String oldUpdate;

    public String getOldUpdate() {
        return oldUpdate;
    }

    public void setOldUpdate(String oldUpdate) {
        this.oldUpdate = oldUpdate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
