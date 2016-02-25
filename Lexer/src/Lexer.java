import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static String space = "(\\s)";
	private static String leftParen = "(\\()";
	private static String rightParen = "(\\))";
	private static String leftBrace = "(\\{)";
	private static String rightBrace = "(\\})";
	private static String quote = "(\")";
	private static String string = "(\"([^\"]*)\")";

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
	private static boolean continueParse = true;

	private static boolean inString = false;
	private static boolean verbose = false;

	public static void main(String[] args) throws IOException {
		// Alter file path to
		// FileInputStream fileInput = new
		// FileInputStream("C:/Users/Chris/Desktop/test.txt");
		Scanner Scanscan = new Scanner(System.in);
		System.out.print("Input file directory:");
		String filename = Scanscan.nextLine();
		FileInputStream inputFile = new FileInputStream(filename);
		Scanner reader = new Scanner(inputFile);

		
		System.out.println("Would you like to enjoy verbose mode? Please enter capital(Y/N)");
		Scanner verboseScanner = new Scanner(System.in);
		String input = verboseScanner.next();
		String[] a = new String[2];
		a[0] = "Y";
		a[1] = "N";
		if (a[0].equals(input)) {
			System.out.println("GOOOODD CHOICE");
			verbose = true;
		}else if(a[1].equals(input)){
			System.out.println("BAAADDD CHOICE");
		}else{
			System.out.println("INVALID INPUT DEFAULTING TO NO");
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
		addEOP(charArray);
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
			parseProgram();
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
				System.out.println("ON LINE " + counter);

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

	public static void addEOP(ArrayList<Character> x) {
		int i = x.size() - 1;
		if (x.get(i) != '$') {
			x.add('$');
			System.out.println("I TOOK THE LIBERTY TO ADD AN EOP CHARACTER FOR YOU");
		}
	}

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

	// Let's try some parsing....maybe
	/*
	 * This is the basic idea but I'm not too sure how to actually go about
	 * checking if there are correctly closed blockes
	 */

	public static void matchAndAnnihilate(String expectedToken) {

		if (tokenArray.get(0).getType() == expectedToken) {
			System.out.println("PARSED!: EXPECTING " + expectedToken + " AND GOT " + tokenArray.get(0).getType());
			tokenArray.remove(0);
		} else {
			System.out.println("PARSE ERROR: EXPECTING " + expectedToken + " BUT GOT " + tokenArray.get(0).getType());
			System.out.println("STOPPING ALL PARSING");
			continueParse = false;

		}
	}

	public static void parseProgram() {
		if (continueParse) {
			parseBlock();
		}
		/*
		 * if(continueParse && !tokenArray.isEmpty()){
		 * matchAndAnnihilate("endProgram"); }else if(tokenArray.isEmpty()){
		 * System.out.println("NO END OF PROGRAM FOUND"); }
		 */
		if (continueParse) {
			matchAndAnnihilate("endProgram");
		}

		if (!tokenArray.isEmpty() && continueParse) {
			parseProgram();
		} else {
			continueParse = false;
		}

	}

	// Might have to remove the token instead of using a parseIndex
	public static void parseBlock() {
		if (continueParse) {
			matchAndAnnihilate("leftBrace");
		}
		if (continueParse) {
			parseStatementList();
		}
		if (continueParse) {
			matchAndAnnihilate("rightBrace");
		}
		// parseProgram();

		// THE PROBLEM LIES HERE
		/*
		 * if (continueParse && tokenArray.isEmpty()) { System.out.println(
		 * "WARNING NEED AN END PROGRAM"); }
		 */

		/*
		 * if (continueParse && tokenArray.isEmpty() == false) {
		 * matchAndAnnihilate("endProgram"); } if (tokenArray.isEmpty()) {
		 * continueParse = false; } else { parseProgram(); }
		 */
	}

	// Look to see if there is something between the braces
	// A statement list can be followed by a statement and a statement list
	public static void parseStatementList() {
		if (tokenArray.get(0).getType() == "intWord" || tokenArray.get(0).getType() == "stringWord"
				|| tokenArray.get(0).getType() == "booleanWord" || tokenArray.get(0).getType() == "printWord"
				|| tokenArray.get(0).getType() == "alpha/ID" || tokenArray.get(0).getType() == "ifWord"
				|| tokenArray.get(0).getType() == "whileWord" || tokenArray.get(0).getType() == "leftBrace") {
			parseStatement();
			if (continueParse) {
				parseStatementList();
			}
		} else {
			// comment

		}
	}

	public static void parseStatement() {
		if (tokenArray.get(0).getType() == "intWord" || tokenArray.get(0).getType() == "stringWord"
				|| tokenArray.get(0).getType() == "booleanWord") {
			parseVarDecl();
		} else if (tokenArray.get(0).getType() == "printWord") {
			parsePrintStatement();
		} else if (tokenArray.get(0).getType() == "alpha/ID") {
			parseAssignmentStatement();
		} else if (tokenArray.get(0).getType() == "ifWord") {
			parseIfStatement();
		} else if (tokenArray.get(0).getType() == "whileWord") {
			parseWhileStatement();
		} else if (tokenArray.get(0).getType() == "leftBrace") {
			parseBlock();
		}
		// comment
	}

	// print statement is a print followed by ( expr )
	public static void parsePrintStatement() {
		if (continueParse) {
			matchAndAnnihilate("printWord");
		}
		if (continueParse) {
			matchAndAnnihilate("leftParen");
		}
		if (continueParse) {
			parseExpression();
			// parseBoolOp();
			// parseExpression();
		}
		if (continueParse) {
			matchAndAnnihilate("rightParen");
		}

	}

	// Assignment Statements are id = expr
	public static void parseAssignmentStatement() {
		if (continueParse) {
			matchAndAnnihilate("alpha/ID");
		}
		if (tokenArray.get(0).getType() == "assign" && continueParse) {

			matchAndAnnihilate("assign");

		} else {
			if (continueParse) {
				matchAndAnnihilate("assign");
			}
		}

		if (continueParse) {
			parseExpression();
		}

	}

	// If statements consist of if booleanExpr block
	public static void parseIfStatement() {
		if (continueParse) {
			matchAndAnnihilate("ifWord");
			parseBoolExpression();
			/*
			 * Still need to figure out how to get multiple blocks. I think it
			 * has to be in the statement check as a block is a statement as
			 * itself parseBlock();
			 */
			parseBlock();
		}
	}

	public static void parseWhileStatement() {
		if (continueParse) {
			matchAndAnnihilate("whileWord");
			parseBoolExpression();
			parseBlock();
		}
	}

	// Boolean expression = (expr boolop exp) OR boolval
	public static void parseBoolExpression() {
		if (tokenArray.get(0).getType() == "true" || tokenArray.get(0).getType() == "false") {
			matchAndAnnihilate(tokenArray.get(0).getType());
		} else {
			if (tokenArray.get(0).getType() == "leftParen") {
				if (continueParse) {
					matchAndAnnihilate("leftParen");
				}
			} else {
				continueParse = false;
				System.out.println("EXPECTING 'TRUE', 'FALSE', OR BOOL EXPRESSION");
				// continueParse = false;
			}

			if (continueParse) {
				parseExpression();
			}

			if (continueParse) {
				parseBoolOp();
			}

			if (continueParse) {
				parseExpression();
			}

			if (tokenArray.get(0).getType() == "rightParen") {
				if (continueParse) {
					matchAndAnnihilate("rightParen");
				}
			}
		}

		/*
		 * if (tokenArray.get(0).getType() == "false" ||
		 * tokenArray.get(0).getType() == "true" || tokenArray.get(0).getType()
		 * == "leftParen") { parseBoolVal(); } else { System.out.println(
		 * "EXPECTING A BOOL VALUE(TRUE OR FALSE), YOU HAVE A " +
		 * tokenArray.get(0).getType()); continueParse = false; }
		 * 
		 * if (tokenArray.get(0).getType() == "leftParen") {
		 * 
		 * } // if (continueParse) { // matchAndAnnihilate("leftParen"); // } //
		 * if (continueParse) { // matchAndAnnihilate("rightParen"); // } /*
		 * parseExpression(); matchAndAnnihilate("boolOp"); parseExpression();
		 * matchAndAnnihilate("rightParen");
		 */

	}

	public static void parseBoolOp() {
		if (tokenArray.get(0).getType() == "boolOp") {
			if (continueParse) {
				matchAndAnnihilate("boolOp");
			}
		} else {
			matchAndAnnihilate("boolOp");

		}
	}

	// Expressions are intExprs, stringExpr, booleanExpr, ID
	// NOT WORRYING ABOUT STRINGS AT THE MOMENT..
	public static void parseExpression() {
		if (tokenArray.get(0).getType() == "digit") {
			parseIntExpression();
		} else if (tokenArray.get(0).getType() == "true" || tokenArray.get(0).getType() == "false"
				|| tokenArray.get(0).getType() == "leftParen") {
			parseBoolExpression();
		} else if (tokenArray.get(0).getType() == "alpha/ID") {
			parseID();
		} else if (tokenArray.get(0).getType() == "ifWord") {
			// does the grammar allow for an if inside a print
			parseIfStatement();
		} else if (tokenArray.get(0).getType() == "string") {
			parseStringExpression();
		} else {
			// System.out.println("BIG ERROR");
			// continueParse = false;
		}

	}

	// String expressions are " charList "
	// Char list is a char Charlist or space Charlist or Nothing
	// Currently only handling a char charlist no spaces or empty string
	public static void parseStringExpression() {
		Pattern p = Pattern.compile("\"([^\"\\d]*)\"");
		Matcher m = p.matcher(tokenArray.get(0).getValue());
		if (continueParse) {
			if (m.find()) {
				if (parseCharList(m.group(1))) {
					matchAndAnnihilate(tokenArray.get(0).getType());
				} else {
					System.out.println("ERROR IN YOUR STRING ");
					continueParse = false;
				}

			} else {
				System.out.println("ERROR IN YOUR STRING ");
				continueParse = false;
			}
		}
	}

	public static boolean parseChar(String s) {
		Pattern p = Pattern.compile("([a-z])");
		Matcher m = p.matcher(s);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	// Char list is a char Charlist, space Charlist, or empty
	public static boolean parseCharList(String s) {
		String[] result = s.split("");
		int counter = 0;
		if (parseCharList(result, counter)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean parseCharList(String[] s, int curIndex) {
		if (!(curIndex < s.length)) {
			if (parseChar(s[curIndex])) {
				curIndex++;
				if (parseCharList(s, curIndex)) {
					return true;
				} else {
					return false;
				}

			} else if (parseSpace(s[curIndex])) {
				curIndex++;

				if (parseCharList(s, curIndex)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return true;
		}
	}

	public static boolean parseSpace(String s) {
		Pattern p = Pattern.compile("(\\s)");
		Matcher m = p.matcher(s);

		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static void parseBoolVal() {
		// Handles the case of just true/false
		if (tokenArray.get(0).getType() == "false" || tokenArray.get(0).getType() == "true") {
			if (continueParse) {
				matchAndAnnihilate(tokenArray.get(0).getType());
			}
		} else if (tokenArray.get(0).getType() == "leftParen") { // Handles case
																	// of (expr
																	// boolop
																	// expr)
			matchAndAnnihilate("leftParen");
			parseExpression();
			matchAndAnnihilate("boolOp");

		}
	}

	public static void parseIntExpression() {
		if (continueParse) {
			matchAndAnnihilate("digit");
		}
		if (tokenArray.get(0).getType() == "intOp" && continueParse) {
			// System.out.println("FOUND THE +");
			matchAndAnnihilate("intOp");
		}

		if (continueParse) {
			parseExpression();
		}
		/*
		 * if (tokenArray.get(0).getType() == "addOp") { if (continueParse) {
		 * matchAndAnnihilate("addOp"); parseExpression(); } } else { //
		 * comments }
		 */
	}

	/*
	 * public static void parseIntOp() { if (continueParse) {
	 * matchAndAnnihilate("intOp"); parseExpression(); } }
	 */

	// var declaration is a type followed by id
	public static void parseVarDecl() {
		if (continueParse) {
			matchAndAnnihilate(tokenArray.get(0).getType());
			parseID();
		}
	}

	public static void parseID() {
		if (continueParse) {
			matchAndAnnihilate("alpha/ID");
		}

	}

	public static void parseWhile(ArrayList<Token> t) {

	}
}