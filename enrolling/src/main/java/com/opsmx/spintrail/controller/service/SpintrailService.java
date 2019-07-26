package com.opsmx.spintrail.controller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

@Service
public class SpintrailService {

	@Autowired
	public JavaMailSender sender;
	public JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	public String userRegistration(String payload) {

		String firstName = new String();
		String lastName = new String();
		String emailId = new String();
		String mobileNo = new String();
		JSONObject payloadJSONResponseObj = new JSONObject();

		try {
			payloadJSONResponseObj = (JSONObject) parser.parse(payload);
			firstName = (String) payloadJSONResponseObj.get("firstName");
			lastName = (String) payloadJSONResponseObj.get("lastName");
			emailId = (String) payloadJSONResponseObj.get("emailId");
			mobileNo = (String) payloadJSONResponseObj.get("phonenumber");

			boolean isEmailValid = mailValidate(emailId.trim());

			if (isEmailValid) {
				String responseOfAD = createUserInAD(firstName, lastName, emailId, mobileNo);
				System.out.println("\n Output of AD :: " +responseOfAD );
				JSONObject responseOfADInJSONObj = (JSONObject) parser.parse(responseOfAD);
				boolean isUserCreated = (boolean) responseOfADInJSONObj.get("userCreated");
				String userName = (String) responseOfADInJSONObj.get("userName");
				String userPass = (String) responseOfADInJSONObj.get("userPass");
				String description = (String) responseOfADInJSONObj.get("description");

				if (isUserCreated) {
					sendMail(firstName, userName, userPass, emailId);
					payloadJSONResponseObj.put("success", true);
					payloadJSONResponseObj.put("response",
							"User creation is done! Please check your register email Id");
					return payloadJSONResponseObj.toJSONString();
				} else {
					payloadJSONResponseObj.put("success", false);
					payloadJSONResponseObj.put("response", description);
					return payloadJSONResponseObj.toJSONString();
				}
			} else {
				payloadJSONResponseObj.put("success", false);
				payloadJSONResponseObj.put("response", "email id is invalid");
				return payloadJSONResponseObj.toJSONString();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String createUserInAD(String firstName, String lastName, String emailId, String mobile) {

		Process exec;
		try {
			String scriptAndInputs = "C:\\Users\\opsmxadmn\\Documents\\newrest.ps1 " + firstName + " " + lastName + " "
					+ emailId + " " + mobile;
			
			System.out.println("\n script and there inputs :: " + scriptAndInputs);
			
			exec = Runtime.getRuntime().exec(new String[] { "powershell.exe", "-c", scriptAndInputs });
			exec.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
			String line = "";
			String tempLine = "";
			while ((tempLine = reader.readLine()) != null) {
				line = line + tempLine.trim() + System.lineSeparator();
			}

			BufferedReader reader2 = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
			String line2 = "";
			String tempLine2 = "";
			while ((tempLine2 = reader2.readLine()) != null) {
				line2 = line2 + tempLine2.trim() + System.lineSeparator();
			}

			reader.close();
			reader2.close();

			if (exec.exitValue() == 0) {
				return line;
			} else {
				return line2;
			}

		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("spintrail creating user in AD error", e);
		}
	}

	public boolean mailValidate(String mailID) {

		HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
		HttpRequest request;

		try {
			String url = "https://api.trumail.io/v2/lookups/json?email=" + mailID;
			request = requestFactory.buildGetRequest(new GenericUrl(url));
			String rawResponse = request.execute().parseAsString();
			JSONObject responseObj = (JSONObject) parser.parse(rawResponse);
			boolean isDeliverable = (boolean) responseObj.get("deliverable");
			return isDeliverable;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String sendMail(String firstName, String userName, String pass, String userMailID) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		String subject = "Welcome to OpsMx Spinnaker Trail";
		String htmlBodyText = "Hi " + firstName
				+ ",\n \nThanks for registering up to keep in touch with OpsMx Spinnaker Trail. Here is your \n\nUserName - "
				+ userName + "\nPassword - " + pass
				+ "\n\nStart using OpsMx Spinnaker Trail by click here 'http://spinnakertrial.opsmx.com:32300' \n\n All the Best \n OpsMx";
		try {
			helper.setTo(userMailID);
			helper.setSubject(subject);
			helper.setText(htmlBodyText);

		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error while sending mail ..";
		}
		sender.send(message);
		return "Mail Sent Success!";
	}

	public static void main(String[] args) {
		SpintrailService st = new SpintrailService();
		st.mailValidate("lalitv92@gmail.com");

	}

}
