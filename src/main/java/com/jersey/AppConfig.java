package com.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

public class AppConfig extends ResourceConfig{
	public AppConfig() {
        packages("com.jersey.controller");
        property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/views");
        register(JspMvcFeature.class);
    }
}
