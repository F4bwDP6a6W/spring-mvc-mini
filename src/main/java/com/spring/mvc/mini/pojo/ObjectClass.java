package com.spring.mvc.mini.pojo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ObjectClass implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	String id;
	
	@NotNull
	int intclass;
	
	@NotNull
	String abbrev;
	@NotNull
	String adaID;
	@NotNull
	String name;
	@NotNull
	String parents;
	
	String comment;
	
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
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public String getAdaID() {
		return adaID;
	}
	public void setAdaID(String adaID) {
		this.adaID = adaID;
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
		return "ObjectClass [id=" + id + ", intclass=" + intclass + ", abbrev="
				+ abbrev + ", adaID=" + adaID + ", name=" + name + ", parents="
				+ parents + ", comment=" + comment + "]";
	}
	
	public boolean match(String searchcritical){
		
		if (this.abbrev.contains(searchcritical)){
			return true;
		}
		if (this.abbrev.contains(searchcritical)){
			return true;
		}
		if (this.adaID.contains(searchcritical)){
			return true;
		}
		if (this.comment.contains(searchcritical)){
			return true;
		}
		if (this.id.contains(searchcritical)){
			return true;
		}
		if (this.name.contains(searchcritical)){
			return true;
		}
		if (this.parents.contains(searchcritical)){
			return true;
		}
		if (String.valueOf(this.intclass).contains(searchcritical)){
			return true;
		}
		return false;
	}
}
