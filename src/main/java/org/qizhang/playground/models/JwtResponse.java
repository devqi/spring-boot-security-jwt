package org.qizhang.playground.models;

import java.io.Serializable;

/**
 * This class is applied to store the generated jwt token that will be returned
 * to users
 * 
 * @author Qi
 *
 */
public class JwtResponse {

	private final String jwtToken;

	public JwtResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getToken() {
		return this.jwtToken;
	}

}
