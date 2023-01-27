package com.naturegecko.application.forms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterationForm {

	private String username;
	private String userEmail;
	private String userPassword;
	private String userComfirmedPassword;
	private String userDiscordAccount;
}
