package com.spring.mvc.mini.web;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.spring.mvc.mini.xml.ObjectClassXMLPaser;

@Controller
@RequestMapping("/objectclassxml")
public class ObjectClassXmlController {
	
	static Logger LOG = LoggerFactory.getLogger(ObjectClassXmlController.class);
	
	@Autowired
	private ObjectClassXMLPaser objectClassXMLPaser;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public HttpEntity<byte[]> getObjectClassesXml(ModelMap map, HttpServletResponse response) {
		
		 byte[] documentBody=null;
		
			try {
				documentBody = objectClassXMLPaser.getTextConent();

			} catch (Exception e) {
				e.printStackTrace();
			}

	    HttpHeaders header = new HttpHeaders();
	    header.setContentType(new MediaType("application", "xml"));
	    header.setContentLength(documentBody.length);
	    return new HttpEntity<byte[]>(documentBody, header);
	}
		
}
