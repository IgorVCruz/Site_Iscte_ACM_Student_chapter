package com.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.utils.EmailSender;

@ManagedBean
@RequestScoped
public class BecameMemberBean {
	
	private String name;
	private String email;
	private String aboutYourself;
	private String errorMessage;
	
	public String submit() {
		
		String content ="Eu " + name + ", quero-me juntar � ACM" + "em seguida, est� algo acerca de mim:\n" + aboutYourself;
		
		EmailSender.getInstance().sendTextEmail("car31dias321@gmail.com", "Want to join acm", content);
		
		
		return "";
	}

	
	
	
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	
	
	
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
	
	
	

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	
	
	
	
	
	/**
	 * @return the aboutYouself
	 */
	public String getAboutYourself() {
		return aboutYourself;
	}
	
	
	
	
	
	
	/**
	 * @param aboutYouself the aboutYouself to set
	 */
	public void setAboutYourself(String aboutYouself) {
		this.aboutYourself = aboutYouself;
	}

	
	
	
	
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	
	
	
	
	

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	
	
	
	
	

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



}
