package com.spring.mvc.mini.pojo;

import java.util.List;

public class RequestStatusListType {

	private List<RequestStatus> requestStatuses;

	public List<RequestStatus> getRequestStatuses() {
		return requestStatuses;
	}

	public void setRequestStatuses(List<RequestStatus> requestStatuses) {
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
