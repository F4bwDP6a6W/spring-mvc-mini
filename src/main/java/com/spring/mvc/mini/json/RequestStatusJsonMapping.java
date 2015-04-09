package com.spring.mvc.mini.json;

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
import com.spring.mvc.mini.pojo.MOCRequestStatus;
import com.spring.mvc.mini.pojo.MOCRequestStatusListType;
import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.pojo.ObjectClassListType;
import com.spring.mvc.mini.pojo.StatusType;
import com.spring.mvc.mini.pojo.UserInfo;
import com.spring.mvc.mini.properties.PropertiesBean;

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
	
	public int getLatestmocrid(){

		ArrayList<MOCRequestStatus> mrsList = this.readStatus();
	
		int mocrid = mrsList.get(mrsList.size()-1).getmocrid(); 
		
		LOGGER.info("getLatestmocrid:"+mocrid);
		
		return mocrid;
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
