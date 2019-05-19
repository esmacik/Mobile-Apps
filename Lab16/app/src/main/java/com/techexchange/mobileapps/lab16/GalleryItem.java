package com.techexchange.mobileapps.lab16;

public class GalleryItem {

    private String url;
    private String id;
    private String caption;

    public GalleryItem() {

    }

    public GalleryItem(String url, String id, String caption) {
        this.url = url;
        this.id = id;
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString(){
        return "[ID: "+id+", URL: "+url+", Caption: "+caption+"]";
    }
}