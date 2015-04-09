package com.spring.mvc.mini.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.mvc.mini.json.RequestStatusJsonMapping;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.MOCRequestStatus;
import com.spring.mvc.mini.pojo.MOCRequestStatusListType;
import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.pojo.StatusType;
import com.spring.mvc.mini.pojo.UserInfo;
import com.spring.mvc.mini.svn.SVNHandler;
import com.spring.mvc.mini.xml.ObjectClassXMLPaser;

public class ScheduleFileUpdator {
	
	static Logger LOGGER = LoggerFactory.getLogger(ScheduleFileUpdator.class);
	
	@Autowired
	RequestStatusJsonMapping rsjm;
	
	@Autowired
	SVNHandler sh;
	
	@Autowired
	ObjectClassXMLPaser ocxp;
	
	@Autowired
	MailSender ms;
	
    public void commitObjectClassXml()
    {	
		Calendar cal = Calendar.getInstance();
    	Date currenttime = cal.getTime();
    	
    	LOGGER.info("Scheduler start at:"+ currenttime);

    	ArrayList<MOCRequestStatus> mrsList = rsjm.readStatus();
    	
    	int mrsIndex = 0;
    	for(MOCRequestStatus mrs:mrsList){
    		
    		int days = Days.daysBetween(new DateTime(mrs.getSubmitDate()), new DateTime(currenttime)).getDays();

    		if(StatusType.ongoing.equals(mrs.getStatus())){
        		if(days >= 5){
        			
        			mrsIndex = mrsList.indexOf(mrs);
        			mrsList.get(mrsIndex).setCommitDate(currenttime);
        			mrsList.get(mrsIndex).setStatus(StatusType.commited);
        			
        			StringBuffer subjectsb = new StringBuffer();
        			subjectsb.append("Final approval of MO CR ");
        			subjectsb.append(mrs.getmocrid());
        			subjectsb.append(" for ");
        			
        			for(ObjectClass objcls:mrs.getOjbclslisttype().getObjectclasslist()){
        				ocxp.AddObjectClass(objcls);
        				subjectsb.append(objcls.getAbbrev());
        			}
        			
        			LOGGER.info("MO CR "+mrs.getmocrid()+" was checked at:"+ new Date());
        			
        			try {
						this.commitAndSendMail(mrs.getUserinfo(), subjectsb.toString(), "Congratulation!");
					} catch (MessagingException e) {
						LOGGER.error(e.toString());
					}
        		}
    		}

    	}
    	MOCRequestStatusListType mrslt = new MOCRequestStatusListType();
    	mrslt.setMrsl(mrsList);
    	rsjm.writeStatus(mrslt);
    	
    	sh.svnCheckin();
    }
    
	public void commitAndSendMail(UserInfo userinfo ,String subject, String text) throws AddressException, MessagingException{
		

		Address[] toAddress = {new InternetAddress(userinfo.getEmail())};
		
		ms.sendMail(userinfo.getUsername(),userinfo.getPassword(),userinfo.getEmail(),toAddress, subject, text);
	}
}
