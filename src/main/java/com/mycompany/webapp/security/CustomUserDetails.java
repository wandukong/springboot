package com.mycompany.webapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUserDetails extends User{
	private String memail;
	
	public CustomUserDetails(String username, String password, Boolean enabled, Collection<? extends GrantedAuthority> authorities, String memail) {
		super(username, password, enabled, true, true, true, authorities);
		this.memail = memail;
	}

	public String getMemail() {
		return memail;
	}
}
