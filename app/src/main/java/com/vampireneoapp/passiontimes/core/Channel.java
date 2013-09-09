package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable {


    private static final long serialVersionUID = -7783546652234765896L;

    protected int id;
    protected String title;
    protected String fb;
    protected String desc;
    protected String host;
    protected String icon;
    protected ArrayList<String> adMp4;
    protected ArrayList<String> adMp3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<String> getAdMp4() {
        return adMp4;
    }

    public void setAdMp4(ArrayList<String> adMp4) {
        this.adMp4 = adMp4;
    }

    public ArrayList<String> getAdMp3() {
        return adMp3;
    }

    public void setAdMp3(ArrayList<String> adMp3) {
        this.adMp3 = adMp3;
    }
}
