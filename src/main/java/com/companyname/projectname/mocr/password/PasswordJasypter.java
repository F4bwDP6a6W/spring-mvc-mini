package com.companyname.projectname.mocr.password;

import org.jasypt.util.text.BasicTextEncryptor;


public class PasswordJasypter {

	
	public static String encryption(String input){
		
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("67254");
		
		return textEncryptor.encrypt(input);
	}
	
	public static String decryption(String input){
		
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("67254");
		
		return textEncryptor.decrypt(input);
	}
	
}
