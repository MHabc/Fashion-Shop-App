package com.example.fashionapp.Domain;

public class SliderItem {
    private String url;
    public SliderItem(String url)
    {
        this.url=url;
    }
    public SliderItem(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
