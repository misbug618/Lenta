package com.example.inmoso.lenta;

import android.graphics.Bitmap;

/**
 * Created by inmoso on 19.03.16.
 */
    // шаблон получаемых данных из интернета
    // заголовок, позаголовок, текст, картинка

public class OneNews {
    private String mTitle;
    private String mContent;
    private String mCategory;
    private String mLink;
    private String mImage;
    private String mDate;

    public OneNews(String title, String content, String category, String link, String image, String date) {
        this.mTitle = title;
        this.mContent = content;
        this.mCategory = category;
        this.mLink = link;
        this.mImage = image;
        this.mDate = date;
    }


    public void setTitle(String title){
        mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setDate (String date){
        mDate = date;
    }

    public String getDate(){
        return mDate;
    }

    public void setContent(String content){
        mContent = content;
    }

    public String getContent(){
        return mContent;
    }

    public void setCategory(String category){
        mCategory = category;
    }

    public String getCategory(){
        return mCategory;
    }


    public String getmLink() {
        return mLink;
    }

    public void setmLink(String link) {
       mLink = link;
    }

    public void setImage(String image){
        mImage = image;
    }

    public String getmImage(){
        return mImage;
    }
}
