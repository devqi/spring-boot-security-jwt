package org.qizhang.playground.services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service implements the Spring Security UserDetailsService interface. 
 * It overrides the loadUserByUsername for fetching user details from the database 
 * using the username. The Spring Security Authentication Manager calls this method 
 * for getting the user details from the database when authenticating the user details 
 * provided by the user. 
 * 
 * @author Qi
 *
 */

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("qizhang".equals(username)) {
			return new User("qizhang", "$2a$10$gxn0uPtqKrOOri8xq1xLBOiKQscCcJ/mr2CQlevzuc6etD66VH422",
					new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}
