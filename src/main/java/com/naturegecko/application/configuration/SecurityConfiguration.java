package com.naturegecko.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.naturegecko.application.security.JwtAuthenticationFIlter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	@Autowired
	private JwtAuthenticationFIlter authenticationFIlter;
	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();

		http.authorizeHttpRequests().requestMatchers("/api/authentication/**").permitAll();
		http.authorizeHttpRequests().requestMatchers("/api/websocket/**").permitAll();
		http.authorizeHttpRequests().requestMatchers("/api/player/**").hasAnyAuthority("player");
		http.authorizeHttpRequests().requestMatchers("/api/controller/**").hasAuthority("staff");
		http.authorizeHttpRequests().anyRequest().authenticated();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authenticationProvider(authenticationProvider).addFilterBefore(authenticationFIlter,
				UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
