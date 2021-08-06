package com.zouyujie.micoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MicoderApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MicoderApplication.class, args);
	}
	//重写configure方法，否则在部署到tomcat时，接口将访问不到
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MicoderApplication.class);
	}

}
