package com.groupe2;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/sensors").setViewName("home");
		registry.addViewController("/addSensor").setViewName("home");
		registry.addViewController("/changeFrequency").setViewName("changeFrequency");
		registry.addViewController("/data").setViewName("data");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/admin").setViewName("admin");
	}
}
