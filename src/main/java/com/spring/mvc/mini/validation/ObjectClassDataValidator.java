package com.spring.mvc.mini.validation;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.xml.ObjectClassXMLPaser;

@Component
public class ObjectClassDataValidator {

	@Autowired
	private ObjectClassXMLPaser objectClassXMLPaser;
	
	public void checkData(ArrayList<ObjectClass> objclsListFormIn) throws Exception{
		
		ArrayList<ObjectClass> objclsListXmlIn = objectClassXMLPaser.objectClassMapping();
		
		for (ObjectClass objclsItemFormIn:objclsListFormIn){
			
			boolean parentsMark = false;
			
			if(!objclsItemFormIn.getAbbreviation().equals(objclsItemFormIn.getAbbreviation().toUpperCase())){
				throw new ObjectClassDataValidationException("FAILED:ObjectClass abbrev:\""+objclsItemFormIn.getAbbreviation()+"\" should be all uppercase.");
			}
			
			for (ObjectClass objclsItemXmlIn:objclsListXmlIn){

				if(objclsItemXmlIn.getId().equals(objclsItemFormIn.getId())){
					throw new ObjectClassDataValidationException("FAILED:ObjectClass id:\""+objclsItemFormIn.getId()+"\" is already reserved.");
				}
				if(objclsItemXmlIn.getAbbreviation().equals(objclsItemFormIn.getAbbreviation())){
					throw new ObjectClassDataValidationException("FAILED:ObjectClass abbrev:\""+objclsItemFormIn.getAbbreviation()+"\" is already reserved.");
				}
				if(objclsItemXmlIn.getId().equals(objclsItemFormIn.getParents())){
					parentsMark = true;
				}
			}
			
			for (ObjectClass objclsItemFormInInner:objclsListFormIn){
				if(objclsItemFormInInner.getId().equals(objclsItemFormIn.getParents())){
					parentsMark = true;
				}
			}
			
			if (!parentsMark){
				throw new ObjectClassDataValidationException("FAILED:ObjectClass parent:\""+objclsItemFormIn.getParents()+"\" is not existing.");
			}
		}
		
	}
	
	public void checkmocrid(String mocrid,int latestmocrid) throws Exception{
		if (Integer.parseInt(mocrid) <= latestmocrid ){
			throw new ObjectClassDataValidationException("FAILED:MO CR ID:\""+mocrid+"\" is not valid anymore, please refresh and recommit again.");
		}
	}
}
