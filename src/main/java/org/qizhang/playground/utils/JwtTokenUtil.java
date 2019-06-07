package org.qizhang.playground.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * This util is used to create and validate JWT token
 * @author Qi
 *
 */
@Component
public class JwtTokenUtil {
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
	
	@Value("${jwt.secret}")
	private String secret;
	
	/**
	 * retrieve username from the given jwt token
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	/**
	 * use different functions to the obtained claims
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * The specified secret key is required for retrieving any information from token
	 * @param token
	 * @return
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	/**
	 * retrieve expiration date from the given jwt token
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	/**
	 * check if the given jwt token already expires <br>
	 * true: expired; false: still valid
	 * @param token
	 * @return
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/**
	 * generate jwt token according to the given user details
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return executeGeneration(claims, userDetails.getUsername());
	}
	
	/**
	 * while generating jwt token, <br>
	 * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID <br>
	 * 2. Sign the JWT using the HS512 algorithm and secret key <br>
	 * 3. According to JWS Compact Serialization 
	 *    (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1) 
	 *    compaction of the JWT to a URL-safe string 
	 * @param claims
	 * @param subject username
	 * @return
	 */
	private String executeGeneration(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY) )
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	/**
	 * validate the given jwt token: <br>
	 * - by username <br>
	 * - by expiration 
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
