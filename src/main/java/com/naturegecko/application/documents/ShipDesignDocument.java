package com.naturegecko.application.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("ShipDesign")
public class ShipDesignDocument {

	public enum SHIP_TYPE {
		SCIENCE, EXPLORATION_CRUISER, CORVETTE, DESTROYER, CRUISER, BATTLESHIP, DREADNOUGHT, HEADQUARTER;
	}

	@Id
	private String id;

	private String title;
	private String description;
	private SHIP_TYPE category;

	private String[] images;
	private String[] sharedTo;
	private String[] components;

	@Indexed
	private String ownedBy;
}
