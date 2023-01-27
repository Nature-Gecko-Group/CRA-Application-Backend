package com.naturegecko.application.documents.sub;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class StarSystemBuilding {

	@Id
	private String id;
	private String title;
	private String type;
	
	
}
