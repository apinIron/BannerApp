package com.github.iron.bannerapp;

/**
 * @author iron
 *         created at 2017/12/13
 */
public class BannerModel {

    private String imageUrl;

    private String tips;

    public BannerModel(String tips,String imageUrl) {
        this.imageUrl = imageUrl;
        this.tips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
