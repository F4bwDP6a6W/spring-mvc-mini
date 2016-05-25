package com.spring.mvc.mini.pojo;

import java.util.List;

public class ObjectClassesType {

    private List<ObjectClass> objectClasses;

    public List<ObjectClass> getObjectClasses() {
        return objectClasses;
    }

    public void setObjectClasses(List<ObjectClass> objectClasses) {
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
