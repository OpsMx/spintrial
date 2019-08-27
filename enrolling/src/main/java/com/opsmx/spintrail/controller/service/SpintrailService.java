package com.opsmx.spintrail.controller.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.opsmx.spintrail.component.ZohoMailOperator;

@Service
public class SpintrailService {
	
	@Autowired
	private ZohoMailOperator zohoMailOperator;
	
	public JSONParser parser = new JSONParser();

	@SuppressWarnings("unchecked")
	public String userRegistration(String payload) {

		String firstName = new String();
		String lastName = new String();
		String emailId = new String();
		String mobileNo = new String();
		String accountExpiryDate = new String();
		
		JSONObject payloadJSONResponseObj = new JSONObject();

		try {
			payloadJSONResponseObj = (JSONObject) parser.parse(payload);
			firstName = (String) payloadJSONResponseObj.get("firstName");
			lastName = (String) payloadJSONResponseObj.get("lastName");
			emailId = (String) payloadJSONResponseObj.get("emailId");
			mobileNo = (String) payloadJSONResponseObj.get("phonenumber");
			accountExpiryDate = (String) payloadJSONResponseObj.get("UserAccountExpiryDate");
			

			// boolean isEmailValid = mailValidate(emailId.trim());
			boolean isEmailValid = true;

			if (isEmailValid) {
				String responseOfAD = createUserInAD(firstName, lastName, emailId, mobileNo);
				System.out.println("\n Output of AD :: " + responseOfAD);
				JSONObject responseOfADInJSONObj = (JSONObject) parser.parse(responseOfAD);
				boolean isUserCreated = (boolean) responseOfADInJSONObj.get("userCreated");
				String userName = (String) responseOfADInJSONObj.get("userName");
				String userPass = (String) responseOfADInJSONObj.get("userPass");
				String description = (String) responseOfADInJSONObj.get("description");

				if (isUserCreated) {
					zohoMailOperator.send(firstName, userName, userPass, emailId, accountExpiryDate);
					payloadJSONResponseObj.put("success", true);
					payloadJSONResponseObj.put("response",
							"You will receive an email within a few minutes with instructions to access your OpsMx Spinnaker free trial.");
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


	public static void main(String[] args) {
		SpintrailService st = new SpintrailService();

	}

}
