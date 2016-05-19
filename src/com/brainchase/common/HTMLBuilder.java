package com.brainchase.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.brainchase.items.Transaction;

/**
 * 
 * This class is to build html pages
 * 
 * @author vbartashchuk@testco.com
 *
 */
public class HTMLBuilder {

	public static void create(ArrayList<HashMap<String, HashMap<String, String>>> transactions, String path)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<style>" + "td { padding: 6px; border: 1px solid #ccc; text-align: left; vertical-align: baseline }"
				+ "</style>");
		sb.append("</head>");
		sb.append("<body>");

		if (transactions.size() > 0) {
			sb.append("<table>");
			sb.append("<tr><td>");
			appendTag(sb, "h3", "Student transactions");
			appendTable(sb, Transaction.transactionsToArrayList(transactions.get(0)), "");
			sb.append("</td></tr>");
			sb.append("</table>");
		}
		appendTag(sb, "h3", " ");
		drawNonStudent(sb, transactions, "Teacher");
		appendTag(sb, "h3", " ");
		drawNonStudent(sb, transactions, "Supervisor");

		sb.append("</body>");
		sb.append("</html>");

		System.out.println(sb.toString());
		Common.writeToFile(path, sb.toString());
	}

	private static void drawNonStudent(StringBuilder sb,
			ArrayList<HashMap<String, HashMap<String, String>>> transactions, String owner) {
		int iterator = 0;

		if (owner.equals("Supervisor")) {
			iterator = 2;
		}
		if (transactions.size() > 1 + iterator) {
			sb.append("<table>");
			sb.append("<tr><td>");
			appendTag(sb, "h3", owner + " dashboard");
			appendTable(sb, Transaction.transactionsToArrayList(transactions.get(1 + iterator)), "dashboard");
			sb.append("</td>");
		}

		if (transactions.size() > 2 + iterator) {
			appendTag(sb, "td", "");
			sb.append("<td>");
			appendTag(sb, "h3", owner + " transactions");
			appendTable(sb, Transaction.transactionsToArrayList(transactions.get(2 + iterator)), "");
			sb.append("</td></tr>");
			sb.append("</table>");
			appendTag(sb, "h3", " ");
		}

		if (transactions.size() > 1 + iterator) {
			sb.append("<table>");
			sb.append("<tr><td>");
			appendTag(sb, "h3", "Compare Student transactions vs. " + owner + " dashboard");

			appendTable(sb, Transaction.compareTransactions(transactions.get(0), transactions.get(1 + iterator), false),
					"compare dashboard");

			sb.append("</td>");
		}

		if (transactions.size() > 2 + iterator) {
			appendTag(sb, "td", "");
			sb.append("<td>");
			appendTag(sb, "h3", "Compare Student transactions vs. " + owner + " transactions");

			appendTable(sb, Transaction.compareTransactions(transactions.get(0), transactions.get(2 + iterator), true),
					"compare");

			sb.append("</td></tr>");
			sb.append("</table>");
		}

	}

	static void appendTable(StringBuilder sb, ArrayList<ArrayList<String>> table, String type) {
		sb.append("<table>");
		if (type.equals("dashboard")) {
			sb.append("<tr><td>").append("Challenge").append("</td><td>").append("Student").append("</td><td>")
					.append("Due Date").append("</td></tr>");
		} else if (type.equals("compare")) {
			sb.append("<tr><td>").append("Challenge").append("</td><td>").append("Student").append("</td><td>")
					.append("Transaction Id Student").append("</td><td>").append("Transaction Id Teacher")
					.append("</td><td>").append("Result").append("</td></tr>");
		} else if (type.equals("compare dashboard")) {
			sb.append("<tr><td>").append("Challenge").append("</td><td>").append("Student").append("</td><td>")
					.append("Result").append("</td></tr>");
		} else {
			sb.append("<tr><td>").append("Challenge").append("</td><td>").append("Student").append("</td><td>")
					.append("Transaction Id").append("</td></tr>");
		}

		for (int i = 0; i < table.size(); i++) {
			sb.append("<tr>");
			ArrayList<String> row = table.get(i);
			for (int j = 0; j < row.size(); j++) {
				sb.append("<td>").append(row.get(j)).append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
	}

	static void appendTag(StringBuilder sb, String tag, String contents) {
		sb.append('<').append(tag).append('>');
		sb.append(contents);
		sb.append("</").append(tag).append('>');
	}

	static void appendDataCell(StringBuilder sb, String contents) {
		appendTag(sb, "td", contents);
	}

	static void appendHeaderCell(StringBuilder sb, String contents) {
		appendTag(sb, "th", contents);
	}

}
