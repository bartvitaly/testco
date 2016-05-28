package com.brainchase.common;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.brainchase.items.Challenge;
import com.brainchase.items.User;

/**
 * @author vbartashchuk@testco.com
 *
 */
public class CsvFileReader {

	// CSV file header
	private static final String[] USERS_HEADER_MAPPING = { "login", "username", "password", "type" };
	public static final String[] CHALLENGES_HEADER_MAPPING = { "art", "bonus", "engineering", "math", "reading",
			"writing" };
	public static final String[] CHALLENGES_ACTUAL = { "art", "engineering", "writing" };

	// Common attributes
	private static final String LOGIN = "login";
	private static final String USERNAME = "username";

	// Users' attributes
	private static final String PASSWORD = "password";
	private static final String TYPE = "type";

	// Challenges' attributes
	private static final String ART = "art";
	private static final String BONUS = "bonus";
	private static final String ENGINEERING = "engineering";
	private static final String MATH = "math";
	private static final String READING = "reading";
	private static final String WRITING = "writing";

	public static ArrayList<ArrayList<User>> readUsersFile(String fileName, String type) {

		FileReader fileReader = null;

		CSVParser csvFileParser = null;

		// Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(USERS_HEADER_MAPPING);

		ArrayList<User> users = new ArrayList<User>();
		ArrayList<User> allUsers = new ArrayList<User>();
		try {
			// initialize FileReader object
			fileReader = new FileReader(fileName);

			// initialize CSVParser object
			csvFileParser = new CSVParser(fileReader, csvFileFormat);

			// Get a list of CSV file records
			List csvRecords = csvFileParser.getRecords();

			// Read the CSV file records starting from the second record to skip
			// the header
			for (int i = 1; i < csvRecords.size(); i++) {
				Object record = csvRecords.get(i);
				// Create a new student object and fill his data
				User user = new User(((CSVRecord) record).get(LOGIN), ((CSVRecord) record).get(USERNAME),
						((CSVRecord) record).get(PASSWORD), ((CSVRecord) record).get(TYPE));
				if (user.type.equals(type)) {
					users.add(user);
				}
				allUsers.add(user);
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				csvFileParser.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader/csvFileParser !!!");
				e.printStackTrace();
			}
		}
		ArrayList<ArrayList<User>> usersToReturn = new ArrayList<ArrayList<User>>();
		usersToReturn.add(users);
		usersToReturn.add(allUsers);
		return usersToReturn;

	}

	public static ArrayList<Challenge> readChallengesFile(String fileName) {

		FileReader fileReader = null;

		CSVParser csvFileParser = null;

		// Create the CSVFormat object with the header mapping
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(CHALLENGES_HEADER_MAPPING);

		ArrayList<Challenge> challenges = new ArrayList<Challenge>();
		try {
			// initialize FileReader object
			fileReader = new FileReader(fileName);

			// initialize CSVParser object
			csvFileParser = new CSVParser(fileReader, csvFileFormat);

			// Get a list of CSV file records
			List csvRecords = csvFileParser.getRecords();

			// Read the CSV file records starting from the second record to skip
			// the header
			for (int i = 1; i < csvRecords.size(); i++) {
				Object record = csvRecords.get(i);
				if (((CSVRecord) record).get(ART).equals("1")) {
					challenges.add(new Challenge("art"));
				}
				if (((CSVRecord) record).get(BONUS).equals("1")) {
					challenges.add(new Challenge("bonus"));
				}
				if (((CSVRecord) record).get(ENGINEERING).equals("1")) {
					challenges.add(new Challenge("engineering"));
				}
				if (((CSVRecord) record).get(MATH).equals("1")) {
					challenges.add(new Challenge("math"));
				}
				if (((CSVRecord) record).get(READING).equals("1")) {
					challenges.add(new Challenge("reading"));
				}
				if (((CSVRecord) record).get(WRITING).equals("1")) {
					challenges.add(new Challenge("writing"));
				}
			}

		} catch (Exception e) {
			System.out.println("Error in CsvFileReader !!!");
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
				csvFileParser.close();
			} catch (IOException e) {
				System.out.println("Error while closing fileReader/csvFileParser !!!");
				e.printStackTrace();
			}
		}
		return challenges;

	}

}
