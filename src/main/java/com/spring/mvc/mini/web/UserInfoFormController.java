package com.spring.mvc.mini.web;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.UserInfo;

@Controller
@RequestMapping("/userinfoform")
@SessionAttributes("userinfo")
public class UserInfoFormController {
	
	@Autowired
	private MailSender ms;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	@ModelAttribute("userinfo")
	public UserInfo createUserinfo() {
		return new UserInfo();
	}

	@RequestMapping(method=RequestMethod.GET)
	public void userinfoForm() {
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submit(UserInfo userinfo,
						 @ModelAttribute("ajaxRequest") boolean ajaxRequest,
						 Model model, RedirectAttributes redirectAttrs) {
		
		try {
			loginAndSendMail(userinfo);
		} catch (Exception e) {
			model.addAttribute("message", "Login Failed:"+e.toString());
			return null;
		}
		
		String message="Login Successful, Click ObjecClassForm to continue.";
		model.addAttribute("userinfo", userinfo);
		

		if (ajaxRequest) {

			model.addAttribute("message", message);
			return null;
		} else {

			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/";			
		}
	}

	private void loginAndSendMail(UserInfo userinfo) throws Exception{
		
		String subject = "Login successfully to mvc mini";
		String text="Welcome you,"+userinfo.getUsername()+"!\r\nYou can reserve the object classes now.";
		Address[] toAddress = {new InternetAddress(userinfo.getEmail())};
		
		ms.sendMail(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail(),toAddress, subject, text);
	}
	
}
