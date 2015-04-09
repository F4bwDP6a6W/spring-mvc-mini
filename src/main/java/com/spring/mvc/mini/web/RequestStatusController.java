package com.spring.mvc.mini.web;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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

import com.spring.mvc.mini.json.RequestStatusJsonMapping;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.MOCRequestStatus;
import com.spring.mvc.mini.pojo.MOCRequestStatusListType;
import com.spring.mvc.mini.pojo.UserInfo;

@Controller
@RequestMapping("/requeststatus")
//@SessionAttributes({"requeststatus"} )
public class RequestStatusController {
	
	static Logger LOGGER = LoggerFactory.getLogger(RequestStatusController.class);
	
	@Autowired
	RequestStatusJsonMapping rsjm;
	
	@Autowired
	MailSender ms;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
//	@ModelAttribute("requeststatus")
//	public miniequestStatus createRequestStatus() {
//		return new miniequestStatus();
//	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void objectClassForm(Model model) {
		model.addAttribute("miniequeststatus", new MOCRequestStatus());
	}
	
	@RequestMapping(params={"mocrid"},method=RequestMethod.GET)
	public void objectClassFormWithParam(@RequestParam String mocrid, Model model) {
		
		ArrayList<MOCRequestStatus> mrsList = rsjm.readStatus();
		
		for(MOCRequestStatus mrs:mrsList){
			
			if(mrs.getmocrid() == Integer.parseInt(mocrid)){
				LOGGER.debug("mrs.getmocrid() is:"+mrs.getmocrid());
				model.addAttribute("miniequeststatus", mrs);
			}
		}
		
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@ModelAttribute("miniequeststatus") MOCRequestStatus miniequeststatus, 
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) {
		
		if(miniequeststatus.getOjbclslisttype() == null){
			ArrayList<MOCRequestStatus> mrsList = rsjm.readStatus();
			
			boolean boo = false;
			for(MOCRequestStatus mrs:mrsList){
				
				if(mrs.getmocrid() == miniequeststatus.getmocrid()){
					
					model.addAttribute("miniequeststatus", mrs);
					boo = true;
				}
			}
			
			if (boo){
				model.addAttribute("message", "SUCCESS:MO CR ID:"+miniequeststatus.getmocrid()+" is presenting.");
				return null;
			} else {
				model.addAttribute("miniequeststatus", new MOCRequestStatus());
				model.addAttribute("message", "FAILED:MO CR ID:"+miniequeststatus.getmocrid()+" is not existing.");
			}
		}
		
		ArrayList<MOCRequestStatus> mrsl =  rsjm.readStatus();
		
		int index = 0;
		for (MOCRequestStatus item:mrsl){
			if(item.getmocrid() == miniequeststatus.getmocrid()){
				index = mrsl.indexOf(item);
				break;
			}
		}
		
		mrsl.get(index).setComments(miniequeststatus.getComments());
		mrsl.get(index).setOjbclslisttype(miniequeststatus.getOjbclslisttype());
		
		MOCRequestStatusListType mrslt = new MOCRequestStatusListType();
		
		mrslt.setMrsl(mrsl);
		rsjm.writeStatus(mrslt);
		
		LOGGER.debug(mrslt.toString());
		
		StringBuffer subjectsb = new StringBuffer();
		subjectsb.append("MO CR:");
		subjectsb.append(miniequeststatus.getmocrid());
		subjectsb.append(" Updated");
		
		StringBuffer textsb = new StringBuffer();
		textsb.append("New Comments: \r\n");
		textsb.append(miniequeststatus.getComments());
		textsb.append(" \r\n");
		
		try {
			if(!mrsl.get(index).getOjbclslisttype().equals(miniequeststatus.getOjbclslisttype())){
				textsb.append("Object Classes updated: \r\n");
				textsb.append(miniequeststatus.getOjbclslisttype().toString());
			}
			
			textsb.append("http://localhost:8080/spring-mvc-mini/requeststatus?mocrid=");
			textsb.append(miniequeststatus.getmocrid());
			textsb.append(" \r\n");
			textsb.append(" \r\n");
		
			this.commentAndSendMail(mrsl.get(index).getUserinfo(), subjectsb.toString(), textsb.toString());
		} catch (MessagingException e) {
			model.addAttribute("message", e.toString());
			return null;
		} catch (NullPointerException e){
			model.addAttribute("message", "FAILED:The MO CR id is not existing!");
			return null;	
		}

		String message = "Your update was submitted.";
		
		// Success response handling
		if (ajaxRequest) {
			// prepare model for rendering success message in this request
			model.addAttribute("message", message);
			return null;
		} else {
			// store a success message for rendering on the next request after redirect
			// redirect back to the form to render the success message along with newly bound values
			//			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/requeststatus";			
		}
	}
	
	public void commentAndSendMail(UserInfo userinfo ,String subject, String text) throws AddressException, MessagingException{
		
		Address[] toAddress = {new InternetAddress(userinfo.getEmail())};
		
		ms.sendMail(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail(),toAddress, subject, text);
	}

}
