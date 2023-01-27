package com.naturegecko.application.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.naturegecko.application.documents.CountryDocument;
import com.naturegecko.application.documents.UserAccountsDocument;
import com.naturegecko.application.forms.CountryForm;
import com.naturegecko.application.services.CountryService;
import com.naturegecko.application.services.UserAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/player/")
public class BoardPlayerController {

	private final String uri = "api/player/";

	@Autowired
	private UserAuthenticationService userAuthenticationService;
	@Autowired
	private CountryService countryService;

	@GetMapping("myProfile")
	public ResponseEntity<UserAccountsDocument> getMyProfile(HttpServletRequest request) {
		return ResponseEntity.ok().body(userAuthenticationService.getUserFromToken(request));
	}

	@GetMapping("")
	public ResponseEntity<String> test() {
		return ResponseEntity.ok().body("HI!");
	}

	@GetMapping("myCountries")
	public ResponseEntity<List<String>> getMyCountries() {
		return null;
	}

	@GetMapping("country/status")
	public ResponseEntity<List<String>> getAllAvailableStatus() {
		return ResponseEntity.ok().body(countryService.getAvailableStatus());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Create new country
	@PostMapping("country")
	public ResponseEntity<CountryDocument> createNewCountry(@RequestBody CountryForm form, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(this.uri + "country").toString());
		return ResponseEntity.created(uri).body(countryService.createNewCountry(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// V 1.0 OK!
	// Edit country
	@PutMapping("country")
	public ResponseEntity<CountryDocument> editCountry(@RequestBody CountryForm form, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(this.uri + "country/" + form.getId()).toString());
		return ResponseEntity.created(uri).body(countryService.editCountry(form, request));
	}

}
