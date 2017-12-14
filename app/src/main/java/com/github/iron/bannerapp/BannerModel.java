package com.github.iron.bannerapp;

/**
 * @author iron
 *         created at 2017/12/13
 */
public class BannerModel {

    private String imageUrl;

    private String mTips;

    public BannerModel(String tips,String imageUrl) {
        this.imageUrl = imageUrl;
        this.mTips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getmTips() {
        return mTips;
    }

    public void setmTips(String mTips) {
        this.mTips = mTips;
    }
}
