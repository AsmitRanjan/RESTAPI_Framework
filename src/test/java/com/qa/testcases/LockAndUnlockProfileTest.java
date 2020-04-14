package com.qa.testcases;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.pojo.LockUser;
import com.qa.pojo.UnlockUser;
import com.qa.pojo.UserQuestions;
import com.qa.supportfiles.Resources;
import com.qa.util.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

import java.util.Arrays;

/* ****************************************************************************************
Class Name  : AccountServicesTest 

Author : Asmit Behara
*******************************************************************************************/

public class LockAndUnlockProfileTest extends TestBase{
	
	public LockAndUnlockProfileTest() {
		super();
	}
	
	@BeforeTest
	public void setUpTest() {
		//Base URL
		basereq = new RequestSpecBuilder().setBaseUri(prop.getProperty("HOST")).addHeader("client_id", prop.getProperty("client_id")).
				  addHeader("client_secret", prop.getProperty("client_secret")).setContentType(ContentType.JSON).build();
	}
	
	@DataProvider
	public Object[][] getAPITestData(){
		Object data[][] = ReusableMethods.getTestData("Test1");
		return data;
	}
	
	
	@Test(priority=1, dataProvider="getAPITestData")
	public void LockUnlockUserProfile(String scnName, String execute, String logOnId, String question, String questionAttribute, String answer){	
		extentTest = extent.startTest("Lock and Unlock user profile - " + scnName);
		
		if (execute.equalsIgnoreCase("No")) {
	        throw new SkipException(scnName + " is Skipped");
	    }
		System.out.println("before flag");
		//To Lock user
		boolean flag = true;
		int sCode = 0;
		int count = 0;
		UserQuestions uQuestion = new UserQuestions();
		uQuestion.setQuestion(question);
		uQuestion.setQuestionAttribute(questionAttribute);
		uQuestion.setAnswer(answer);	
		LockUser lockUser = new LockUser();
		lockUser.setLogonID(logOnId);
		lockUser.setUserQuestions(Arrays.asList(uQuestion));
		lockUser.setImpactAuthentication(true);		
		System.out.println("before request");
		
		while(flag) {
			Response res = 	
			given().
				relaxedHTTPSValidation().and().spec(basereq).body(lockUser).and().
			when().
					post(Resources.profileQuestion()).
			then().
					assertThat().contentType(ContentType.JSON).and().
					extract().response();
			sCode = res.getStatusCode();
			System.out.println("after req");
			if(sCode==423) {
				flag = false;
			}
			count++;
			if(count==10) {
				break;
			}
		}
		
		if(sCode==423) {
			extentTest.log(LogStatus.PASS, "Successfully provided wrong answer to lock the user's Account");
		}else {
			extentTest.log(LogStatus.FAIL, "Unable to provide wrong answer to lock the user's Account");
		}
		
		//To get the Status Of Account
		Response res2 = 
		given().
			relaxedHTTPSValidation().and().spec(basereq).header("webLogonID", logOnId).and().
		when().
				get(Resources.statusOfAccount()).
		then().
				assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
				extract().response();
			
		JsonPath js = ReusableMethods.rawToJson(res2);
		boolean accStatus = js.get("lockedInternally");
		if(accStatus) {
			extentTest.log(LogStatus.PASS, "Account is locked in CSR application");
		}else {
			extentTest.log(LogStatus.FAIL, "Account is not locked in CSR application");
		}
		
		//To UnLock the user
		UnlockUser unlockUser = new UnlockUser();
		unlockUser.setLogonID(logOnId);
		unlockUser.setImpersonationID("testft999");
		
		Response res3 = 
		given().
			relaxedHTTPSValidation().and().spec(basereq).body(unlockUser).and().
		when().
				post(Resources.unlockUser()).
		then().
				assertThat().statusCode(204).and().contentType(ContentType.JSON).and().
				extract().response();
		
		int unLockStatusCode = res3.getStatusCode();
		if(unLockStatusCode==204) {
			extentTest.log(LogStatus.PASS, "Successfully hit the unlock API");
		}else {
			extentTest.log(LogStatus.FAIL, "Unable to hit the unlock API");
		}
		
		//To get the Status Of Account
		Response res4 = 
		given().
			relaxedHTTPSValidation().and().spec(basereq).header("webLogonID", logOnId).and().
		when().
				get(Resources.statusOfAccount()).
		then().
				assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
				extract().response();
			
		JsonPath js4 = ReusableMethods.rawToJson(res4);
		boolean accStatus4 = js4.get("lockedInternally");
		if(!accStatus4) {
			extentTest.log(LogStatus.PASS, "Account is unlocked in CSR Application");
		}else {
			extentTest.log(LogStatus.FAIL, "Account is not unlocked in CSR Application");
		}
	}	
}
