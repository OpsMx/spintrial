package com.opsmx.spintrail.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

	@Autowired
	public JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);

	}
	
	
	public static void main(String[] args) {
		EmailServiceImpl el = new EmailServiceImpl();
		el.sendSimpleMessage("lalitv92@gmail.com", "No subject", "Hi there how are you brother");
	}
}