package com.naturegecko.application.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.naturegecko.application.utilities.JWTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFIlter extends OncePerRequestFilter {

	private final JWTokenService tokenService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		String authenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authenHeader == null || !authenHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = authenHeader.substring(7);
		String username = tokenService.getUsernameFromToken(accessToken);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (tokenService.isTokenValid(accessToken, userDetails)) {
				UsernamePasswordAuthenticationToken autheToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				autheToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(autheToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
