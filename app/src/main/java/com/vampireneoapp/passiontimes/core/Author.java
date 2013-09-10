package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;

public class Author implements Serializable {


    private static final long serialVersionUID = -7775386652234765896L;

    protected int id;
    protected String name;
    protected String content;
    protected String profilePic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profile_pic) {
        this.profilePic = profile_pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
