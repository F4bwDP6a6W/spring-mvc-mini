<<<<<<< HEAD
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
=======
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
        StringBuilder sb = new StringBuilder();
        for (ObjectClass item : objectClasses) {
            sb.append(item);
        }
        return "ObjectClassesType [objectClasses=" + sb + "]";
    }
}
>>>>>>> 158cee89e79a15fb22fc10af26bd6a164bf0bb02
