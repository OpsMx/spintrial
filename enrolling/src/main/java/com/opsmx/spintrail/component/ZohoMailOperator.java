package com.opsmx.spintrail.component;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZohoMailOperator {
	
	@Value("${spring.application.mail.userID}")
	private String userId;

	@Value("${spring.application.mail.userID.password}")
	private String password;

	public void send(String firstName, String userName, String pass, String userMailID, String accountExpiryDate) {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "smtp.zoho.com");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.debug", "true");
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.debug.auth", "true");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("lalit@opsmx.com", "92!vikraM");
			}
		});

		String subject = "Your Spinnaker trial is ready.";
		String htmlBodyText = "Hi " + firstName	+ ",<p>Thank you for signing up for OpsMx Spinnaker. " +
				"You can access your trial at <a href=\"https://spinnakertrial.opsmx.com:9000\">https://spinnakertrial.opsmx.com:9000</a>.<p>" +
				"UserName: " + userName + "<br>Password: " + pass +
				"<p>Your subscription will expire in 7 days. Please contact us to extend your trial." +
				"<p>You can set up an <a href=\"https://meetings.hubspot.com/opsmx/spinnaker-trial\">office hour</a>  with a Spinnaker expert to get live help with your trial." +
				"<p><a href=\"https://blog.opsmx.com/?p=1926\">Here</a> is a tutorial on creating and running a pipeline in your Spinnaker trial." +
				"<p>If you are new to Spinnaker, you may want to familiarize yourself with <a href=\"https://www.spinnaker.io/concepts\">Spinnaker concepts</a>." +
				"<p>For questions, please send an email to <a href=mailto:spintrial@opsmx.com>spintrial@opsmx.com</a>." +
				"<p>Thank you, <br>OpsMx Spinnaker Team";
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("spintrial@opsmx.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(userMailID));
			message.setSubject(subject);
			message.setContent(htmlBodyText, "text/html");
			//message.setText(htmlBodyText);
			Transport.send(message);
				
		} catch (MessagingException  e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ZohoMailOperator().send("John", "jdoe", "321password", "nirmalya@opsmx.com", null);
	}
}