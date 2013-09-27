package com.vampireneoapp.passiontimes.core;

import java.io.Serializable;

public class Category implements Serializable {


    private static final long serialVersionUID = -7775386652234764012L;

    protected String name;
    protected String categoryId;
    protected String subCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
