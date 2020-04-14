package com.qa.pojo;

import java.util.List;

public class UpdateUserProfile {
	private String logonID;
	private String password;
	private String newlogonID;
	private String newPassword;
	private String emailAddress;
	private String mobilePhone;
	private List<UserQuestions> userQuestions;
	private boolean acceptTerms;

	
	public String getLogonID() {
		return logonID;
	}
	public void setLogonID(String logonID) {
		this.logonID = logonID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNewlogonID() {
		return newlogonID;
	}
	public void setNewlogonID(String newlogonID) {
		this.newlogonID = newlogonID;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public List<UserQuestions> getUserQuestions() {
		return userQuestions;
	}
	public void setUserQuestions(List<UserQuestions> userQuestions) {
		this.userQuestions = userQuestions;
	}
	public boolean isAcceptTerms() {
		return acceptTerms;
	}
	public void setAcceptTerms(boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}
	
}
