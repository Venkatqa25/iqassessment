package com.restassured.APIAutomation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;
import com.restassured.utils.TestUtils;

import io.restassured.response.Response;


public class Assertions {
	
	private static Logger log = LogManager.getLogger(Assertions.class.getName());
	
	public static void VerifyStatusCode(Response response, int status) {
		Assert.assertEquals(TestUtils.getStatusCode(response),status);
		
	}
	
	public static void VerifyStatusMessage(Response response, String status) {
		Assert.assertEquals(TestUtils.getStatusMessage(response), status);
	}
	
	public static void VerifyAttributevalues(String actualvalue, String expectedvalue) {
		Assert.assertEquals(actualvalue,expectedvalue);
		
	}
}
