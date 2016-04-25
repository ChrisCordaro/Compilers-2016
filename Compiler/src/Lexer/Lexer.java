package Lexer;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Parse.Parser;

public class Lexer {
	// Keywords
	private static String intPattern = "(int)";
	private static String stringPattern = "(string)";
	private static String booleanPattern = "(boolean)";
	private static String ifPattern = "(if)";
	private static String whilePattern = "(while)";
	private static String printPattern = "(print)";
	private static String falsePattern = "(false)";
	private static String truePattern = "(true)";

	// private static String whiteSpace = "(\\s)";

	private static String endOfProg = "(\\$)";

	private static String assign = "(=)";
	private static String digit = "(\\d)";
	private static String alpha = "([a-z])";
//	private static String space = "(\\s)";
	private static String leftParen = "(\\()";
	private static String rightParen = "(\\))";
	private static String leftBrace = "(\\{)";
	private static String rightBrace = "(\\})";
	private static String quote = "(\")";
	// make string not accept captial letters
	//private static String string = "(\"([^\"]*)\")";
	private static String string = "(\"([a-z\\s]*)\")";


	private static String equality = "(==)";
	private static String notEqual = "(!=)";
	private static String intOp = "(\\+)";

	private static Pattern p1 = Pattern.compile(printPattern + "|" + whilePattern + "|" + intPattern + "|" + ifPattern
			+ "|" + stringPattern + "|" + falsePattern + "|" + truePattern + "|" + booleanPattern + "|" + alpha + "|"
			+ equality + "|" + notEqual + "|" + assign + "|" + leftParen + "|" + rightParen + "|" + digit + "|"
			+ leftBrace + "|" + rightBrace + "|" + string + "|" + intOp + "|" + endOfProg);

	private static ArrayList<Character> charArray = new ArrayList<Character>();
	private static ArrayList<Token> tokenArray = new ArrayList<Token>();
	private static ArrayList<String> validArray = new ArrayList<String>();

	private static int parseIndex = 0;
	// private static boolean continueParse = true;

	private static boolean inString = false;
	private static boolean verbose = false;

	static Parser myParser = new Parser();
	static HashMap myHashMap = new HashMap();

	public static void main(String[] args) throws IOException {
		// Alter file path to
		// FileInputStream fileInput = new
		// FileInputStream("C:/Users/Chris/Desktop/test.txt");
		String filename;
		Scanner scanScan = new Scanner(System.in);
		if (args[0] != null) {
			filename = args[0];
		} else {
			System.out.print("Input file directory:");
			filename = scanScan.nextLine();
		}

		FileInputStream inputFile = new FileInputStream(filename);
		Scanner reader = new Scanner(inputFile);
		String verboseCheck = "";

		if (args[1] != null) {
			System.out.println("" + args[1]);
			if (args[1] == "Y" || args[1] == "N" || args[1] == "y" || args[1] == "n") {
				verboseCheck = args[1];
			}
		} else {
			System.out.println("Would you like to enjoy verbose mode? Please enter capital(Y/N)");
			String input = scanScan.next();
			verboseCheck = input;
		}

		if (verboseCheck == "Y" || verboseCheck == "y") {
			verbose = true;
		} else if (verboseCheck == "N" || verboseCheck == "n") {
			verbose = false;
		}

		ArrayList<Character> charArray = new ArrayList<Character>();

		int r;
		while ((r = inputFile.read()) != -1) {
			char c = (char) r;
			if (verbose) {
				System.out.println(c);
			}
			charArray.add(c);

		}
	

		testForSpace(charArray);
		// System.out.println(analyzeList(charArray));
		//addEOP(charArray);
		
		if (testProperQuote(charArray)) {
			System.out.println("FIX YOUR QUOTES");
		} else if (checkBadInput(analyzeList(charArray))) {
			System.out.println("I CANT LET YOU CONTINUE YOU ENTERED AN INVALID CHAR");
		} else {
			System.out.println("CONTINUING ALONG ALL CHARACTERS ARE GOOD");
			testString(analyzeList(charArray));

			System.out.println("Here are the valid characters that will be analyzed " + validArray);

			for (int i = 0; i < tokenArray.size(); i++) {

				// System.out.println("Token " + i + " : " +
				// tokenArray.get(i).getValue());
				System.out.println("Token " + i + " : " + tokenArray.get(i).getType());
			}
			Parser.parseProgram();
			for (int i = 0; i < tokenArray.size(); i++) {
				System.out.println(tokenArray.get(i).getType());
			}
			
			
		}

		// System.out.println("TESTING FOR STRINGS ");

		// checkIfString(tokenArray);

		// System.out.println(tokenArray.get(2).getType());

		inputFile.close();

	}

	// Iterates through arraylist creating a new array list of characters
	// Until a space is found. (May have to fix how it recognizes a space)
	// Need to add functionality that will tell the user where the error exists.

	public static boolean checkLexError(ArrayList<Character> x) {
		if (checkBadInput(analyzeList(charArray))) {
			if (testProperQuote(x)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static void testForSpace(ArrayList<Character> x) {

		int counter = 1;
		for (int i = 0; i < x.size(); i++) {

			if (x.get(i) != ' ') {

				charArray.add(x.get(i));
				// System.out.println(x.get(i));

			}
			if (x.get(i) == '"') {
				// System.out.println("GOT ONE OF THEM DOUBLES");
				inString = true;
				while (inString) {
					if (x.get(i) != '"') {
						charArray.add(x.get(i));
					} else {
						inString = false;
					}
				}
				/*
				 * for (i = i + 1; x.get(i) != '"'; i++) { //System.out.println(
				 * "IN A STRING"); charArray.add(x.get(i)); }
				 */
				// if (x.get(i) == '"') {
				// charArray.add(x.get(i));
				// }

			}

			if (x.get(i) == '\n') {
				// System.out.println("ON LINE " + counter);

				counter++;
				// intArray.add(x.get(i));

			}

			// if (x.get(i) == ' ') {
			// System.out.println("List of characters");
			if (verbose) {
				System.out.println("Characters being analyzed " + charArray);
			}

			// }
		}
	}

	public static boolean testProperQuote(ArrayList<Character> x) {
		int quoteCount = 0;
		for (int i = 0; i < x.size(); i++) {
			if (x.get(i) == '"') {
				quoteCount++;
			}
		}
		if ((quoteCount & 1) == 0) {
			return false; // Even
		} else {

			return true; // ODD
		}

	}

	/*public static void addEOP(ArrayList<Character> x) {
		int i = x.size() - 1;
	if (x.get(i) != '$') {
			x.add('$');
			System.out.println("I TOOK THE LIBERTY TO ADD AN EOP CHARACTER FOR YOU");
		}
	}*/

	// This checks through the chaacter array to make sure that no 'bad' tokens
	// get through
	// If there is a bad token it will spit out an error and will not continue
	public static boolean checkBadInput(String s) {

		Pattern p2 = Pattern.compile("(\\s)" + "|" + printPattern + "|" + whilePattern + "|" + intPattern + "|"
				+ ifPattern + "|" + stringPattern + "|" + falsePattern + "|" + truePattern + "|" + booleanPattern + "|"
				+ alpha + "|" + equality + "|" + notEqual + "|" + assign + "|" + leftParen + "|" + rightParen + "|"
				+ digit + "|" + leftBrace + "|" + rightBrace + "|" + string + "|" + endOfProg + "|" + intOp);
		Matcher m = p2.matcher(s);

		String removed = m.replaceAll("");
		if (removed.length() > 0) {
			System.out.println("BAD CHARACTER(S): " + removed);
			return true; // there is a bad character
		} else {
			return false; // there are NO bad characters
		}

	}

	// Takes in the array list created in testSpace
	// Converts it to a string
	public static String analyzeList(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for (Character ch : list) {
			builder.append(ch);
		}
		return builder.toString();
	}

	// Test the newly formed string against regex and build tokens
	// Remember longest match
	public static ArrayList<String> testString(String s) {

		// Figure out the correct order to check these because at the moment
		// cannot find the assignment statement and every time i move the regexs
		// around
		/// everything dies
		Pattern r2 = Pattern.compile(printPattern + "|" + whilePattern + "|" + intPattern + "|" + ifPattern + "|"
				+ stringPattern + "|" + falsePattern + "|" + truePattern + "|" + booleanPattern + "|" + alpha + "|"
				+ equality + "|" + notEqual + "|" + assign + "|" + leftParen + "|" + rightParen + "|" + digit + "|"
				+ leftBrace + "|" + rightBrace + "|" + string + "|" + endOfProg + "|" + intOp);

		Matcher m2 = r2.matcher(s);

		Token token;
		int counter = 1;
		boolean found = false;

		while (m2.find()) {
			while (counter < 22 && !found) {
				if (m2.group(counter) != null) {
					// if you match on a quote set global testForString var to
					// true and make everything until the next quote a
					// character.

					validArray.add(m2.group());
					if (verbose) {
						System.out.println("valid array");
						System.out.println(validArray);
					}
					tokenArray.add(token = new Token(counter, m2.group(counter), 1));
					found = true;
				}
				counter++;

			}
			counter = 1;
			found = false;
		}
		// return tokenArray;
		return validArray;
	};

	public static ArrayList<Token> getTokenArray() {
		return tokenArray;
	}
	
	public static ArrayList<String> getValidArray(){
		return validArray;
	}

}