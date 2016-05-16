package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.brainchase.common.CsvFileReader;

/**
 * 
 * This class describes a student entity
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Student extends User {

	public ArrayList<Challenge> challenges = new ArrayList<Challenge>();
	
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
		this.challenges = CsvFileReader.readChallengesFile((new File(".")).getCanonicalPath() + "\\challenges.csv", name);
	}
	
}
