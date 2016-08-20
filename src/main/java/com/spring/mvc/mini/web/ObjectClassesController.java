package com.spring.mvc.mini.web;

import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.mvc.extensions.ajax.AjaxUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.mini.pojo.ObjectClass;
import com.spring.mvc.mini.xml.ObjectClassXMLPaser;

@Controller
@RequestMapping("/objectClasses")
@SessionAttributes("productList")
public class ObjectClassesController {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectClassesController.class);

    @Autowired
    private ObjectClassXMLPaser objectClassXMLPaser;

    @ModelAttribute
    public void ajaxAttribute(WebRequest request, Model model) {
        model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
    }


    @RequestMapping(method = RequestMethod.GET)
    public String enrichobjectClassesType(Model model, @ModelAttribute("page") String page) {

        ArrayList<ObjectClass> ojbclslist = getObjectClasses();

        Collections.reverse(ojbclslist);
        PagedListHolder productList = new PagedListHolder(ojbclslist);
        productList.setPageSize(100);

        model.addAttribute("productList", productList);
        return null;
    }

    private ArrayList<ObjectClass> getObjectClasses() {
        ArrayList<ObjectClass> ojbclslist = null;
        try {
            ojbclslist = objectClassXMLPaser.objectClassMapping();

        } catch (Exception e) {
            LOG.error("get XML Object Class exception", e);
        }
        return ojbclslist;
    }

    @RequestMapping(params = {"jumppage"}, method = RequestMethod.GET)
    public String handleObjectClassPagination(Model model, @ModelAttribute("jumppage") String jumppage, @ModelAttribute("productList") PagedListHolder<ArrayList<ObjectClass>> productList) {

        LOG.info("@RequestMapping(params={\"jumppage\"}, method = RequestMethod.GET)");
        if (jumppage != null) {

            if ("next".equals(jumppage)) {
                productList.nextPage();
            } else if ("previous".equals(jumppage)) {
                productList.previousPage();
            } else if (isInteger(jumppage) > 0) {
                productList.setPage(isInteger(jumppage));
            }
        }

        model.addAttribute("productList", productList);
        return null;
    }

    @RequestMapping(params = {"searchcritical"}, method = RequestMethod.GET)
    public String searchObjectClass(Model model, @ModelAttribute("searchcritical") String searchcritical) {

        LOG.info("@RequestMapping(params={\"searchcritical\"}, method = RequestMethod.GET)");
        ArrayList<ObjectClass> searchOjbclslist = null;
        if (searchcritical != null) {

            ArrayList<ObjectClass> ojbclslist = getObjectClasses();

            searchOjbclslist = new ArrayList<ObjectClass>();
            for (ObjectClass ojbcls : ojbclslist) {
                if (ojbcls.match(searchcritical)) {
                    searchOjbclslist.add(ojbcls);
                }
            }
        }

        PagedListHolder<ArrayList<ObjectClass>> productList = new PagedListHolder(searchOjbclslist);
        productList.setPageSize(50);

        model.addAttribute("productList", productList);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(Model model, @ModelAttribute("page") String page,
                         @ModelAttribute("productList") PagedListHolder<ArrayList<ObjectClass>> productList,
                         @ModelAttribute("ajaxRequest") boolean ajaxRequest, RedirectAttributes redirectAttrs) {

        LOG.info("@RequestMapping(method=RequestMethod.POST)");
        String message = "Form submitted successfully.  Bound ";

        if (ajaxRequest) {

            model.addAttribute("message", message);
            return null;
        } else {

            redirectAttrs.addFlashAttribute("message", message);
            return "redirect:/objectClasses";
        }
    }

    private int isInteger(String s) {
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
        return i;
    }

}
