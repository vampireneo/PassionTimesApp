package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;
import java.util.ArrayList;

public class ChannelDetail implements Serializable {


    private static final long serialVersionUID = -7786547652234765896L;

    protected String id;
    protected String topic;
    protected String host;
    protected String ts;
    protected String thumbnail;
    protected String adMp4;
    protected ArrayList<String> mp3;
    protected ArrayList<String> video;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getAdMp4() {
        return adMp4;
    }

    public void setAdMp4(String adMp4) {
        this.adMp4 = adMp4;
    }

    public ArrayList<String> getMp3() {
        return mp3;
    }

    public void setMp3(ArrayList<String> mp3) {
        this.mp3 = mp3;
    }

    public ArrayList<String> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<String> video) {
        this.video = video;
    }
}
