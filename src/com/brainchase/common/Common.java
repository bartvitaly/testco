package com.brainchase.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Common {

	final static Logger logger = Logger.getLogger(Common.class);

	/**
	 * This method is to get canonical path
	 * 
	 * @return String
	 * @throws IOException 
	 * 
	 */
	public static String canonicalPath() throws IOException {
		return (new File(".")).getCanonicalPath();
	}
	
	/**
	 * This method is to generate random string
	 * 
	 * @param length
	 * @return String
	 * 
	 */
	public static String randomString(int length) {
		return RandomStringUtils.random(length, "abcdefghijklmnopqrstuvwxyz");
	}

	/**
	 * This method is to generate random string with words count
	 * 
	 * @param length
	 * @return String
	 * 
	 */
	public static String randomStringWordsCount(int numberOfWords) {
		String[] randomStrings = new String[numberOfWords];
		Random random = new Random();
		for (int i = 0; i < numberOfWords; i++) {
			char[] word = new char[random.nextInt(8) + 3];
			for (int j = 0; j < word.length; j++) {
				word[j] = (char) ('a' + random.nextInt(26));
			}
			randomStrings[i] = new String(word);
		}
		return arrayToString(randomStrings);
	}

	/**
	 * This method is to convert an array to string
	 * 
	 * @param arr
	 * @return String
	 * 
	 */
	public static String arrayToString(String[] arr) {
		StringBuilder builder = new StringBuilder();
		for (String s : arr) {
			builder.append(s + " ");
		}
		return builder.toString();
	}

	/**
	 * This method is to extract numbers from a List
	 * 
	 * @param text
	 * @return ArrayList
	 * 
	 */
	public static ArrayList<String> extractNumbers(String text) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(text);
		ArrayList<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group());
		}
		return result;
	}

	/**
	 * This method is to get a current timestamp
	 * 
	 * @return String
	 * 
	 */
	public static String timestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		long time = date.getTime();
		return String.valueOf(time);
	}

	/**
	 * This method is to read from file
	 * 
	 * @param path
	 * @param encoding
	 * @return String
	 * 
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	/**
	 * This method is to write to file
	 * 
	 * @param path
	 * @param text
	 * @throws IOException
	 * 
	 */
	public static void writeToFile(String path, String text) throws IOException {
		 Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
			writer.write(text);
//			Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException ex) {
//			logger.info("Can't write to a file '" + path + "', trying again...");
//			Files.write(Paths.get(path), System.getProperty("line.separator").getBytes(), StandardOpenOption.APPEND);
//			Files.write(Paths.get(path), text.getBytes(), StandardOpenOption.APPEND);
		} finally {
			try {
				 writer.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * This method is used to write transaction ids to file
	 * 
	 * @param user
	 * @return DashboardPage
	 * @throws IOException
	 */
	public static void writeTransactions(String path, ArrayList<ArrayList<String>> transactions) throws IOException {
		String toPrint = "student_name,challenge_type,transaction_id";
		ArrayList<String> transaction = new ArrayList<>();
		
		for (int j = 0; j < transactions.size(); j++) {
			transaction = transactions.get(j);
			toPrint = toPrint + "\r\n";
			for (int j2 = 0; j2 < transaction.size(); j2++) {					
				toPrint = toPrint + transaction.get(j2) + ",";
			}
		}
		Common.writeToFile(path, toPrint);
	}
	
}
