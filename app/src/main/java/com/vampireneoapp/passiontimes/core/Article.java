package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable {


    private static final long serialVersionUID = -7783546652064588896L;

    protected String thumbnail;
    protected String desc;
    protected String author;
    protected String url;
    protected String title;
    protected String ts;
    protected String category;
    protected String subCategory;
    protected String id;
    protected String content;
    protected ArrayList<String> images;
    protected ArrayList<String> images_thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImagesThumbnail() {
        return images_thumbnail;
    }

    public void setImagesThumbnail(ArrayList<String> images_thumbnail) {
        this.images_thumbnail = images_thumbnail;
    }
}
