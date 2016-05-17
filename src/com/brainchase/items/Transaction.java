package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.brainchase.common.Common;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Transaction {

	String studentName, challengeType, transactionid;
	
	/**
	 * This is constructor of the class
	 * 
	 */
	public Transaction(String studentName, String challengeType, String transactionid) {
		this.studentName = studentName;
		this.challengeType = challengeType;
		this.transactionid = transactionid;
	}	
	
	/**
	 * This method is to write students' transactions to a file
	 * 
	 * @param students
	 * @throws IOException
	 */
	public static void writeTransactions(ArrayList<Student> students) throws IOException {
		ArrayList<ArrayList<String>> transactionsLocal = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < students.size(); i++) {
			transactionsLocal.addAll((students.get(i).transactions));
		}
		Common.writeTransactions(Common.canonicalPath() + "\\transactionsStudent.csv", transactionsLocal);
	}	
	
	/**
	 * This method is to write teacher's transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeTeacherTransactions(ArrayList<ArrayList<String>> transactions) throws IOException {
		Common.writeTransactions(Common.canonicalPath() + "\\transactionsTeacher.csv", transactions);
	}
	
	/**
	 * This method is to write supervisors transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeSupervisorTransactions(ArrayList<ArrayList<String>> transactions) throws IOException {
		Common.writeTransactions(Common.canonicalPath() + "\\transactionsSupervisor.csv", transactions);
	}
	
	/**
	 * This method is to create transactions mock
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static ArrayList<ArrayList<String>> mock(String studentName, String challengeType, String transactionId) {
		ArrayList<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
		ArrayList<String> transaction = new ArrayList<String>();
		
		transaction.add(studentName);
		transaction.add(challengeType);
		transaction.add(transactionId);
		
		transactions.add(transaction);
		
		return transactions;
	}
	
}
