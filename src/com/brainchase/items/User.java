package com.brainchase.items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * This class describes a user entity
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class User {

	public String name, password, type;
	private HashMap<String, HashMap<String, String>> transactions = Transaction.getTransactionsHashMap();
	
	/**
	 * This is constructor of the class
	 * 
	 * @param name
	 * @param password
	 * @param type
	 * 
	 */
	public User(String name, String password, String type) {
		this.name = name;
		this.password = password;
		this.type = type;
	}

	/**
	 * This method is to create data type readable for TestNG data provider
	 * 
	 * @param users
	 * @param size
	 * 
	 */
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

	public HashMap<String, HashMap<String, String>> getTransactions() {
		return transactions;
	}

	public void setTransactions(HashMap<String, HashMap<String, String>> transactions) {
		this.transactions = transactions;
	}
	
	
}
