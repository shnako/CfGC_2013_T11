package com.jpmorgan.thefoodchain.Objects;

public class Account {
	private String username, password;
	
	public Account(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername(){
		return username;
	}
	
	//A token system should be implemented here.
	//Alternatively, a PBKDF2 encryption should be used.
	public String getPassword(){
		return password;
	}
	
	public boolean loadCredentials(){
		//TODO
		return true;
	}
}
