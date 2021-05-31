package com.restassured.APIAutomation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.When;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.restassured.utils.TestUtils;
import com.restassured.utils.URL;

import io.restassured.http.ContentType;
//import io.restassured.internal.common.assertion.Assertion;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CountriesandCapitals {

	Response response;

	private static Logger log = LogManager.getLogger(CountriesandCapitals.class.getName());

	public static ExtentReports report;
	public static ExtentTest logger;
	public static ExtentHtmlReporter htmlReporter;

	private static String countrycapital;
	private static String currencycodefmcountriesapi;
	private static String currencycodefmcapitalapi;
	static Assertion h_assert = new Assertion();
	private static int expecstatuscode = 200;
	private static boolean capitalfound = false;
	private static int i;

	// public static String WidgetUserId;

	@BeforeSuite
	public void report_setup() {

		htmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "//test-output//Assessment_APITesting_Report_310521.html");
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		report.setSystemInfo("OS", "Windows");
		report.setSystemInfo("Host Name", "IQVIA");
		report.setSystemInfo("Environment", "QA");
		report.setSystemInfo("User Name", "Venkatesh S");

		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setDocumentTitle("Extent Report-API Testing Report");
		htmlReporter.config().setReportName("Assessment - API Automation testing Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	@Test(priority = 1, enabled = true)
	public void fetch_CountriesList() {

		log.info("Start Countries List Test");

		logger = report.createTest("Countries List test");

		logger.info("Positive TC-> Fetching the countries list");

		String URI = URL.getEndPoint("/all?fields=name;capital;currencies;latlng");

		logger.info("Countries list End point : " + URI);

		response = RestRequestCalls.GETRequest(URI);

		String responsebody = TestUtils.getResponseString(response);

		String statusmsg = TestUtils.getStatusMessage(response);

		// Verifying response body is not empty
		if (responsebody != "") {
			logger.info("Status Message for Countries API : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.BROWN));
			System.out.println("Status Message for Countries API " + statusmsg);

			logger.pass("Country list Fetched Successfully ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.BROWN));
			System.out.println("Country List Response payload " + responsebody);

		}

		else {
			logger.info("Status Message for Countries  API : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.BROWN));
			System.out.println("Status Message for Countries API " + statusmsg);

			logger.fail("Fetching the Countries List failed ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.RED));
			System.out.println("Countries List Response payload " + responsebody);
			// h_assert.fail("Fetching the Capital details failed ");
		}

		int statuscode = TestUtils.getStatusCode(response);

		// Below assertion verifying the Status code in payload respond
		try {
			logger.info("Status Code for Countries API : " + statuscode);
			System.out.println("Status code for Countries API " + statuscode);
			Assertions.VerifyStatusCode(response, expecstatuscode);
			logger.pass("Countires List status code matches with status code : " + statuscode);
		} catch (Exception e) {

			logger.fail("Fetching Countries list gets Failed and Status code is not matched with Status code : "
					+ statuscode);

		}

		// Verifying Json schema validation
		try {

			response.then().assertThat().body(matchesJsonSchemaInClasspath("countriesschema.json"));
			logger.pass("Json Schema Validation Success for Countries API");
			System.out.println("Schema Validation Success for Countries API");
		} catch (Exception e) {
			// System.out.println(e);
			logger.fail("Json Schema Validation Fail for Countries API " + e);
			System.out.println("Schema Validation Failure for Countries API");
			h_assert.fail("Json Schema Validation Fail for Countries API ");

		}

		// Extracting the Capital from Countries response payload
		try {

			JsonPath jsonresextract1 = TestUtils.jsonParser(responsebody);
			List<String> jsonextractlist = jsonresextract1.getList("capital");
			int countriescount = jsonextractlist.size();
			System.out.println("Total number of Countries in List : " + countriescount);
			logger.info("Total number of Countries in List : " + countriescount);

			for (i = 0; i <= countriescount - 1; i++) {

				countrycapital = jsonextractlist.get(i);

				if (countrycapital.equalsIgnoreCase("New Delhi")) {
					System.out.println("Country capital : " + countrycapital + " Index value of " + i);
					capitalfound = true;
					logger.pass("Index Value : " + i + " for Country capital : ")
							.info(MarkupHelper.createLabel(countrycapital, ExtentColor.BROWN));

					break;
				}
			}

			System.out.println("Index value for Country Capital: " + i);

		} catch (Exception e) {

			logger.fail("Fetching the country capital gets failed : ")
					.info(MarkupHelper.createLabel(countrycapital, ExtentColor.RED));

		}

		// Extracting the currency code from Countries response payload
		try {

			JsonPath jsonresextract2 = TestUtils.jsonParser(responsebody);
			currencycodefmcountriesapi = jsonresextract2.getString("currencies[" + i + "].code[0]");

			System.out.println("Country currency code is " + currencycodefmcountriesapi);
			logger.pass("Country currency code : ")
					.info(MarkupHelper.createLabel(currencycodefmcountriesapi, ExtentColor.BROWN));

		} catch (Exception e) {
			logger.fail("Fetching the Country currency code gets failed : ")
					.info(MarkupHelper.createLabel(currencycodefmcountriesapi, ExtentColor.RED));
			h_assert.fail("Fetching the Country currency code gets failed ");

		}

	}

	@Test(priority = 2, enabled = true)
	public void fetch_Capitallist() {

		log.info("Starting Capital Details Test");
		logger = report.createTest("Capital Details test");

		logger.info(" Postive TC-> Fetching the Capital details");

		String URI = URL
				.getEndPoint("/capital/" + countrycapital + "?fields=name;capital;currencies;latlng;regionalBlocs");
		response = RestRequestCalls.GETRequest(URI);

		logger.info("Capital Details End point : " + URI);

		String responsebody = TestUtils.getResponseString(response);
		String statusmsg = TestUtils.getStatusMessage(response);
		int statuscode = TestUtils.getStatusCode(response);

		// Verifying response body is not empty
		if (responsebody != "") {
			logger.info("Status Message for Capital details : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.BROWN));
			System.out.println("Status Message for Capital details " + statusmsg);

			logger.pass("Capital details Fetched Successfully ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.BROWN));
			System.out.println("Capital details Response payload " + responsebody);

		}

		else {
			logger.info("Status Message for Capital details : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.RED));
			System.out.println("Status Message for Capital details " + statusmsg);

			logger.fail("Fetching the Capital details failed ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.RED));
			System.out.println("Capital details Response payload " + responsebody);

		}

		// Below assertion verifying the Status code in payload respond
		try {

			logger.info("Capital Details Status code : " + statuscode);
			Assertions.VerifyStatusCode(response, expecstatuscode);
			logger.pass("Capital Details Status code is matches with Status code : " + statuscode);
		}

		catch (Exception e) {

			logger.fail("Fetching Captial details gets Failed and Status code is not matches with status code : "
					+ statuscode);

		}

		// Verifying Json schema validation
		try {

			response.then().assertThat().body(matchesJsonSchemaInClasspath("capitalschema.json"));
			logger.pass("Json Schema Validation Success for Capital API");
			System.out.println("Schema Validation Success for Capital API");
		} catch (Exception e) {
			System.out.println(e);
			logger.fail("Json Schema Validation Fail for Capital API " + e);
			System.out.println("Schema Validation Failure for Capital API");
			h_assert.fail("Json Schema Validation Fail for Capital API ");

		}

		// Extracting the currency code from Capital response payload
		try {

			JsonPath jsonresextract2 = TestUtils.jsonParser(responsebody);
			currencycodefmcapitalapi = jsonresextract2.getString("currencies[0].code[0]");
			logger.pass("Capital currency code : ")
					.info(MarkupHelper.createLabel(currencycodefmcapitalapi, ExtentColor.BROWN));
			System.out.println("Capital currency code is " + currencycodefmcapitalapi);
		} catch (Exception e) {
			logger.fail("Fetching the Capital currency code gets failed : ")
					.info(MarkupHelper.createLabel(currencycodefmcapitalapi, ExtentColor.RED));
			h_assert.fail("Json Schema Validation Fail for Capital API ");

		}

		// Verifying the Capital currency code from Capital Api with Country list Api
		try {
			Assertions.VerifyAttributevalues(currencycodefmcapitalapi, currencycodefmcountriesapi);

			logger.pass("Capital Currency code :: " + currencycodefmcapitalapi
					+ " matches with Country Currency code :: " + currencycodefmcountriesapi);

		} catch (Exception e) {

			logger.fail("Capital Currency code :: " + currencycodefmcapitalapi
					+ "not matches with Country Currency code :: " + currencycodefmcountriesapi);

		}
	}

	@Test(priority = 3, enabled = true)
	public void Negative_TC_fetching_the_Capitaldetails() {

		logger = report.createTest("Negative Test for Capital Details ");

		logger.info("  Negative TC -> For fetching the Capital details");

		// Passing wrong endpoint by entering the resource with spelling mistake as
		// capita instead capital
		String URI = URL
				.getEndPoint("/capita/" + countrycapital + "?fields=name;capital;currencies;latlng;regionalBlocs");
		response = RestRequestCalls.GETRequest(URI);

		logger.info("Capital Details End point : " + URI);

		String responsebody = TestUtils.getResponseString(response);
		String statusmsg = TestUtils.getStatusMessage(response);

		if (responsebody != "") {
			logger.info("Status Message for Capital details : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.BROWN));
			System.out.println("Status Message for Capital details " + statusmsg);

			logger.pass("Capital details Fetched Successfully ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.BROWN));
			System.out.println("Capital details Response payload " + responsebody);

		}

		else {
			logger.info("Status Message for Capital details : ")
					.info(MarkupHelper.createLabel(statusmsg, ExtentColor.BROWN));
			System.out.println("Status Message for Capital details " + statusmsg);

			logger.fail("Fetching the Capital details failed ")
					.info(MarkupHelper.createLabel(responsebody, ExtentColor.RED));
			System.out.println("Capital details Response payload " + responsebody);
		}

		int statuscode = TestUtils.getStatusCode(response);

		// Below assertion verifying the Status code in payload respond
		try {

			logger.info("Capital Details Status code : " + statuscode);
			Assertions.VerifyStatusCode(response, expecstatuscode);
			logger.pass("Capital Details Status code is matches with Status code : " + statuscode);
		}

		catch (Exception e) {

			logger.fail("Fetching Captial details gets Failed and Status code is not matches with status code : "
					+ statuscode);
		}

	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.fail(result.getThrowable());
			logger.fail(MarkupHelper.createLabel(result.getName() + " ->This Test Case is FAILED", ExtentColor.RED));
		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.skip(result.getThrowable());
			logger.skip(
					MarkupHelper.createLabel(result.getName() + " ->This Test Case is SKIPPED", ExtentColor.YELLOW));

		} else if (result.getStatus() == ITestResult.SUCCESS) {

			logger.pass(MarkupHelper.createLabel(result.getName() + " -> This Test Case is PASSED", ExtentColor.GREEN));

		}
	}

	@AfterSuite
	// @AfterClass
	public void teardown() {
		report.flush();

	}

}
