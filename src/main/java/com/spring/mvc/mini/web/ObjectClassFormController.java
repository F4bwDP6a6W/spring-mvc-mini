package com.spring.mvc.mini.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.validation.Valid;

import com.spring.mvc.mini.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.spring.mvc.mini.json.RequestStatusJsonParser;
import com.spring.mvc.mini.mail.MailSender;
import com.spring.mvc.mini.pojo.RequestStatus;
import com.spring.mvc.mini.validation.ObjectClassDataValidator;

@Controller
@RequestMapping("/objectclassform")
@SessionAttributes({"ojbclslisttype", "userinfo", "mocrid"})
public class ObjectClassFormController {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectClassFormController.class);

    @Value("${mail.to}")
    private String mailto;

    @Value("${web.hostname}")
    private String webHostname;

    @Autowired
    private RequestStatusJsonParser requestStatusJsonParser;

    @Autowired
    private ObjectClassDataValidator objectClassDataValidator;

    @Autowired
    private MailSender mailSender;

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }

    @ModelAttribute("userinfo")
    public UserInfo createUserinfo() {
        return new UserInfo();
    }

    @ModelAttribute("mocrid")
    public String createMocrid() {
        return String.valueOf(1 + requestStatusJsonParser.getLatestmocrid());
    }

    @RequestMapping(method = RequestMethod.GET)
    public void handleObjectClassForm(Model model, @ModelAttribute("userinfo") UserInfo userinfo, @ModelAttribute("mocrid") String mocrid) {
        ObjectClassesType objectClassesType = new ObjectClassesType();

        ArrayList<ObjectClass> objectClasses = new ArrayList<ObjectClass>();
        objectClasses.add(createObjectClassInstance(0, userinfo, mocrid));

        objectClassesType.setObjectClasses(objectClasses);

        model.addAttribute("objectClassesType", objectClassesType);
    }


    //Add More
    @RequestMapping(params = {"objclscount"}, method = RequestMethod.POST)
    public void handleObjectClassFormWithParam(@Valid ObjectClassesType objectClassesType, @ModelAttribute("userinfo") UserInfo userinfo, @ModelAttribute("mocrid") String mocrid, @RequestParam String ObjectClassCount, Model model) {

        ArrayList<ObjectClass> objectClasses = objectClassesType.getObjectClasses();
        objectClasses.add(createObjectClassInstance(Integer.parseInt(ObjectClassCount), userinfo, mocrid));
        objectClassesType.setObjectClasses(objectClasses);
        model.addAttribute("ObjectClassesType", objectClassesType);
    }

    //Submit
    @RequestMapping(method = RequestMethod.POST)
    public String handleSubmit(ObjectClassesType objectClassesType, @ModelAttribute("userinfo") UserInfo userinfo,
                               @ModelAttribute("mocrid") String mocrid,
                               @ModelAttribute("ajaxRequest") boolean ajaxRequest,
                               Model model, RedirectAttributes redirectAttrs) {

        if (userinfo.getUsername() == null) {
            model.addAttribute("message", "Sorry, Please submit UserInfoForm at first. ");
            return null;
        }

        ArrayList<ObjectClass> objectClasses = objectClassesType.getObjectClasses();

        try {
            objectClassDataValidator.checkData(objectClasses);
            objectClassDataValidator.checkmocrid(mocrid, requestStatusJsonParser.getLatestmocrid());

        } catch (Exception e) {
            model.addAttribute("message", e.toString());
            return null;
        }

        constructDebugMessage(objectClasses);

        RequestStatus s = new RequestStatus();
        Calendar cal = Calendar.getInstance();

        s.setSubmitDate(cal.getTime());
        s.setmocrid(Integer.parseInt(mocrid));
        s.setObjectClassesType(objectClassesType);
        s.setStatus(StatusType.ongoing);
        s.setUserinfo(userinfo);

        ArrayList<RequestStatus> l = requestStatusJsonParser.readStatus();
        l.add(s);

        RequestStatusListType type = new RequestStatusListType();
        type.setRequestStatuses(l);
        requestStatusJsonParser.writeStatus(type);

        try {
            this.commitAndSendMail(userinfo, constructMailSubject(mocrid, objectClasses), constructMailText(userinfo, mocrid));
        } catch (Exception e) {

            model.addAttribute("message", e.toString());
            return null;
        }

        String message = "MO CR:" + mocrid + " committed. System will send mail to reviewers.";

        if (ajaxRequest) {
            model.addAttribute("message", message);
            return null;
        } else {
            redirectAttrs.addFlashAttribute("message", message);
            return "redirect:/objectclassform";
        }
    }

    private String constructMailText(@ModelAttribute("userinfo") UserInfo userinfo, @ModelAttribute("mocrid") String mocrid) {
        StringBuffer textsb = new StringBuffer();
        textsb.append("Hi,\r\n\r\nThese MO CR's shall be approved if no other comments.\r\nIf you have any comment, please comment on this page:\r\n");
        textsb.append("http://");
        textsb.append(webHostname);
        textsb.append(":8080/spring-mvc-mini/requeststatus?mocrid=");
        textsb.append(mocrid);
        textsb.append("\r\nor mailto:");
        textsb.append(userinfo.getEmail());
        textsb.append("\r\n");
        textsb.append("The request will be committed in 5 days.\r\n\r\nThanks.\r\n");
        textsb.append("http://");
        textsb.append(webHostname);
        textsb.append(":8080/spring-mvc-mini/");
        return textsb.toString();
    }

    private String constructMailSubject(@ModelAttribute("mocrid") String mocrid, ArrayList<ObjectClass> objectClasses) {
        StringBuffer subjectsb = new StringBuffer();
        subjectsb.append("Forthcoming approval of MO CR ");
        subjectsb.append(mocrid);
        subjectsb.append(" for ");
        for (ObjectClass objcls : objectClasses) {
            subjectsb.append(objcls.getAbbreviation());
        }
        return subjectsb.toString();
    }

    private void constructDebugMessage(ArrayList<ObjectClass> objectClasses) {
        StringBuffer debugmessage = new StringBuffer();
        for (ObjectClass objcls : objectClasses) {
            debugmessage.append(objcls);
        }

        LOG.debug(debugmessage.toString());
    }

    private ObjectClass createObjectClassInstance(int i, UserInfo userinfo, String mocrid) {
        ObjectClass objcls = new ObjectClass();
        objcls.setIntclass(requestStatusJsonParser.getMaxIntClass() + 1 + i);

        Calendar cal = Calendar.getInstance();
        Date currenttime = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY.MM.DD");

        objcls.setComment("# " + userinfo.getEmail() + " " + sdf.format(currenttime) + " MO CR " + mocrid);
        return objcls;
    }

    public void commitAndSendMail(UserInfo userinfo, String subject, String text) throws Exception {

        Address[] toAddress = {new InternetAddress(mailto), new InternetAddress(userinfo.getEmail())};

        mailSender.sendMail(userinfo.getUsername(), userinfo.getPassword(), userinfo.getEmail(), toAddress, subject, text);
    }

}
