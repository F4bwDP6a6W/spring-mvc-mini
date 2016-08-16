package com.spring.mvc.mini.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ObjectClass implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    private String id;

    @NotNull
    private int intclass;

    @NotNull
    private String abbreviation;
    @NotNull
    private String packageName;
    @NotNull
    private String name;
    @NotNull
    private String parents;

    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntclass() {
        return intclass;
    }

    public void setIntclass(int intclass) {
        this.intclass = intclass;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ObjectClass [id=" + id + ", intclass=" + intclass + ", abbreviation="
                + abbreviation + ", packageName=" + packageName + ", name=" + name + ", parents="
                + parents + ", comment=" + comment + "]";
    }

    public boolean match(String searchcritical) {

        if (this.abbreviation.contains(searchcritical)) {
            return true;
        }
        if (this.abbreviation.contains(searchcritical)) {
            return true;
        }
        if (this.packageName.contains(searchcritical)) {
            return true;
        }
        if (this.comment.contains(searchcritical)) {
            return true;
        }
        if (this.id.contains(searchcritical)) {
            return true;
        }
        if (this.name.contains(searchcritical)) {
            return true;
        }
        if (this.parents.contains(searchcritical)) {
            return true;
        }
        return String.valueOf(this.intclass).contains(searchcritical);
    }
}
