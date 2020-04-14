package com.qa.testcases;

import static io.restassured.RestAssured.given;

import java.util.Arrays;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.base.TestBase;
import com.qa.pojo.UpdateUserProfile;
import com.qa.pojo.UserQuestions;
import com.qa.supportfiles.Resources;
import com.qa.util.ReusableMethods;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UpdateUserProfileTest extends TestBase{
	
	public UpdateUserProfileTest() {
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
		Object data[][] = ReusableMethods.getTestData("Test2");
		return data;
	}
	
	@Test(priority=1, dataProvider="getAPITestData")
	public void updateUserProfile(String scnName, String execute, String logOnId, String pwd, String newLogonID, String newPwd, 
			String email, String mobNumber, String question, String questionAttribute, String answer) throws InterruptedException {
		extentTest = extent.startTest("Update User Profile - " + scnName);
		
		if (execute.equalsIgnoreCase("No")) {
	        throw new SkipException(scnName + " is Skipped");
	    }
		
		
		//To update the profile data
		UserQuestions uQuestion = new UserQuestions();
		uQuestion.setQuestion(question);
		uQuestion.setQuestionAttribute(questionAttribute);
		uQuestion.setAnswer(answer);
		UpdateUserProfile updateUserProfileBody = new UpdateUserProfile();
		updateUserProfileBody.setLogonID(logOnId);
		updateUserProfileBody.setPassword(pwd);
		updateUserProfileBody.setNewlogonID(newLogonID);
		updateUserProfileBody.setNewPassword(newPwd);
		updateUserProfileBody.setEmailAddress(email);
		updateUserProfileBody.setMobilePhone(mobNumber);
		updateUserProfileBody.setUserQuestions(Arrays.asList(uQuestion));
		updateUserProfileBody.setAcceptTerms(true);
		
		
		Response res = 	
		given().
			relaxedHTTPSValidation().and().spec(basereq).body(updateUserProfileBody).and().
		when().
				post(Resources.updateUserProfile()).
		then().
				assertThat().statusCode(204).and().extract().response();
		
		int statusCode = res.getStatusCode();
		if(statusCode==204) {
			extentTest.log(LogStatus.PASS, "Successfully hit the Post request to update user profile data into database");
		}else {
			extentTest.log(LogStatus.FAIL, "Unable to hit the Post request to update user profile data");
		}
		
		
		Thread.sleep(3000);
		
		//To verify the updated data
		Response res2 = 
		given().
			relaxedHTTPSValidation().and().spec(basereq).header("webLogonID", newLogonID).and().
		when().
				get(Resources.updateUserProfile()).
		then().
				assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
				extract().response();
		
		JsonPath js = ReusableMethods.rawToJson(res2);

		if(res2.getStatusCode()==200) {
			extentTest.log(LogStatus.PASS, "Successfully hit the get request to fetch the updated user profile data from database");
		}else {
			extentTest.log(LogStatus.FAIL, "Unable to hit the get request to fetch the updated user profile data");
		}
		
		
		
		String respLogonID=js.get("logonID");
		if(respLogonID.equalsIgnoreCase(newLogonID)) {
			extentTest.log(LogStatus.PASS, "The logonID is updated successfully in database, updated logonID is: " + respLogonID);
		}else {
			extentTest.log(LogStatus.FAIL, "The logonID is not updated successfully");
		}
		
		String respEmailAddress=js.get("emailAddress");
		if(respEmailAddress.equalsIgnoreCase(email)) {
			extentTest.log(LogStatus.PASS, "The emailAddress is updated successfully in database, updated emailAddress is: " + respEmailAddress);
		}else {
			extentTest.log(LogStatus.FAIL, "The emailAddress is not updated successfully");
		}
		String respMobilePhone=js.get("mobilePhone");
		if(respMobilePhone.equalsIgnoreCase(mobNumber)) {
			extentTest.log(LogStatus.PASS, "The mobileNumber is updated successfully in database, updated mobileNumber is: " + respMobilePhone);
		}else {
			extentTest.log(LogStatus.FAIL, "The mobileNumber is not updated successfully");
		}
		
		String rsepQuestion=js.get("userQuestions[0].question");
		if(rsepQuestion.equalsIgnoreCase(question)) {
			extentTest.log(LogStatus.PASS, "The user question is updated successfully in database, updated user question is: " + rsepQuestion);
		}else {
			extentTest.log(LogStatus.FAIL, "The user question is not updated successfully");
		}
		String respQuestionAttribute=js.get("userQuestions[0].questionAttribute");
		if(respQuestionAttribute.equalsIgnoreCase(questionAttribute)) {
			extentTest.log(LogStatus.PASS, "The QuestionAttribute is updated successfully in database, updated QuestionAttribute is: " + respQuestionAttribute);
		}else {
			extentTest.log(LogStatus.FAIL, "The user QuestionAttribute is not updated successfully");
		}
		
	}
}
