package com.companyname.projectname.mocr.web;

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

import com.companyname.projectname.mocr.json.RequestStatusJsonMapping;
import com.companyname.projectname.mocr.mail.MailSender;
import com.companyname.projectname.mocr.pojo.MOCRequestStatus;
import com.companyname.projectname.mocr.pojo.MOCRequestStatusListType;
import com.companyname.projectname.mocr.pojo.UserInfo;

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
//	public MOCRequestStatus createRequestStatus() {
//		return new MOCRequestStatus();
//	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void objectClassForm(Model model) {
		model.addAttribute("mocrequeststatus", new MOCRequestStatus());
	}
	
	@RequestMapping(params={"mocrid"},method=RequestMethod.GET)
	public void objectClassFormWithParam(@RequestParam String mocrid, Model model) {
		
		ArrayList<MOCRequestStatus> mrsList = rsjm.readStatus();
		
		for(MOCRequestStatus mrs:mrsList){
			
			if(mrs.getMocrId() == Integer.parseInt(mocrid)){
				LOGGER.debug("mrs.getMocrId() is:"+mrs.getMocrId());
				model.addAttribute("mocrequeststatus", mrs);
			}
		}
		
	}

	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(@ModelAttribute("mocrequeststatus") MOCRequestStatus mocrequeststatus, 
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) {
		
		if(mocrequeststatus.getOjbclslisttype() == null){
			ArrayList<MOCRequestStatus> mrsList = rsjm.readStatus();
			
			boolean boo = false;
			for(MOCRequestStatus mrs:mrsList){
				
				if(mrs.getMocrId() == mocrequeststatus.getMocrId()){
					
					model.addAttribute("mocrequeststatus", mrs);
					boo = true;
				}
			}
			
			if (boo){
				model.addAttribute("message", "SUCCESS:MO CR ID:"+mocrequeststatus.getMocrId()+" is presenting.");
				return null;
			} else {
				model.addAttribute("mocrequeststatus", new MOCRequestStatus());
				model.addAttribute("message", "FAILED:MO CR ID:"+mocrequeststatus.getMocrId()+" is not existing.");
			}
		}
		
		ArrayList<MOCRequestStatus> mrsl =  rsjm.readStatus();
		
		int index = 0;
		for (MOCRequestStatus item:mrsl){
			if(item.getMocrId() == mocrequeststatus.getMocrId()){
				index = mrsl.indexOf(item);
				break;
			}
		}
		
		mrsl.get(index).setComments(mocrequeststatus.getComments());
		mrsl.get(index).setOjbclslisttype(mocrequeststatus.getOjbclslisttype());
		
		MOCRequestStatusListType mrslt = new MOCRequestStatusListType();
		
		mrslt.setMrsl(mrsl);
		rsjm.writeStatus(mrslt);
		
		LOGGER.debug(mrslt.toString());
		
		StringBuffer subjectsb = new StringBuffer();
		subjectsb.append("MO CR:");
		subjectsb.append(mocrequeststatus.getMocrId());
		subjectsb.append(" Updated");
		
		StringBuffer textsb = new StringBuffer();
		textsb.append("New Comments: \r\n");
		textsb.append(mocrequeststatus.getComments());
		textsb.append(" \r\n");
		
		try {
			if(!mrsl.get(index).getOjbclslisttype().equals(mocrequeststatus.getOjbclslisttype())){
				textsb.append("Object Classes updated: \r\n");
				textsb.append(mocrequeststatus.getOjbclslisttype().toString());
			}
			
			textsb.append("http://localhost:8080/companyname-projectname-mocr/requeststatus?mocrid=");
			textsb.append(mocrequeststatus.getMocrId());
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
