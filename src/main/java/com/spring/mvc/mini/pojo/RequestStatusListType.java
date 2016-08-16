<<<<<<< HEAD
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
=======
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
		
		StringBuilder s = new StringBuilder();
		for (RequestStatus r: requestStatuses){
			s.append(r);
		}
		return "RequestStatusListType [requestStatuses=" + s + "]";
	}


}
>>>>>>> 158cee89e79a15fb22fc10af26bd6a164bf0bb02
