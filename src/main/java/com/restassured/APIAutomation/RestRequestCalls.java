package com.restassured.APIAutomation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestRequestCalls {

	/*
	 * This call is responsible to provide the request
	 */

	private static Logger log = LogManager.getLogger(RestRequestCalls.class.getName());

	public static Response GETRequest(String uRI) {

		log.info("Inside GetRequest Call");

		RequestSpecification requestSpec = RestAssured.given();
		requestSpec.contentType(ContentType.JSON);
		Response response = requestSpec.get(uRI);
		log.debug(requestSpec.log().all());
		return response;
	}
	

}
