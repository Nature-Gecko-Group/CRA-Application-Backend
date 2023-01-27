package com.naturegecko.application.controllers;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.naturegecko.application.forms.UserAuthenticationForm;
import com.naturegecko.application.forms.UserRegisterationForm;
import com.naturegecko.application.services.UserAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/authentication/")
public class AuthenticationController {

	private final String uri = "api/authentication/";

	@Autowired
	private UserAuthenticationService userAuthenticationService;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	@PostMapping("register")
	public ResponseEntity<String> register(@RequestBody UserRegisterationForm form) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(this.uri + "register").toString());
		return ResponseEntity.created(uri).body(userAuthenticationService.createNewUser(form));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	@PostMapping("login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserAuthenticationForm form,
			HttpServletResponse response) {
		return ResponseEntity.ok().body(userAuthenticationService.authenticate(form, response));
	}
	
	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
		// V 1.0 OK!
	@GetMapping("signout")
	public ResponseEntity<String> signOut(HttpServletResponse response){
		return ResponseEntity.ok().body(userAuthenticationService.signOut(response));
	}

}
