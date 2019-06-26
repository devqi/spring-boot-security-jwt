package org.qizhang.playground.models;

/**
 * This class is responsible for getting values from user and 
 * passing it to the DAO layer for inserting data into database.
 *  
 * @author Qi
 *
 */
public class UserDTO {
	
	private String username;
	private String password;
	
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
