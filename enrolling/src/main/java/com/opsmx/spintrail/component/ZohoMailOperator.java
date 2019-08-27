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

		String subject = "Your OpsMx Spinnaker free trial is ready for use!";
		String htmlBodyText = "Hi " + firstName
				+ ",\n \nThank you for signing up for OpsMx Spinnaker free trial. You can start using it at https://spinnakertrial.opsmx.com:9000 . \n\nUserName - "
				+ userName + "\nPassword - " + pass
				+ "\n\nPlease note your subscription will expire in 7 Days on "+ accountExpiryDate +" . \n\nHere are some useful information: \nCreating and running a pipeline in Spinnaker https://www.spinnaker.io/concepts/pipelines .\nUser manual of OpsMx Spinnaker free trial https://docs.opsmx.com/spinnakerTrial .\n\n For questions or issues, please send an email to 'spintrial@opsmx.com'. \n\n All the Best \n OpsMx";

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("spintrial@opsmx.com"));
			message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(userMailID));
			message.setSubject(subject);
			message.setText(htmlBodyText);
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
}