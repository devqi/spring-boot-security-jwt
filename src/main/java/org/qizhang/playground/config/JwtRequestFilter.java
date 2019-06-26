package org.qizhang.playground.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qizhang.playground.services.JwtUserDetailsService;
import org.qizhang.playground.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter class. 
 * For any incoming request, this Filter class gets executed. 
 * It checks if the request has a valid JWT token. 
 * If it has a valid JWT Token then it sets the Authentication in the context, 
 * to specify that the current user is authenticated.
 * 
 * @author Qi
 *
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String tokenInHeader = request.getHeader("Authorization");
		
		String username = null;
		String jwtToken = null;
		
		/*
		 * A jwt token in header is in the form "Bearer jwtToken". Next step is to
		 * remove "Bearer " to get a pure token
		 */		 
		if(tokenInHeader != null && tokenInHeader.startsWith("Bearer ")) {
			jwtToken = tokenInHeader.substring(7);
			username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			
		}
		else {
			log.warn("JWT token does not start with 'Bearer '");
		}
		
		/* validate token when it is obtained from header correctly */
//		if(username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
		if(username != null ) {
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
			
			/* if token is valid, then configure Spring Security to manually set authentication */
			if(jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				/*
				 * After setting the Authentication in the context, we specify that the current
				 * user is authenticated. So it passes the Spring Security Configurations
				 * successfully.
				 */
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
