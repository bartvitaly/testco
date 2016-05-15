package com.brainchase.common;

import java.io.BufferedWriter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Common {

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
	 * 
	 */
	public static void writeToFile(String path, String text) {
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"));
			writer.write(text);
		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

}
