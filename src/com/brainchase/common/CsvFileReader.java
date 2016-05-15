package com.brainchase.common;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.brainchase.items.User;

/**
 * @author vbartashchuk@testco.com
 *
 */
public class CsvFileReader {
	
	//CSV file header
    private static final String [] FILE_HEADER_MAPPING = {"username","password","type"};
	
	//Users' attributes
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String TYPE = "type";
	
	public static ArrayList<User> readCsvFile(String fileName, String type) {

		FileReader fileReader = null;
		
		CSVParser csvFileParser = null;
		
		//Create the CSVFormat object with the header mapping
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
     
        ArrayList<User> users = new ArrayList<User>();
        try {
            //initialize FileReader object
            fileReader = new FileReader(fileName);
            
            //initialize CSVParser object
            csvFileParser = new CSVParser(fileReader, csvFileFormat);
            
            //Get a list of CSV file records
            List csvRecords = csvFileParser.getRecords(); 
            
            //Read the CSV file records starting from the second record to skip the header
            for (int i = 1; i < csvRecords.size(); i++) {
            	Object record = csvRecords.get(i);
            	//Create a new student object and fill his data
            	User user = new User(((CSVRecord) record).get(USERNAME), ((CSVRecord) record).get(PASSWORD), ((CSVRecord) record).get(TYPE));
            	if (user.type.equals(type)) {
            		users.add(user);
            	}
			}
            
        } 
        catch (Exception e) {
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
		return users;

	}

}
