package com.spring.mvc.mini.pojo;

import java.util.ArrayList;

public class ObjectClassesType {

    private ArrayList<ObjectClass> objectClasses;

    public ArrayList<ObjectClass> getObjectClasses() {
        return objectClasses;
    }

    public void setObjectClasses(ArrayList<ObjectClass> objectClasses) {
        this.objectClasses = objectClasses;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ObjectClass item : objectClasses) {
            sb.append(item);
        }
        return "ObjectClassesType [objectClasses=" + sb + "]";
    }
}
