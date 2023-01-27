package com.naturegecko.application.documents.sub;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("Components")
public class ComponentDocument {

	public enum COMPONENT_TYPE {
		BUILDING, SHIP, MEGA_STRUCTURE
	}

	@Id
	private String id;
	private String title;
	private String description;
	private COMPONENT_TYPE type;

	private String[] sharedTo;

	@Indexed
	private String ownedBy;
}
