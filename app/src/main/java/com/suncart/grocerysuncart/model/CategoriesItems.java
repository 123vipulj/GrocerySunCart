package com.suncart.grocerysuncart.model;

public class CategoriesItems {
    String urlImg, cat_title;

    public CategoriesItems(String urlImg, String cat_title) {
        this.urlImg = urlImg;
        this.cat_title = cat_title;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
