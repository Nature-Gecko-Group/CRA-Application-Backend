package com.naturegecko.application.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.naturegecko.application.documents.sub.StatusEnum.COUNTRY_STATUS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("Countries")
public class CountryDocument {

	@Id
	private String id;

	private String name;
	private String precis;
	private String[] authorities;
	private String primaryColor;
	private String secondaryColor;
	private String description;
	private String documentURL;

	private COUNTRY_STATUS status;

	@Indexed
	private String ownedBy;
	
	private String countryFlag;
}
