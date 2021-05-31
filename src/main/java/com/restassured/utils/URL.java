package com.restassured.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class URL {
	
	private static Logger log = LogManager.getLogger(URL.class.getName());
	public static String BaseURL = "https://restcountries.eu/rest/v2";
	
	public static String getEndPoint() {
		log.info("Base URI :" +BaseURL);
		return BaseURL;
	}
	
	public static String getEndPoint(String resource){
		log.info("URI End Point " + BaseURL + resource);
		
		return BaseURL + resource;
		
	}
	

}
