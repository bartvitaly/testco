package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.brainchase.common.Common;

/**
 * 
 * This class describes a student entity
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Student extends User {

	public ArrayList<Challenge> challenges = new ArrayList<Challenge>();
//	public ArrayList<String> challengesProgress = new ArrayList<String>();
	public ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
	
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
		this.challenges.add(new Challenge("art"));
//		this.challenges.add(new Challenge("engineering"));
		this.challenges.add(new Challenge("writing"));
		
//		this.challenges = CsvFileReader.readChallengesFile(Common.canonicalPath() + "\\challenges.csv", name);
	}
	
}
