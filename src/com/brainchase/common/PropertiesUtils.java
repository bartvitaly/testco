package com.brainchase.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * This class works with property file
 * 
 * @author vbartashchuk@testco.com
 *
 */

public class PropertiesUtils {

	final static String TEST_PROPERTIES = File.separator + "brainchase.properties";
	final static Logger logger = Logger.getLogger(PropertiesUtils.class);

	/**
	 * This method is to get a variable from properties file
	 * 
	 * @param key
	 * @return String
	 * 
	 */
	public static String get(String key) {

		Properties properties = new Properties();
		String result = "";
		try {
			properties.load(new BufferedReader(new FileReader(Common.canonicalPath() + TEST_PROPERTIES)));
			result = properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug(
				"Property '" + key + "' with value '" + result + "' is retreived from file '" + TEST_PROPERTIES + "'");
		return result;
	}

	/**
	 * This method is to get a variable from properties file
	 * 
	 * @param key
	 * @return Integer
	 * 
	 */
	public static int getInt(String key) {
		return Integer.valueOf(get(key));
	}
}
