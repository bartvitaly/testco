package com.brainchase.items;

import com.brainchase.common.Common;

/**
 * 
 * This class defines Challenge object
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Challenge {

	public String type, externalLink, response, image, answer;
	
	/**
	 * This is a constructor of the class
	 * 
	 * @param type
	 * @param externalLink
	 * @param response
	 * @param image
	 * @param answer
	 * 
	 */
	public Challenge(String type, String externalLink, String response, String image, String answer) {
		this.type = type;
		this.externalLink = externalLink;
		this.response = response;
		this.image = image;
		this.answer = answer;
	}	
	
	/**
	 * This is a constructor of the class
	 * 
	 * @param type
	 * 
	 */
	public Challenge(String type) {
		this.type = type;
		this.externalLink = "https://app.brainchase.com/sites/all/themes/brainchase/images/bc_onlinelogo.png";
		this.response = Common.randomStringWordsCount(200);
		this.image = null;
		this.answer = Common.randomString(20);
	}	
	
	
	
}
