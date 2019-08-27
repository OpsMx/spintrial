package com.opsmx.spintrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpintrailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpintrailApplication.class, args);
	}

	// HTTP port

	//@Value("${http.port}")
	//private int httpPort;

	// Let's configure additional connector to enable support for both HTTP and
	// HTTPS

	/*
	 * @Bean public ServletWebServerFactory servletContainer() {
	 * TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
	 * tomcat.addAdditionalTomcatConnectors(createStandardConnector()); return
	 * tomcat; }
	 * 
	 * private Connector createStandardConnector() { Connector connector = new
	 * Connector("org.apache.coyote.http11.Http11NioProtocol");
	 * connector.setPort(httpPort); return connector; }
	 */

}
