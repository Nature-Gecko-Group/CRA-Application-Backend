package com.naturegecko.application.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("province")
public class StarSystemDocument {
	@Id
	private String id;

	private String countryId;
}
