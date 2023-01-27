package com.naturegecko.application.services;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("Technology")
public class TechnologyDocument {

	@Id
	private String id;
	private String title;
	private String description;
	private String category;
	
	
}
