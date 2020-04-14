package com.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.specification.RequestSpecification;

/* ****************************************************************************************
Class Name  : TestBase 

Author : Asmit Behara
*******************************************************************************************/

public class TestBase {
	
	public static Properties prop;
	public static ExtentReports extent;
	public static ExtentTest extentTest;
	public static RequestSpecification basereq;
	
	public TestBase() {
		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(".//Resource//config.properties");
			prop.load(ip);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@BeforeSuite
	public void setupSuite() {
		String dateName = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss").format(new Date());
		extent = new ExtentReports(System.getProperty("user.dir")+"/Reports/"
				+ prop.getProperty("projectName")+ "_" + dateName + ".html", true);	
	}	
	
	@AfterMethod
	public void tearDown(ITestResult result){
		
		if(result.getStatus()==ITestResult.FAILURE){
			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED is "+result.getName()); //to add name in extent report
//			extentTest.log(LogStatus.FAIL, "TEST CASE FAILED is "+result.getThrowable()); //to add error/exception in extent report		
		}
		else if(result.getStatus()==ITestResult.SKIP){
			extentTest.log(LogStatus.SKIP, "Test Case SKIPPED is " + result.getName());
		}
		else if(result.getStatus()==ITestResult.SUCCESS){
//			extentTest.log(LogStatus.PASS, "Test Case PASSED is " + result.getName());
		}
		extent.endTest(extentTest); 
		extent.flush();
	}
}
