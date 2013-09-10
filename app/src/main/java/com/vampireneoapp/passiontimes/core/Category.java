package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;

public class Category implements Serializable {


    private static final long serialVersionUID = -7775386652234764012L;

    protected int id;
    protected String name;
    protected String subCategory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
