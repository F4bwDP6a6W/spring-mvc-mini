package com.companyname.projectname.mocr.pojo;

import java.util.Date;

public class MOCRequestStatus {
	
	int mocrId;
	Date submitDate;
	Date commitDate;
	Date holdDate;
	ObjectClassListType ojbclslisttype;
	UserInfo userinfo;
	StatusType status;
	String comments;
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public Date getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(Date commitDate) {
		this.commitDate = commitDate;
	}
	public Date getHoldDate() {
		return holdDate;
	}
	public void setHoldDate(Date holdDate) {
		this.holdDate = holdDate;
	}
	public UserInfo getUserinfo() {
		return userinfo;
	}
	public void setUserinfo(UserInfo userinfo) {
		this.userinfo = userinfo;
	}
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
	}
	public ObjectClassListType getOjbclslisttype() {
		return ojbclslisttype;
	}
	public void setOjbclslisttype(ObjectClassListType ojbclslisttype) {
		this.ojbclslisttype = ojbclslisttype;
	}
	public int getMocrId() {
		return mocrId;
	}
	public void setMocrId(int mocrId) {
		this.mocrId = mocrId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "MOCRequestStatus [mocrId=" + mocrId + ", submitDate="
				+ submitDate + ", commitDate=" + commitDate + ", holdDate="
				+ holdDate + ", ojbclslisttype=" + ojbclslisttype
				+ ", userinfo=" + userinfo + ", status=" + status
				+ ", comments=" + comments + "]";
	}
}
