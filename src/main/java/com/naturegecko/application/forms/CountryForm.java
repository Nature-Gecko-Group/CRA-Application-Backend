package com.naturegecko.application.forms;

import lombok.Data;

@Data
public class CountryForm {

	private String id;
	private String name;
	private String precis;
	private String[] authorities;
	private String primaryColor;
	private String secondaryColor;
	private String description;
	private String documentURL;

}
