package com.naturegecko.application.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naturegecko.application.documents.UserAccountsDocument;
import com.naturegecko.application.exception.ExceptionFoundation;
import com.naturegecko.application.exception.ExceptionResponseModel.EXCEPTION_CODE;
import com.naturegecko.application.forms.UserAuthenticationForm;
import com.naturegecko.application.forms.UserRegisterationForm;
import com.naturegecko.application.repositories.UserAccountsRepository;
import com.naturegecko.application.utilities.JWTokenService;
import com.naturegecko.application.utilities.StringValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserAuthenticationService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTokenService tokenService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserAccountsRepository userAccountsRepository;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// User creation.
	// V 1.0 OK!
	public String createNewUser(UserRegisterationForm form) {

		// Check if password is OK to go.
		if (!StringValidationService.validatePassword(form.getUserPassword())) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_INVALID_PASSWORD_FORMAT,
					HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_INVALID_PASSWORD_FORMAT ] Invalid password format. The password must be longer than 8 characters.");
		} else if (!form.getUserPassword().equals(form.getUserComfirmedPassword())) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_PASSWORD_MISMATCH, HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_PASSWORD_MISMATCH ] The password doesn't match.");
		}

		// Check if username exist and is valid.
		Optional<UserAccountsDocument> checkAccountUsername = userAccountsRepository.findByUsername(form.getUsername());
		if (!checkAccountUsername.isEmpty()) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_TAKEN_USERNAME, HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_TAKEN_USERNAME ] This username is taken by another user.");
		} else if (!StringValidationService.validateUsername(form.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_INVALID_USERNAME_FORMAT,
					HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_INVALID_USERNAME_FORMAT ] Username must only contain A-Z or a-z or 0-9.");
		}

		// Check if email exist and is valid.
		Optional<UserAccountsDocument> checkAccountEmail = userAccountsRepository.findByEmail(form.getUserEmail());
		if (!checkAccountEmail.isEmpty()) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_TAKEN_EMAIL, HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_TAKEN_EMAIL ] This email is taken by another user.");
		} else if (!StringValidationService.validateEmail(form.getUserEmail())) {
			throw new ExceptionFoundation(EXCEPTION_CODE.REGISTERATION_INVALID_EMAIL_FORMAT, HttpStatus.I_AM_A_TEAPOT,
					"[ REGISTERATION_INVALID_EMAIL_FORMAT ] Email must be in a right format.");
		}

		// Create new user account.
		UserAccountsDocument newUser = new UserAccountsDocument();
		newUser.setUsername(form.getUsername());
		newUser.setUserEmail(form.getUserEmail());
		newUser.setUserDiscordName(form.getUserDiscordAccount() == null ? "" : form.getUserDiscordAccount());
		newUser.setUserPassword(passwordEncoder.encode(form.getUserPassword()));

		String[] defaultProfileImage = new String[1];
		defaultProfileImage[0] = "userprofile/_Default-Userprofile.png";
		newUser.setProfileImage(defaultProfileImage);

		String[] defaultRoles = new String[1];
		defaultRoles[0] = "player";
		newUser.setRoles(defaultRoles);
		newUser.setCreationDate(LocalDateTime.now());

		newUser.setSuspended(false);

		userAccountsRepository.insert(newUser);

		return newUser.getUsername();
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// User authentication with token.
	// V 1.0 OK!
	public Map<String, Object> authenticate(UserAuthenticationForm form, HttpServletResponse response) {
		Optional<UserAccountsDocument> authenticatingUser = userAccountsRepository.findByUsername(form.getUsername());

		if (authenticatingUser.isEmpty()
				|| !passwordEncoder.matches(form.getPassword(), authenticatingUser.get().getUserPassword())) {
			throw new ExceptionFoundation(EXCEPTION_CODE.AUTHEN_INCORRECT_CREDENTIALS, HttpStatus.UNAUTHORIZED,
					"[ AUTHEN_INCORRECT_CREDENTIALS ] Incorrect username or password.");
		} else if (!authenticatingUser.get().isAccountNonLocked()) {
			throw new ExceptionFoundation(EXCEPTION_CODE.AUTHEN_ACCOUNT_SUSPENDED, HttpStatus.UNAUTHORIZED,
					"[ AUTHEN_ACCOUNT_SUSPENDED ] This account is suspended. Please contact the staff of this roleplay.");
		}

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));

		// This authentication function can have more than one role.
		Map<String, Object> extraClaimMap = new HashMap<>();
		extraClaimMap.put("roles", authenticatingUser.get().getRoles());
		String accessToken = tokenService.createToken(authenticatingUser.get().getUsername(), extraClaimMap);
		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

		authenticatingUser.get().setUserPassword(null);

		Map<String, Object> result = new HashMap<>();
		result.put("useraccount", authenticatingUser.get());
		result.put("accessToken", accessToken);
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Sign out
	// V 1.0 OK!
	public String signOut(HttpServletResponse response) {
		response.setHeader(HttpHeaders.AUTHORIZATION, null);
		return "OK";
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Get user profile from token
	// V 1.0 OK!
	public UserAccountsDocument getUserFromToken(HttpServletRequest request) {
		Optional<UserAccountsDocument> user = userAccountsRepository
				.findByUsername(tokenService.getUsernameFromToken(tokenService.getTokenFromRequest(request)));
		if (user.isEmpty()) {
			throw new ExceptionFoundation(EXCEPTION_CODE.USER_DOES_NOT_EXIST, HttpStatus.NOT_FOUND,
					"[ USER_DOES_NOT_EXIST ] This user does not exist");
		}

		user.get().setUserPassword(null);
		return user.get();
	}

}
