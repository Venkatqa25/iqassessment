package com.restassured.utils;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class TestUtils {

	private static Logger log = LogManager.getLogger(TestUtils.class.getName());

	public static String getResponseString(Response response) {

		log.info("Converting Json to String");
		String StrResponse = response.getBody().asString();
		log.debug(StrResponse);

		return StrResponse;

	}

	public static JsonPath jsonParser(String response) {
		log.info("Parsing String Response to Json");
		JsonPath jsonresextract = new JsonPath(response);
		log.debug(jsonresextract);

		return jsonresextract;

	}
	
	public static int getStatusCode(Response response) {

		log.info("Getting Response code");

		int Statuscode = response.getStatusCode();
		log.info(Statuscode);
		return Statuscode;

	}

	public static String getStatusMessage(Response response) {
		log.info("Getting Response Message");

		String StatusMessage = response.getStatusLine();
		log.info(StatusMessage);
		return StatusMessage;
	}

}
