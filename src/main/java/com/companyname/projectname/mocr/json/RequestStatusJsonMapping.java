package com.companyname.projectname.mocr.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.companyname.projectname.mocr.pojo.MOCRequestStatus;
import com.companyname.projectname.mocr.pojo.MOCRequestStatusListType;
import com.companyname.projectname.mocr.pojo.ObjectClass;
import com.companyname.projectname.mocr.pojo.ObjectClassListType;
import com.companyname.projectname.mocr.pojo.StatusType;
import com.companyname.projectname.mocr.pojo.UserInfo;
import com.companyname.projectname.mocr.properties.PropertiesBean;

@Component
public class RequestStatusJsonMapping {
	
	static Logger LOGGER = LoggerFactory.getLogger(RequestStatusJsonMapping.class);
	
    @Autowired
    private PropertiesBean propertiesBean;
	
	public int getMaxIntClass(){
		
		ArrayList<MOCRequestStatus> mrsList = this.readStatus();
		
		ArrayList<ObjectClass> objclsList = mrsList.get(mrsList.size()-1).getOjbclslisttype().getObjectclasslist();
		int intclass = objclsList.get(objclsList.size()-1).getIntclass();
		
		LOGGER.debug("MaxIntClass:"+intclass);
		
		return intclass;
	}
	
	public int getLatestMoCrId(){

		ArrayList<MOCRequestStatus> mrsList = this.readStatus();
	
		int mocrId = mrsList.get(mrsList.size()-1).getMocrId(); 
		
		LOGGER.info("getLatestMoCrId:"+mocrId);
		
		return mocrId;
	}
	

	public void writeStatus(MOCRequestStatusListType mrslt){
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(propertiesBean.getJsonpath()), mrslt);
		} catch (IOException e) {
			LOGGER.error(e.toString());
		}
		
	}
	
	public ArrayList<MOCRequestStatus> readStatus(){
		ObjectMapper mapper = new ObjectMapper();
		MOCRequestStatusListType mrslt = null;
		
		try {
			mrslt = mapper.readValue(new File(propertiesBean.getJsonpath()), MOCRequestStatusListType.class);
			
			LOGGER.debug(mrslt.toString());
				
		} catch (IOException e) {
			
			LOGGER.error(e.toString());
		}
		
		ArrayList<MOCRequestStatus> mrsList = mrslt.getMrsl();
		return mrsList;
	}


}
