package com.brainchase.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.brainchase.common.Common;
import com.brainchase.common.CsvFileReader;
import com.brainchase.pages.DashboardTeacherPage;

/**
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class Transaction {
	final static Logger logger = Logger.getLogger(DashboardTeacherPage.class);
	String studentName, challengeType, transactionid;
	public static ArrayList<Challenge> challenges = CsvFileReader
			.readChallengesFile(Common.canonicalPath() + File.separator + "challenges.csv");

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
	 * This method is to add a new transaction into existing HashMap
	 * 
	 * @param transactionsOld
	 * @param transactionNew
	 * @return
	 */
	public static HashMap<String, HashMap<String, String>> addTransactions(
			HashMap<String, HashMap<String, String>> transactionsOld,
			HashMap<String, HashMap<String, String>> transactionsNew) {

		// Iterate over challenges
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactionsNew.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();
				transactionsOld.get(types.getKey()).put(students.getKey(), students.getValue());
			}
		}

		return transactionsOld;
	}

	/**
	 * This method is to create transactions HashMap
	 * 
	 * @param transactions
	 */
	public static HashMap<String, HashMap<String, String>> getTransactionsHashMap() {
		HashMap<String, HashMap<String, String>> transactions = new HashMap<String, HashMap<String, String>>();
		for (int i = 0; i < challenges.size(); i++) {
			transactions.put(challenges.get(i).type, new HashMap<String, String>());
		}

		return transactions;
	}

	/**
	 * This method is to create transactions HashMap
	 * 
	 * @param transactions
	 */
	public static HashMap<String, HashMap<String, String>> getAllTransactionsHashMap() {
		HashMap<String, HashMap<String, String>> transactions = new HashMap<String, HashMap<String, String>>();
		for (int i = 0; i < CsvFileReader.CHALLENGES_ACTUAL.length; i++) {
			transactions.put(CsvFileReader.CHALLENGES_ACTUAL[i], new HashMap<String, String>());
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
		writeTransactions(Common.canonicalPath() + File.separator + "transactionsStudent.csv", transactionsLocal);
	}

	/**
	 * This method is to write teacher's transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeTeacherTransactions(HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		writeTransactions(Common.canonicalPath() + File.separator + "transactionsTeacher.csv", transactions);
	}

	/**
	 * This method is to write supervisors transactions to a file
	 * 
	 * @param transactions
	 * @throws IOException
	 */
	public static void writeSupervisorTransactions(HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		writeTransactions(Common.canonicalPath() + File.separator + "transactionsSupervisor.csv", transactions);
	}

	/**
	 * This method is to create transactions mock
	 * 
	 * @param studentName
	 * @param challengeType
	 * @param transactionId
	 * 
	 */
	public static HashMap<String, HashMap<String, String>> mock(String studentName, String challengeType,
			String transactionId) {
		HashMap<String, HashMap<String, String>> transactions = new HashMap<String, HashMap<String, String>>();

		transactions.get(challengeType).put(studentName, transactionId);

		return transactions;
	}

	/**
	 * This method is to remove empty challenges in transactions
	 *
	 * @param transactions
	 * @param dashboard
	 */
	public static void removeEmpty(HashMap<String, HashMap<String, String>> transaction) {
		// Iterate over challenges' types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transaction.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();
			if (types.getValue().isEmpty()) {
				itTypes.remove();
			}
		}
	}

	/**
	 * This method is to compare transaction vs dashboard
	 * 
	 * @param transaction
	 * @param transaction2
	 */
	public static ArrayList<ArrayList<String>> compareTransactions(HashMap<String, HashMap<String, String>> transaction,
			HashMap<String, HashMap<String, String>> transaction2, Boolean transactions) {

		logger.info("Comparing transactions:\r\n" + transactionsToString(transactionsToArrayList(transaction)) + "\r\n"
				+ transactionsToString(transactionsToArrayList(transaction2)));

		removeEmpty(transaction);
		removeEmpty(transaction2);
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

		// Iterate over challenges in student HashMap
		for (String challenge : transaction.keySet()) {

			// Comparison
			Boolean result = false;
			for (String student : transaction.get(challenge).keySet()) {
				ArrayList<String> row = new ArrayList<String>();
				if (transactions) {
					if (transaction2.containsKey(challenge) && !transaction2.get(challenge).isEmpty()
							&& !transaction.get(challenge).isEmpty() && transaction2.get(challenge).get(student)
									.equals(transaction.get(challenge).get(student))) {
						result = true;
					}
				} else {
					if (transaction2.containsKey(challenge) && !transaction2.get(challenge).isEmpty()
							&& !transaction.get(challenge).isEmpty()
							&& transaction2.get(challenge).get(student) != null) {
						result = true;
					}
				}
				row.add(challenge);
				row.add(student);
				if (transactions) {
					if (!transaction.containsKey(challenge) || transaction.get(challenge).isEmpty()) {
						row.add("");
					} else {
						row.add(transaction.get(challenge).get(student));
					}
					if (!transaction2.containsKey(challenge) || transaction2.get(challenge).isEmpty()) {
						row.add("");
					} else {
						row.add(transaction2.get(challenge).get(student));
					}
				}
				row.add(result.toString());
				table.add(row);
			}
		}

		// Iterate over teacher's dashboard
		for (String challenge : transaction2.keySet()) {
			// Checking if element exist
			for (String student : transaction2.get(challenge).keySet()) {
				ArrayList<String> row = new ArrayList<String>();
				if (!transaction.get(challenge).containsKey(student)) {
					row.add(challenge);
					row.add(student);
					if (transactions) {
						if (transaction.get(challenge).isEmpty() || !transaction.get(challenge).containsKey(student)) {
							row.add("");
						} else {
							row.add(transaction.get(challenge).get(student));
						}
						if (transaction2.get(challenge).isEmpty()
								|| !transaction2.get(challenge).containsKey(student)) {
							row.add("");
						} else {
							row.add(transaction2.get(challenge).get(student));
						}
					}
					row.add("false");
					table.add(row);
				}
			}
		}

		return table;
	}

	/**
	 * This method is used to write transaction ids to file
	 * 
	 * @param user
	 * @throws IOException
	 */
	public static void writeTransactions(String path, HashMap<String, HashMap<String, String>> transactions)
			throws IOException {
		Common.writeToFile(path, transactionsToString(transactionsToArrayList(transactions)));
	}

	/**
	 * This method is convert transactions Hash into and ArrayList
	 * 
	 * @param transactions
	 * @return ArrayList<ArrayList<String>>
	 */
	public static ArrayList<ArrayList<String>> transactionsToArrayList(
			HashMap<String, HashMap<String, String>> transactions) {
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

		// Iterate over challenges types
		Iterator<Entry<String, HashMap<String, String>>> itTypes = transactions.entrySet().iterator();
		while (itTypes.hasNext()) {
			Map.Entry<String, HashMap<String, String>> types = (Map.Entry<String, HashMap<String, String>>) itTypes
					.next();

			// Iterate over students
			Iterator<Entry<String, String>> itStudents = types.getValue().entrySet().iterator();
			while (itStudents.hasNext()) {
				ArrayList<String> row = new ArrayList<String>();
				Map.Entry<String, String> students = (Map.Entry<String, String>) itStudents.next();
				row.add(types.getKey());
				row.add(students.getKey());
				row.add(students.getValue());
				table.add(row);
			}
		}

		return table;
	}

	/**
	 * This method is used to write transaction ids to file
	 * 
	 * @param transactions
	 * @return String
	 */
	public static String transactionsToString(ArrayList<ArrayList<String>> transactions) {
		String toPrint = "student_name,challenge_type,transaction_id\r\n";
		for (int i = 0; i < transactions.size(); i++) {
			ArrayList<String> row = transactions.get(i);
			for (int j = 0; j < row.size(); j++) {
				toPrint = toPrint + row.get(j);
				if (j < row.size() - 1) {
					toPrint = toPrint + ",";
				}
			}
			toPrint = toPrint + "\r\n";
		}

		return toPrint;
	}

}
