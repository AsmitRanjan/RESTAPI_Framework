package com.qa.pojo;

import java.util.List;

public class LockUser {
	private String logonID;
	private List<UserQuestions> userQuestions;
	private boolean impactAuthentication;
	
	public String getLogonID() {
		return logonID;
	}

	public void setLogonID(String logonID) {
		this.logonID = logonID;
	}
	
	public List<UserQuestions> getUserQuestions() {
		return userQuestions;
	}

	public void setUserQuestions(List<UserQuestions> userQuestions) {
		this.userQuestions = userQuestions;
	}
	public boolean isImpactAuthentication() {
		return impactAuthentication;
	}

	public void setImpactAuthentication(boolean impactAuthentication) {
		this.impactAuthentication = impactAuthentication;
	}

	
		
}
