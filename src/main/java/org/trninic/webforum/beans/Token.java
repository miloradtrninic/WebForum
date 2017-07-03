package org.trninic.webforum.beans;

import java.util.ArrayList;
import java.util.Collection;

public class Token {
	private User.RoleEnum role;
	private String username;
	private String token;
	private ArrayList<String> subscribed;
	
	
	
	public Token() {
		super();
	}
	public User.RoleEnum getRole() {
		return role;
	}
	public void setRole(User.RoleEnum role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public ArrayList<String> getSubscribed() {
		return subscribed;
	}
	public void setSubscribed(Collection<String> collection) {
		this.subscribed = (ArrayList<String>) collection;
	}
	public void setSubscribed(ArrayList<String> subscribed) {
		this.subscribed = subscribed;
	}
	
	
}
