package com.example.avi.firebaseauth;

/**
 * Created by AVI on 3/8/2018.
 */

public class Cards {
    private String imgurl;
    private String title;
    private String day_night;
    private String price;

    public String getDay_night() {
        return day_night;
    }

    public void setDay_night(String day_night) {
        this.day_night = day_night;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Cards(String imgurl, String title,String day_night,String price) {
        this.imgurl = imgurl;
        this.title = title;
        this.day_night = day_night;
        this.price = price;

    }
}
