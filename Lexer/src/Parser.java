import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	private static boolean continueParse = true;
	
	public static void Parser(){
		
	}
	// Let's try some parsing....maybe
		/*
		 * This is the basic idea but I'm not too sure how to actually go about
		 * checking if there are correctly closed blockes
		 */

		public static void matchAndAnnihilate(String expectedToken) {

			if (Lexer.getTokenArray().get(0).getType() == expectedToken) {
				System.out.println("PARSED!: EXPECTING " + expectedToken + " AND GOT " + Lexer.getTokenArray().get(0).getType());
				Lexer.getTokenArray().remove(0);
			} else {
				System.out.println("PARSE ERROR: EXPECTING " + expectedToken + " BUT GOT " +  Lexer.getTokenArray().get(0).getType());
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

			if (!Lexer.getTokenArray().isEmpty() && continueParse) {
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
			if (Lexer.getTokenArray().get(0).getType() == "intWord" || Lexer.getTokenArray().get(0).getType() == "stringWord"
					|| Lexer.getTokenArray().get(0).getType() == "booleanWord" || Lexer.getTokenArray().get(0).getType() == "printWord"
					||Lexer.getTokenArray().get(0).getType() == "alpha/ID" || Lexer.getTokenArray().get(0).getType() == "ifWord"
					|| Lexer.getTokenArray().get(0).getType() == "whileWord" || Lexer.getTokenArray().get(0).getType() == "leftBrace") {
				parseStatement();
				if (continueParse) {
					parseStatementList();
				}
			} else {
				// comment

			}
		}

		public static void parseStatement() {
			if (Lexer.getTokenArray().get(0).getType() == "intWord" || Lexer.getTokenArray().get(0).getType() == "stringWord"
					|| Lexer.getTokenArray().get(0).getType() == "booleanWord") {
				parseVarDecl();
			} else if (Lexer.getTokenArray().get(0).getType() == "printWord") {
				parsePrintStatement();
			} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
				parseAssignmentStatement();
			} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
				parseIfStatement();
			} else if (Lexer.getTokenArray().get(0).getType() == "whileWord") {
				parseWhileStatement();
			} else if (Lexer.getTokenArray().get(0).getType() == "leftBrace") {
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
			if (Lexer.getTokenArray().get(0).getType() == "assign" && continueParse) {

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
			if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false") {
				matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
			} else {
				if (Lexer.getTokenArray().get(0).getType() == "leftParen") {
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

				if (Lexer.getTokenArray().get(0).getType() == "rightParen") {
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
			if (Lexer.getTokenArray().get(0).getType() == "boolOp") {
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
			if (Lexer.getTokenArray().get(0).getType() == "digit") {
				parseIntExpression();
			} else if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false"
					|| Lexer.getTokenArray().get(0).getType() == "leftParen") {
				parseBoolExpression();
			} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
				parseID();
			} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
				// does the grammar allow for an if inside a print
				parseIfStatement();
			} else if (Lexer.getTokenArray().get(0).getType() == "string") {
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
			Matcher m = p.matcher(Lexer.getTokenArray().get(0).getValue());
			if (continueParse) {
				if (m.find()) {
					if (parseCharList(m.group(1))) {
						matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
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
			if (Lexer.getTokenArray().get(0).getType() == "false" || Lexer.getTokenArray().get(0).getType()== "true") {
				if (continueParse) {
					matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
				}
			} else if (Lexer.getTokenArray().get(0).getType() == "leftParen") { // Handles case
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
			if (Lexer.getTokenArray().get(0).getType() == "intOp" && continueParse) {
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
				matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
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
