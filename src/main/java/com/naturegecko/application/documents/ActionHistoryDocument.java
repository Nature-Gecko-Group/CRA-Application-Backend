package com.naturegecko.application.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.naturegecko.application.documents.sub.StatusEnum.ACTION_STATUS;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document("ActionHistory")
public class ActionHistoryDocument {

	@Id
	private String id;
	private String title;
	private String description;
	private String from;
	private String[] to;
	private int turn;
	private LocalDateTime date;

	private ACTION_STATUS status;

	private boolean isSecret;

}
