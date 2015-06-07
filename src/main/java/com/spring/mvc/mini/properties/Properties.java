package com.spring.mvc.mini.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    @Value("${path.json}")
    private String jsonPath;

    @Value("${path.xml}")
    private String xmlPath;

    @Value("${mail.starttls.enable}")
    private String starttlsEnable;

    @Value("${mail.auth}")
    private String auth;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private String port;

    @Value("${svn.rep.url}")
    private String repURL;

    @Value("${path.rep}")
    private String repPath;

    @Value("${svn.username}")
    private String username;

    @Value("${svn.password}")
    private String password;

    @Value("${mail.to}")
    private String mailto;

    @Value("${web.hostname}")
    private String webHostname;

    public String getJsonPath() {
        return jsonPath;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public String getStarttlsEnable() {
        return starttlsEnable;
    }

    public String getAuth() {
        return auth;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getRepURL() {
        return repURL;
    }

    public String getRepPath() {
        return repPath;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMailto() {
        return mailto;
    }

    public String getWebHostname() {
        return webHostname;
    }

}
