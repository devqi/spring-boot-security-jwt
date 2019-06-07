package org.qizhang.playground.models;

import java.io.Serializable;

/**
 * This class is applied to store the username and password received from the client
 * @author Qi
 *
 */
public class JwtRequest {
	
	private String username;
	private String password;
	
	public JwtRequest() {
		
	}
	
	public JwtRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
