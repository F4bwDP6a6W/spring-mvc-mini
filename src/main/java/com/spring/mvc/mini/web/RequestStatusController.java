package com.spring.mvc.mini.web;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import com.spring.mvc.mini.pojo.RequestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.mini.json.RequestStatusJsonParser;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.RequestStatusListType;
import com.spring.mvc.mini.pojo.UserInfo;

@Controller
@RequestMapping("/requeststatus")
public class RequestStatusController {
	
	static Logger LOGGER = LoggerFactory.getLogger(RequestStatusController.class);
	
	@Autowired
	RequestStatusJsonParser requestStatusJsonParser;
	
	@Autowired
	MailSender ms;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void handleObjectClassForm(Model model) {
		model.addAttribute("miniequeststatus", new RequestStatus());
	}
	
	@RequestMapping(params={"mocrid"},method=RequestMethod.GET)
	public void enrichObjectClassFormWithParam(@RequestParam String mocrid, Model model) {
		
		ArrayList<RequestStatus> mrsList = requestStatusJsonParser.readStatus();
		
		for(RequestStatus mrs:mrsList){
			
			if(mrs.getmocrid() == Integer.parseInt(mocrid)){
				LOGGER.debug("mrs.getmocrid() is:"+mrs.getmocrid());
				model.addAttribute("miniequeststatus", mrs);
			}
		}
		
	}

	@RequestMapping(method=RequestMethod.POST)
	public String submit(@ModelAttribute("miniequeststatus") RequestStatus miniequeststatus,
						 @ModelAttribute("ajaxRequest") boolean ajaxRequest,
						 Model model, RedirectAttributes redirectAttrs) {
		
		if(miniequeststatus.getObjectClassesType() == null){
			ArrayList<RequestStatus> mrsList = requestStatusJsonParser.readStatus();
			
			if (isMocridEquals(miniequeststatus, model, mrsList)){
				model.addAttribute("message", "SUCCESS:MO CR ID:"+miniequeststatus.getmocrid()+" is presenting.");
				return null;
			} else {
				model.addAttribute("miniequeststatus", new RequestStatus());
				model.addAttribute("message", "FAILED:MO CR ID:"+miniequeststatus.getmocrid()+" is not existing.");
			}
		}
		
		ArrayList<RequestStatus> requestStatuses =  requestStatusJsonParser.readStatus();
		
		int index = 0;
		for (RequestStatus item:requestStatuses){
			if(item.getmocrid() == miniequeststatus.getmocrid()){
				index = requestStatuses.indexOf(item);
				break;
			}
		}
		
		requestStatuses.get(index).setComments(miniequeststatus.getComments());
		requestStatuses.get(index).setObjectClassesType(miniequeststatus.getObjectClassesType());
		
		RequestStatusListType type = new RequestStatusListType();
		
		type.setRequestStatuses(requestStatuses);
		requestStatusJsonParser.writeStatus(type);
		
		LOGGER.debug(type.toString());

		try {
			this.commentAndSendMail(requestStatuses.get(index).getUserinfo(), constructMailSubject(miniequeststatus), constructMailText(miniequeststatus, requestStatuses, index));
		} catch (Exception e) {
			model.addAttribute("message", e.toString());
			return null;
		}

		String message = "Your update was submitted.";
		
		if (ajaxRequest) {
			model.addAttribute("message", message);
			return null;
		} else {
			return "redirect:/requeststatus";			
		}
	}

	private String constructMailText(@ModelAttribute("miniequeststatus") RequestStatus miniequeststatus, ArrayList<RequestStatus> requestStatuses, int index) {
		StringBuffer textsb = new StringBuffer();
		textsb.append("New Comments: \r\n");
		textsb.append(miniequeststatus.getComments());
		textsb.append(" \r\n");

		if(!requestStatuses.get(index).getObjectClassesType().equals(miniequeststatus.getObjectClassesType())){
			textsb.append("Object Classes updated: \r\n");
			textsb.append(miniequeststatus.getObjectClassesType().toString());
		}

		textsb.append("http://localhost:8080/spring-mvc-mini/requeststatus?mocrid=");
		textsb.append(miniequeststatus.getmocrid());
		textsb.append(" \r\n");
		textsb.append(" \r\n");
		return textsb.toString();
	}

	private String constructMailSubject(@ModelAttribute("miniequeststatus") RequestStatus miniequeststatus) {
		StringBuffer subjectsb = new StringBuffer();
		subjectsb.append("MO CR:");
		subjectsb.append(miniequeststatus.getmocrid());
		subjectsb.append(" Updated");
		return subjectsb.toString();
	}

	private boolean isMocridEquals(@ModelAttribute("miniequeststatus") RequestStatus miniequeststatus, Model model, ArrayList<RequestStatus> mrsList) {
		boolean boo = false;
		for(RequestStatus mrs:mrsList){

            if(mrs.getmocrid() == miniequeststatus.getmocrid()){

                model.addAttribute("miniequeststatus", mrs);
                boo = true;
            }
        }
		return boo;
	}

	public void commentAndSendMail(UserInfo userinfo ,String subject, String text) throws Exception{
		
		Address[] toAddress = {new InternetAddress(userinfo.getEmail())};
		
		ms.sendMail(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail(),toAddress, subject, text);
	}

}
