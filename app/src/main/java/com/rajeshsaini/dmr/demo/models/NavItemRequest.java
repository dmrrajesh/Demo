package com.rajeshsaini.dmr.demo.models;

/**
 * Created by rajesh on 3/9/2016.
 */
public class NavItemRequest {
    private String navTitle;
    private String id;
    private String sn;
    private int navIcon;
    private String mobile;
    private String image;
    public NavItemRequest() {

    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public NavItemRequest(String navTitle, int navIcon) {
        this.navTitle = navTitle;
        this.navIcon = navIcon;
    }
    public String getNavTitle() {
        return navTitle;
    }
    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }
    public int getNavIcon() {
        return navIcon;
    }
    public void setNavIcon(int navIcon) {
        this.navIcon = navIcon;
    }
}
