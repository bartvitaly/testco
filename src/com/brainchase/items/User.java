package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class User {

	public String name, password, type;
	
	/**
	 * This is constructor of the class
	 * 
	 * @param name
	 * @param password
	 * 
	 */
	public User(String name, String password, String type) {
		this.name = name;
		this.password = password;
		this.type = type;
	}

	public static User[][] getUsers(ArrayList<User> users, int size) throws IOException {
		User[][] dataProviderArray = new User[size][1];

		int usersIterator = 0;
		for (int dataProviderIterator = 0; dataProviderIterator < size; dataProviderIterator++) {
			if (usersIterator == users.size()) {
				usersIterator = 0;
			}
			dataProviderArray[dataProviderIterator][0] = users.get(usersIterator);
			usersIterator++;
		}
		
		return dataProviderArray;
	}
	
	
}
