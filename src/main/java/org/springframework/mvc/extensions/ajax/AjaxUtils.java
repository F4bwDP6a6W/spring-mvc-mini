package org.springframework.mvc.extensions.ajax;

import org.springframework.web.context.request.WebRequest;

public class AjaxUtils {

	public static boolean isAjaxRequest(WebRequest webRequest) {
		String requestedWith = webRequest.getHeader("X-Requested-With");
		return requestedWith != null && "XMLHttpRequest".equals(requestedWith);
	}

	private AjaxUtils() {}

}
