package com.companyname.projectname.mocr.pojo;

import java.util.ArrayList;

public class MOCRequestStatusListType {
	
	ArrayList<MOCRequestStatus> mrsl;

	public ArrayList<MOCRequestStatus> getMrsl() {
		return mrsl;
	}

	public void setMrsl(ArrayList<MOCRequestStatus> mrsl) {
		this.mrsl = mrsl;
	}

	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		for (MOCRequestStatus mrs:mrsl){
			sb.append(mrs);
		}
		return "MOCRequestStatusListType [mrsl=" + sb + "]";
	}


}
