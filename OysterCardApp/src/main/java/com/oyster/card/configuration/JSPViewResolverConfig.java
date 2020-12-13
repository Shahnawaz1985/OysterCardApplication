package com.oyster.card.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author Shahnawaz
 */
@Configuration
public class JSPViewResolverConfig {
	
	/**
	 * @return
	 */
	public ViewResolver jspViewResolver() {
	  InternalResourceViewResolver viewResolver = 
			  new InternalResourceViewResolver();
	  viewResolver.setViewClass(JstlView.class);
	  viewResolver.setPrefix("/jsp/");
	  viewResolver.setSuffix(".jsp");
	  viewResolver.setContentType("text/html");
	  return viewResolver;
	}

}
