package com.spring.mvc.mini.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.mvc.mini.json.RequestStatusJsonParser;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.RequestStatus;
import com.spring.mvc.mini.pojo.RequestStatusListType;
import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.pojo.StatusType;
import com.spring.mvc.mini.pojo.UserInfo;
import com.spring.mvc.mini.svn.SVNHandler;
import com.spring.mvc.mini.xml.ObjectClassXMLPaser;

public class ScheduleFileUpdator {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleFileUpdator.class);

    @Autowired
    private RequestStatusJsonParser jsonParser;

    @Autowired
    private SVNHandler svnHandler;

    @Autowired
    private ObjectClassXMLPaser objectClassXMLPaser;

    @Autowired
    private MailSender mailSender;

    public void commitObjectClassXml() {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        LOG.info("Scheduler start at:" + currentTime);

        ArrayList<RequestStatus> requestStatuses = jsonParser.readStatus();

        for (RequestStatus status : requestStatuses) {

            int daysBetweenSubmitAndCurrent = Days.daysBetween(new DateTime(status.getSubmitDate()), new DateTime(currentTime)).getDays();

            if (StatusType.ongoing.equals(status.getStatus())) {
                if (daysBetweenSubmitAndCurrent >= 5) {

                    setCommitDateAndStatus(currentTime, requestStatuses, status);

                    LOG.info("MO CR " + status.getmocrid() + " was checked at:" + new Date());

                    try {
                        this.commitAndSendMail(status.getUserinfo(), appendCommitMessage(status), "Congratulation!");
                    } catch (Exception e) {
                        LOG.error(e.toString());
                    }
                }
            }

        }
        RequestStatusListType type = new RequestStatusListType();
        type.setRequestStatuses(requestStatuses);
        jsonParser.writeStatus(type);

        svnHandler.svnCheckin();
    }

    private String appendCommitMessage(RequestStatus status) {

        StringBuffer s = new StringBuffer();
        s.append("Final approval of MO CR ");
        s.append(status.getmocrid());
        s.append(" for ");

        for (ObjectClass objcls : status.getObjectClassesType().getObjectClasses()) {
            objectClassXMLPaser.AddObjectClass(objcls);
            s.append(objcls.getAbbreviation());
        }
        return s.toString();
    }

    private void setCommitDateAndStatus(Date currentTime, ArrayList<RequestStatus> requestStatuses, RequestStatus status) {
        int requestStatusIndex;
        requestStatusIndex = requestStatuses.indexOf(status);
        requestStatuses.get(requestStatusIndex).setCommitDate(currentTime);
        requestStatuses.get(requestStatusIndex).setStatus(StatusType.commited);
    }

    public void commitAndSendMail(UserInfo userInfo, String subject, String text) throws Exception {

        Address[] toAddress = {new InternetAddress(userInfo.getEmail())};

        mailSender.sendMail(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEmail(), toAddress, subject, text);
    }
}
