package com.naturegecko.application.documents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@Document("UserAccounts")
public class UserAccountsDocument implements UserDetails {
	@Id
	private String id;

	@Indexed(unique = true)
	private String username;
	@Indexed(unique = true)
	private String userEmail;
	private String userDiscordName;

	@JsonIgnore
	private String userPassword;

	private String[] profileImage;
	private String[] roles;

	private LocalDateTime creationDate;

	private boolean isSuspended;

	public UserAccountsDocument(String username, String userEmail, String userPassword, String[] profileImage,
			String[] roles, LocalDateTime creationDate) {
		super();
		this.username = username;
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		this.profileImage = profileImage;
		this.roles = roles;
		this.creationDate = creationDate;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Arrays.stream(roles).forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role));
		});
		return authorities;
	}

	@Override
	public String getPassword() {
		return userPassword;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !isSuspended;
	}

}
