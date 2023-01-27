package com.naturegecko.application.forms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthenticationForm {

	private String username;
	private String password;

}
