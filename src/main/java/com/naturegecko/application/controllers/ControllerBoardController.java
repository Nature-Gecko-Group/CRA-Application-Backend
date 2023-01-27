package com.naturegecko.application.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/controller/")
public class ControllerBoardController {

	@GetMapping("listAllCountries")
	public ResponseEntity<Page<String>> listCountriesInGame() {
		return null;
	}
	
	
}
