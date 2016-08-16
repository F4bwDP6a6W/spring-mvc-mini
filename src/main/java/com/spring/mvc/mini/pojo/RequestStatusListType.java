package com.spring.mvc.mini.pojo;

import java.util.ArrayList;

public class RequestStatusListType {

	private ArrayList<RequestStatus> requestStatuses;

	public ArrayList<RequestStatus> getRequestStatuses() {
		return requestStatuses;
	}

	public void setRequestStatuses(ArrayList<RequestStatus> requestStatuses) {
		this.requestStatuses = requestStatuses;
	}

	@Override
	public String toString() {
		
		StringBuffer s = new StringBuffer();
		for (RequestStatus r: requestStatuses){
			s.append(r);
		}
		return "RequestStatusListType [requestStatuses=" + s + "]";
	}


}
