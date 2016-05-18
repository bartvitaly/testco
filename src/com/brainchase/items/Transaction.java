package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;

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
	 * This method is to create transactions HashMap
	 * 
	 * @param transactions
	 */
	public static HashMap<String, HashMap<String, String>> getTransactionsHashMap() {
		HashMap<String, HashMap<String, String>> transactions = new HashMap<String, HashMap<String, String>>();
		ArrayList<Challenge> challenges = CsvFileReader.readChallengesFile(Common.canonicalPath() + "\\challenges.csv");
		for (int i = 0; i < challenges.size(); i++) {
			transactions.put(challenges.get(i).type, new HashMap<String, String>());
		}

		return transactions;
	}

	/**
	 * This method is to write students' transactions to a file
	 * 
	 * @param students
	 * @throws IOException
	 */
	public static void writeTransactions(ArrayList<Student> students) throws IOException {
		HashMap<String, HashMap<String, String>> transactionsLocal = new HashMap<String, HashMap<String, String>>();

		for (int i = 0; i < students.size(); i++) {
			transactionsLocal.putAll((students.get(i).getTransactions()));
		}
		writeTransactions(Common.canonicalPath() + "\\transactionsStudent.csv", transactionsLocal);
	}

	/**
	 * This method is to write teacher's transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeTeacherTransactions(HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		writeTransactions(Common.canonicalPath() + "\\transactionsTeacher.csv", transactions);
	}

	/**
	 * This method is to write supervisors transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeSupervisorTransactions(HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		writeTransactions(Common.canonicalPath() + "\\transactionsSupervisor.csv", transactions);
	}

	/**
	 * This method is to create transactions mock
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static HashMap<String, HashMap<String, String>> mock(String studentName, String challengeType,
			String transactionId) {
		HashMap<String, HashMap<String, String>> transactions = new HashMap<String, HashMap<String, String>>();

		transactions.get(challengeType).put(studentName, transactionId);

		return transactions;
	}

	public static void compareTransactions(ArrayList<HashMap<String, HashMap<String, String>>> transactions,
			String path) throws IOException {

		String transaction_id3 = "";
		if (transactions.size() > 2) {
			transaction_id3 = ",transaction_id3";
		}
		String toPrint = "student_name,challenge_type,transaction_id1,transaction_id2" + transaction_id3
				+ ",comparison_result";

		// Iterate over challenges' types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactions.get(0).entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			toPrint = toPrint + "\r\n";
			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();

				// Comparison
				Boolean result = false;

				String transactionId1 = students.getValue();
				String transactionId2 = transactions.get(1).get(types.getKey()).get(students.getKey());
				if (transactions.size() > 2) {
					String transactionId3 = transactions.get(2).get(types.getKey()).get(students.getKey());
					if (transactionId1.equals(transactionId2) && transactionId1.equals(transactionId2)) {
						result = true;
					}
					toPrint = toPrint + types.getKey() + "," + students.getKey() + "," + transactionId1 + ","
							+ transactionId2 + "," + transactionId3 + "," + result.toString();
				} else {
					toPrint = "student_name,challenge_type,transaction_id1,transaction_id2,comparison_result";
					if (transactionId1.equals(transactionId2)) {
						result = true;
					}
					toPrint = toPrint + types.getKey() + "," + students.getKey() + "," + transactionId1 + ","
							+ transactionId2 + "," + result.toString();

				}
				itStudents.remove();
			}
			itTypes.remove();
		}

		Common.writeToFile(path, toPrint);
	}

	/**
	 * This method is used to write transaction ids to file
	 * 
	 * @param user
	 * @return DashboardPage
	 * @throws IOException
	 */
	public static void writeTransactions(String path, HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		Common.writeToFile(path, transactionsToString(transactions));
	}

	/**
	 * This method is used to write transaction ids to file
	 * 
	 * @param user
	 * @return DashboardPage
	 */
	public static String transactionsToString(HashMap<String, HashMap<String, String>> transactions) {
		String toPrint = "student_name,challenge_type,transaction_id";

		// Iterate over challenges' types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactions.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				toPrint = toPrint + "\r\n";
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();
				toPrint = toPrint + types.getKey() + "," + students.getKey() + "," + students.getValue();
				itStudents.remove();
			}
			itTypes.remove();
		}

		return toPrint;
	}

}
