package com.spring.mvc.mini.json;

import com.spring.mvc.mini.pojo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by n2zhang on 2015/6/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:**/test.xml"})
public class RequestStatusJsonParserTest1 {

    @Resource
    private RequestStatusJsonParser requestStatusJsonParser;

    @Test
    public void test_writeStatus(){

        RequestStatusListType requestStatusListType = new RequestStatusListType();

        ArrayList<RequestStatus> requestStatuses = new ArrayList<RequestStatus>();

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@test");
        userInfo.setPassword("password");
        userInfo.setUsername("username");

        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setUserinfo(userInfo);
        requestStatus.setComments("comments");
        requestStatus.setCommitDate(new Date());
        requestStatus.setHoldDate(new Date());
        requestStatus.setmocrid(1);


        ObjectClassesType objectClassesType = new ObjectClassesType();


        ObjectClass objectClass = new ObjectClass();
        objectClass.setAbbreviation("abbreviation");
        objectClass.setComment("comment");
        objectClass.setId("id");
        objectClass.setIntclass(1);
        objectClass.setName("name");
        objectClass.setPackageName("package name");
        objectClass.setParents("parent");

        ArrayList<ObjectClass> objectClasses = new ArrayList<ObjectClass>();
        objectClasses.add(objectClass);
        objectClassesType.setObjectClasses(objectClasses);

        requestStatus.setObjectClassesType(objectClassesType);

        requestStatus.setStatus(StatusType.commited);
        requestStatus.setSubmitDate(new Date());
        requestStatuses.add(requestStatus);
        requestStatusListType.setRequestStatuses(requestStatuses);
//        requestStatusJsonParser = new RequestStatusJsonParser();
        requestStatusJsonParser.writeStatus(requestStatusListType);
    }
}
