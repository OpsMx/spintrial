package com.opsmx.spintrail.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.opsmx.spintrail.component.ZohoMailOperator;
import com.opsmx.spintrail.controller.service.SpintrailService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class SpintrailController {

	@Autowired
	private SpintrailService stServiceObj;
	
	@Autowired
	private ZohoMailOperator zohoMailOperator;
	
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String registerUser(HttpServletRequest request, @RequestBody String payload) {
		return stServiceObj.userRegistration(payload);
	}

	@RequestMapping("/testAPI")
	public void testAPI() {
		zohoMailOperator.send("Lalit", "test", "dashhj8y@%", "lalitv92@gmail.com","28/09/2020");
	}

	public static void main(String[] args) {

	}

}
