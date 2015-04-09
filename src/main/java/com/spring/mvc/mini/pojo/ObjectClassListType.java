package com.spring.mvc.mini.pojo;

import java.util.ArrayList;

public class ObjectClassListType {
	ArrayList<ObjectClass> objectclasslist;

	public ArrayList<ObjectClass> getObjectclasslist() {
		return objectclasslist;
	}

	public void setObjectclasslist(ArrayList<ObjectClass> objectclasslist) {
		this.objectclasslist = objectclasslist;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (ObjectClass item:objectclasslist){
			sb.append(item);
		}
		return "ObjectClassListType [objectclasslist=" + sb.toString() + "]";
	}
}
