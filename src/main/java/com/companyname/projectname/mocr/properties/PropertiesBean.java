package com.companyname.projectname.mocr.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesBean {

	@Value("${path.json}")
	private String jsonpath;
	
	@Value("${path.xml}")
	private String xmlpath;

	@Value("${mail.starttls.enable}")
	private String starttlsEnable;
	
	@Value("${mail.auth}")
	private String auth;
	
	@Value("${mail.host}")
	private String host;
	
	@Value("${mail.port}")
	private String port;
	
	@Value("${svn.rep.url}")
	private String repurl;
	
	@Value("${path.rep}")
	private String reppath;
	
	@Value("${svn.username}")
	private String username;
	
	@Value("${svn.password}")
	private String password;
	
	@Value("${mail.to}")
	private String mailto;
	
	@Value("${web.hostname}")
	private String webhostname;

	public String getJsonpath() {
		return jsonpath;
	}

	public String getXmlpath() {
		return xmlpath;
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

	public String getRepurl() {
		return repurl;
	}

	public String getReppath() {
		return reppath;
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

	public String getWebhostname() {
		return webhostname;
	}

}
