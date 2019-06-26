package org.qizhang.playground.controllers;

import org.qizhang.playground.models.JwtRequest;
import org.qizhang.playground.models.JwtResponse;
import org.qizhang.playground.models.UserDTO;
import org.qizhang.playground.services.JwtUserDetailsService;
import org.qizhang.playground.utils.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is used to expose a POST API /authenticate 
 * which gets and validates the username and password in the request body.
 * If the credentials are valid, a JWT Token is created using JWTTokenUtil 
 * and is provided to the client.
 * @author Qi
 *
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest request) throws Exception {
		
		/* perform authentication */
		authenticate(request.getUsername(), request.getPassword());
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST) 
	public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
		return ResponseEntity.ok(userDetailsService.save(userDTO));
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
