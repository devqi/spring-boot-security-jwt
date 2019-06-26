package org.qizhang.playground.services;

import java.util.ArrayList;

import org.qizhang.playground.dao.UserDAO;
//import org.qizhang.playground.models.User;
import org.qizhang.playground.models.UserDTO;
import org.qizhang.playground.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if ("qizhang".equals(username)) {
//			return new User("qizhang", "$2a$10$gxn0uPtqKrOOri8xq1xLBOiKQscCcJ/mr2CQlevzuc6etD66VH422",
//					new ArrayList<>());
//		} else {
//			throw new UsernameNotFoundException("User not found with username: " + username);
//		}
		UserEntity user = userDAO.findByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), 
				user.getPassword(), new ArrayList<>());
	}
	
	public UserEntity save(UserDTO userDTO) {
		UserEntity user = new UserEntity();
		user.setUsername(userDTO.getUsername());
		user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
		return userDAO.save(user);
	}

}
