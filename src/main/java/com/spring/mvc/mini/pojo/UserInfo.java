package com.spring.mvc.mini.pojo;

import javax.validation.constraints.NotNull;

import com.spring.mvc.mini.password.PasswordJasypter;

public class UserInfo {
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;

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
