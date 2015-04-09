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
@RequestMapping("/objectclasslist")
@SessionAttributes("productList")
public class ObjectClassListController {
	
	static Logger LOGGER = LoggerFactory.getLogger(ObjectClassListController.class);
	
	@Autowired
	ObjectClassXMLPaser ocxp;
	
	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model) {
		model.addAttribute("ajaxRequest", AjaxUtils.isAjaxRequest(request));
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public String objectClassListType(Model model, @ModelAttribute("page") String page ) {

 			ArrayList<ObjectClass> ojbclslist = null;
 			try {
 				ojbclslist = ocxp.objectClassMapping();
 				Collections.reverse(ojbclslist);
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
 			
 			PagedListHolder<ArrayList<ObjectClass>> productList = new PagedListHolder(ojbclslist);
	        productList.setPageSize(100);
	        
	        model.addAttribute("productList",productList);
	        return null;
	}
	
	@RequestMapping(params={"jumppage"}, method = RequestMethod.GET)
	public String objectClassPagination(Model model, @ModelAttribute("jumppage") String jumppage, @ModelAttribute("productList") PagedListHolder<ArrayList<ObjectClass>> productList ) {
			
			LOGGER.info("@RequestMapping(params={\"jumppage\"}, method = RequestMethod.GET)");
        	 if (jumppage == null) {
    	        
     	    } else {
     	    	
     	        if ("next".equals(jumppage)) {
     	        	productList.nextPage();
     	        }
     	        else if ("previous".equals(jumppage)) {
     	        	productList.previousPage();
     	        } else if (isInteger(jumppage) > 0){
     	        	productList.setPage(isInteger(jumppage));
     	        } 
     	    }
        
	        model.addAttribute("productList",productList);
	        return null;
	}

	@RequestMapping(params={"searchcritical"}, method = RequestMethod.GET)
	public String objectClassSearch(Model model, @ModelAttribute("searchcritical") String searchcritical ) {
			
			LOGGER.info("@RequestMapping(params={\"searchcritical\"}, method = RequestMethod.GET)");
			ArrayList<ObjectClass> searchOjbclslist = null;
        	 if (searchcritical == null) {
    	        
     	    } else {
     	    	
     			ArrayList<ObjectClass> ojbclslist = null;
     			searchOjbclslist = new ArrayList<ObjectClass>();
     			try {
     				ojbclslist = ocxp.objectClassMapping();
     				
     				for(ObjectClass ojbcls:ojbclslist){
     					if (ojbcls.match(searchcritical)){
     						searchOjbclslist.add(ojbcls);
     					}
     				}
     				
     			} catch (Exception e) {
     				e.printStackTrace();
     			}
     	    }
        	 
        	 PagedListHolder<ArrayList<ObjectClass>> productList = new PagedListHolder(searchOjbclslist);
        	 productList.setPageSize(50);
        	 
	        model.addAttribute("productList",productList);
	        return null;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String processSubmit(Model model, @ModelAttribute("page") String page, 
			@ModelAttribute("productList") PagedListHolder<ArrayList<ObjectClass>> productList,
								@ModelAttribute("ajaxRequest") boolean ajaxRequest, RedirectAttributes redirectAttrs) {

		LOGGER.info("@RequestMapping(method=RequestMethod.POST)");
		String message = "Form submitted successfully.  Bound ";
		// Success response handling
		if (ajaxRequest) {
			// prepare model for rendering success message in this request
			model.addAttribute("message", message);
			return null;
		} else {
			// store a success message for rendering on the next request after redirect
			// redirect back to the form to render the success message along with newly bound values
			redirectAttrs.addFlashAttribute("message", message);
			return "redirect:/objectclasslist";			
		}
	}
	
	public int isInteger(String s) {
		int i;
	    try { 
	        i = Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return -1; 
	    }
	    // only got here if we didn't return false
	    return i;
	}
	
}
