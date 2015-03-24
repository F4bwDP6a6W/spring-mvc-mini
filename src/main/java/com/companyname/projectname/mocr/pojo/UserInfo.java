package com.companyname.projectname.mocr.pojo;

import javax.validation.constraints.NotNull;

import com.companyname.projectname.mocr.password.PasswordJasypter;

public class UserInfo {
	@NotNull
	String username;
	@NotNull
	String email;
	@NotNull
	String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		
		return PasswordJasypter.decryption(this.password);
	}
	public void setPassword(String password) {
		this.password = PasswordJasypter.encryption(password);
	}
	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", email=" + email
				+ ", password=******]";
	}
}
