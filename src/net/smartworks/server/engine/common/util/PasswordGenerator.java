/*	
 * $Id$
 * created by    : hsshin
 * creation-date : 2012. 5. 8.
 * =========================================================
 * Copyright (c) 2012 ManinSoft, Inc. All rights reserved.
 */

package net.smartworks.server.engine.common.util;

import java.util.Random;

public class PasswordGenerator {

private int length;
private boolean containsNumber;

	public PasswordGenerator(int length) {
		this.length = length;
		containsNumber= true;
	}

	public PasswordGenerator(int length, boolean number) {
		this.length = length;
		this.containsNumber = number;
	}

	private String getCharacterMap() {
		String characterMap = "abcdefghijklmnopqrstuvwxyz";
		if(containsNumber) characterMap += "0123456789";
		return characterMap;
	}

	public String getNewPassword() {
		Random randomNumber = new Random();
		String characterMap = getCharacterMap();
		String password = "";
		for(int i=0; i<length; i++) {
			password += characterMap.charAt(Math.abs(randomNumber.nextInt() % characterMap.length()));
		}
		return password;
	}
}