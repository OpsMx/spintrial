package com.opsmx.spintrail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpintrailApplication {
	
	/*
	 * @Bean public JavaMailSender getJavaMailSender() { JavaMailSenderImpl
	 * mailSender = new JavaMailSenderImpl(); mailSender.setHost("smtp.gmail.com");
	 * mailSender.setPort(587);
	 * 
	 * mailSender.setUsername("lalitv9293@gmail.com");
	 * mailSender.setPassword("Vikram92!");
	 * 
	 * Properties props = mailSender.getJavaMailProperties();
	 * props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.auth",
	 * "true"); props.put("mail.smtp.starttls.enable", "true");
	 * props.put("mail.debug", "true");
	 * 
	 * return mailSender; }
	 */

	public static void main(String[] args) {
		SpringApplication.run(SpintrailApplication.class, args);
	}

}
