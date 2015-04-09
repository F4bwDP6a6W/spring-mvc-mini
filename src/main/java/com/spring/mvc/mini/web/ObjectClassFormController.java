package com.spring.mvc.mini.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.mini.json.RequestStatusJsonMapping;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.MOCRequestStatus;
import com.spring.mvc.mini.pojo.MOCRequestStatusListType;
import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.pojo.ObjectClassListType;
import com.spring.mvc.mini.pojo.StatusType;
import com.spring.mvc.mini.pojo.UserInfo;
import com.spring.mvc.mini.properties.PropertiesBean;
import com.spring.mvc.mini.validation.ObjectClassDataValidator;

@Controller
@RequestMapping("/objectclassform")
@SessionAttributes({"ojbclslisttype", "userinfo", "mocrid"} )
public class ObjectClassFormController {
	
	static Logger LOGGER = LoggerFactory.getLogger(ObjectClassFormController.class);
	
	@Autowired
	RequestStatusJsonMapping rsjm;
	
	@Autowired
	ObjectClassDataValidator ocdv;

	@Autowired
	PropertiesBean propertiesBean;
	
	@Autowired
	MailSender ms;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}
	
	@ModelAttribute("userinfo")
	public UserInfo createUserinfo() {
		return new UserInfo();
	}
	
	@ModelAttribute("mocrid")
	public String createmocrid() {
		return String.valueOf(1+rsjm.getLatestmocrid());
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public void objectClassForm(Model model, @ModelAttribute("userinfo") UserInfo userinfo, @ModelAttribute("mocrid") String mocrid) {
		ObjectClassListType ojbclslisttype = new ObjectClassListType();
		
		ArrayList<ObjectClass> objclsList = new ArrayList<ObjectClass>();
		objclsList.add(createObjectClassInstance(0,userinfo,mocrid));
		ojbclslisttype.setObjectclasslist(objclsList);
		
		model.addAttribute("ojbclslisttype", ojbclslisttype);
	}
	

	
	//Add More
	@RequestMapping(params={"objclscount"},method=RequestMethod.POST)
	public void objectClassFormWithParam(@Valid ObjectClassListType ojbclslisttype, @ModelAttribute("userinfo") UserInfo userinfo, @ModelAttribute("mocrid") String mocrid, @RequestParam String objclscount, Model model) {
		
		ArrayList<ObjectClass> objclsList = ojbclslisttype.getObjectclasslist();
		
//		for(int i=1;i<Integer.parseInt(objclscount)+1;i++){
			
			objclsList.add(createObjectClassInstance(Integer.parseInt(objclscount),userinfo,mocrid));

//		}
		
		ojbclslisttype.setObjectclasslist(objclsList);
		
		model.addAttribute("ojbclslisttype", ojbclslisttype);
	}
	
	//Submit
	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(ObjectClassListType ojbclslisttype, @ModelAttribute("userinfo") UserInfo userinfo, 
								@ModelAttribute("mocrid") String mocrid,
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, 
								Model model, RedirectAttributes redirectAttrs) {
		
		if (userinfo.getUsername() == null){
			String message = "Sorry, Please submit UserInfoForm at first. ";
			model.addAttribute("message", message);
			return null;
		}
		
		ArrayList<ObjectClass> objclsList = ojbclslisttype.getObjectclasslist();
		
		try {
			ocdv.checkData(objclsList);
			ocdv.checkmocrid(mocrid,rsjm.getLatestmocrid());
			
		} catch (Exception e) {
			model.addAttribute("message", e.toString());
			return null;
		}
		
		StringBuffer debugmessage = new StringBuffer();
		for(ObjectClass objcls:objclsList){
			debugmessage.append(objcls);
		}
		
		LOGGER.debug(debugmessage.toString());
		
		ArrayList<MOCRequestStatus> mrsl = rsjm.readStatus();
		
		MOCRequestStatus mrs = new MOCRequestStatus();
		Calendar cal = Calendar.getInstance();

		mrs.setSubmitDate(cal.getTime());
		mrs.setmocrid(Integer.parseInt(mocrid));
		mrs.setOjbclslisttype(ojbclslisttype);
		mrs.setStatus(StatusType.ongoing);
		mrs.setUserinfo(userinfo);
		
		mrsl.add(mrs);
		
		MOCRequestStatusListType mrslt = new MOCRequestStatusListType();
		mrslt.setMrsl(mrsl);
		rsjm.writeStatus(mrslt);
		
		String message = "MO CR:"+mocrid+" committed. System will send mail to reviewers.";
		
		StringBuffer subjectsb = new StringBuffer();
		subjectsb.append("Forthcoming approval of MO CR ");
		subjectsb.append(mocrid);
		subjectsb.append(" for ");
		for(ObjectClass objcls:objclsList){
			subjectsb.append(objcls.getAbbrev());
		}
		
		StringBuffer textsb = new StringBuffer();
		textsb.append("Hi,\r\n\r\nThese MO CR's shall be approved if no other comments.\r\nIf you have any comment, please comment on this page:\r\n");
		textsb.append("http://");
		textsb.append(propertiesBean.getWebhostname());
		textsb.append(":8080/spring-mvc-mini/requeststatus?mocrid=");
		textsb.append(mocrid);
		textsb.append("\r\nor mailto:");
		textsb.append(userinfo.getEmail());
		textsb.append("\r\n");
		textsb.append("The request will be committed in 5 days.\r\n\r\nThanks.\r\n");
		textsb.append("http://");
		textsb.append(propertiesBean.getWebhostname());
		textsb.append(":8080/spring-mvc-mini/");
		
		try {
			this.commitAndSendMail(userinfo, subjectsb.toString(), textsb.toString());
		} catch (MessagingException e) {
			
			model.addAttribute("message", e.toString());
			return null;
		}
		
		// Success response handling
		if (ajaxRequest) {
			// prepare model for rendering success message in this request
			model.addAttribute("message", message.toString());
			return null;
		} else {
			// store a success message for rendering on the next request after redirect
			// redirect back to the form to render the success message along with newly bound values
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/objectclassform";			
		}
	}
	
	private ObjectClass createObjectClassInstance(int i, UserInfo userinfo, String mocrid){
		ObjectClass objcls = new ObjectClass();
		objcls.setIntclass(rsjm.getMaxIntClass()+1+i);	
		
		Calendar cal = Calendar.getInstance();
    	Date currenttime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.DD");
		
		objcls.setComment("# " + userinfo.getEmail() +" " + sdf.format(currenttime) + " MO CR "+ mocrid);
		return objcls;
	}
	
	public void commitAndSendMail(UserInfo userinfo ,String subject, String text) throws AddressException, MessagingException{
		
		Address[] toAddress = {new InternetAddress(propertiesBean.getMailto()), new InternetAddress(userinfo.getEmail())};
		
		ms.sendMail(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail(),toAddress, subject, text);
	}
	
}
