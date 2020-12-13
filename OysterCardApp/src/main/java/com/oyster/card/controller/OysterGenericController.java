package com.oyster.card.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * @author Shahnawaz
 */
@Controller
public class OysterGenericController implements ErrorController {
	
	private static final String PATH = "/error";
	
	 private final ErrorAttributes errorAttributes;

	  @Autowired
	  public OysterGenericController(ErrorAttributes errorAttributes) {
	    Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
	    this.errorAttributes = errorAttributes;
	  }

	  @Override
	  public String getErrorPath() {
	    return PATH;
	  }

	  @RequestMapping("/error")
	  public Map<String, Object> error(HttpServletRequest aRequest){
	     Map<String, Object> body = getErrorAttributes(aRequest,getTraceParameter(aRequest));
	     String trace = (String) body.get("trace");
	     if(trace != null){
	       String[] lines = trace.split("\n\t");
	       body.put("trace", lines);
	     }
	     return body;
	  }

	  private boolean getTraceParameter(HttpServletRequest request) {
	    String parameter = request.getParameter("trace");
	    if (parameter == null) {
	        return false;
	    }
	    return !"false".equals(parameter.toLowerCase());
	  }

	  private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
		WebRequest requestAttributes =  new ServletWebRequest(aRequest);
	    return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
	  }

}
