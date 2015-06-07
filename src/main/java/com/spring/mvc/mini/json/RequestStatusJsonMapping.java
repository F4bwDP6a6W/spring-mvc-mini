package com.spring.mvc.mini.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.mvc.mini.pojo.RequestStatus;
import com.spring.mvc.mini.pojo.RequestStatusListType;
import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.properties.Properties;

@Component
public class RequestStatusJsonMapping {

    static Logger LOG = LoggerFactory.getLogger(RequestStatusJsonMapping.class);

    @Autowired
    private Properties properties;

    public int getMaxIntClass() {

        ArrayList<RequestStatus> list = this.readStatus();

        ArrayList<ObjectClass> objectClasses = list.get(list.size() - 1).getObjectClassListType().getObjectclasslist();

        return objectClasses.get(objectClasses.size() - 1).getIntclass();
    }

    public int getLatestmocrid() {

        ArrayList<RequestStatus> mrsList = this.readStatus();

        return mrsList.get(mrsList.size() - 1).getmocrid();
    }


    public void writeStatus(RequestStatusListType mrslt) {

        try {
            new ObjectMapper().writeValue(new File(properties.getJsonPath()), mrslt);
        } catch (IOException e) {
            LOG.error(e.toString());
        }

    }

    public ArrayList<RequestStatus> readStatus() {

        RequestStatusListType type = null;

        try {
            type = new ObjectMapper().readValue(new File(properties.getJsonPath()), RequestStatusListType.class);

            LOG.debug(type.toString());

        } catch (IOException e) {

            LOG.error(e.toString());
        }

        return type.getRequestStatuses();
    }


}
