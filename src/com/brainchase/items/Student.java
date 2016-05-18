package com.brainchase.items;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * This class describes a student entity
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Student extends User {

	public HashMap<String, Challenge> challenges = new HashMap<String, Challenge>();
	
	/**
	 * This is constructor of the class
	 * 
	 * @param name
	 * @param password
	 * @param type
	 * @throws IOException 
	 * 
	 */
	public Student(String name, String password, String type) throws IOException {
		super(name, password, type);
		for (Map.Entry<String, HashMap<String, String>> entry : super.getTransactions().entrySet())
		{
			this.challenges.put(entry.getKey(), new Challenge(entry.getKey()));
		}
//		this.challenges.add(new Challenge("writing"));
		
//		this.challenges = CsvFileReader.readChallengesFile(Common.canonicalPath() + "\\challenges.csv", name);
	}
	
}
