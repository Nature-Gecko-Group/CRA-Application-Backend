package com.naturegecko.application.utilities;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.naturegecko.application.documents.UserAccountsDocument;
import com.naturegecko.application.exception.ExceptionFoundation;
import com.naturegecko.application.exception.ExceptionResponseModel.EXCEPTION_CODE;
import com.naturegecko.application.repositories.UserAccountsRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JWTokenService {

	private static final String SECRET_KEY = "A0fzwr2zfklJ1nnapakMyL0ve1y2001dfgry4xcf5rycf";
	private static String issuedBy = "NatureGeckoGroup";
	private static int expireInMili = 43200000;

	@Autowired
	private UserAccountsRepository userAccountsRepository;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	public Claims getAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBype = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBype);
	}

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	public String getUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public UserAccountsDocument getUserAccountFromToken(String token) {
		return userAccountsRepository.findByUsername(getUsernameFromToken(token))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODE.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR,
						"[ INTERNAL_SERVER_ERROR ] The username does not exist in this token."));
	}

	public UserAccountsDocument getUserAccountFromRequest(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
		return userAccountsRepository.findByUsername(getUsernameFromToken(token))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODE.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR,
						"[ INTERNAL_SERVER_ERROR ] The username does not exist in this token."));

	}

	public String createToken(UserDetails userDetails, Map<String, Object> extraClaims) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expireInMili)).setIssuer(issuedBy)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public String createToken(String username, Map<String, Object> extraClaims) {
		return Jwts.builder().setClaims(extraClaims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expireInMili)).setIssuer(issuedBy)
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	public String createToken(UserDetails userDetails) {
		return createToken(userDetails, new HashMap<>());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!

	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isExpired(token));
	}

	public boolean isExpired(String token) {
		return getExpirationDate(token).before(new Date());
	}

	public Date getExpirationDate(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	public String getTokenFromRequest(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		return token.substring(7);
	}
}
