import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	private static boolean continueParse = true;
	private static TreeNodeList myCSTree = new TreeNodeList();
	private static TreeNodeList myASTree = new TreeNodeList();

	public static void Parser() {

	}
	// Let's try some parsing....maybe
	/*
	 * This is the basic idea but I'm not too sure how to actually go about
	 * checking if there are correctly closed blockes
	 */

	// Here is where you create a leaf node
	public static void matchAndAnnihilate(String expectedToken) {

		if (Lexer.getTokenArray().get(0).getType() == expectedToken) {
			System.out.println(
					"PARSED!: EXPECTING " + expectedToken + " AND GOT " + Lexer.getTokenArray().get(0).getType());

			System.out.println("Creating leaf node of " + expectedToken);
			myCSTree.addLeafNode(expectedToken);
			System.out.println("Leaf Node Parent Test " + myCSTree.getCurrItem().getParent().getData());

			Lexer.getTokenArray().remove(0);

		} else {
			System.out.println(
					"PARSE ERROR: EXPECTING " + expectedToken + " BUT GOT " + Lexer.getTokenArray().get(0).getType());
			System.out.println("STOPPING ALL PARSING");
			continueParse = false;

		}
	}

	public static void parseProgram() {
		if (continueParse) {
			// TreeNode root = new TreeNode("root");
			myCSTree.addRootNode("goal");
			myASTree.addASTRootNode("goal");
			System.out.println(myCSTree.getRoot().getData());
			parseBlock();
			// myASTree.climb();

		}
		/*
		 * if(continueParse && !tokenArray.isEmpty()){
		 * matchAndAnnihilate("endProgram"); }else if(tokenArray.isEmpty()){
		 * System.out.println("NO END OF PROGRAM FOUND"); }
		 */
		if (continueParse) {
			matchAndAnnihilate("endProgram");
			myCSTree.climb();

			System.out.println("FINAL ROOT CHECK " + myCSTree.getRoot().getData());
			// System.out.println("Children of root test: " +
			// myTree.getRoot().getChildren().get(0));
			System.out.println("Children of root ");
			myCSTree.rootChildren();
			System.out.println("Children of Main Block: ");

			myCSTree.blockChildren();

			System.out.println("::::::::::::::::");
			System.out.println("AST");

			myASTree.blockChildren();

			System.out.println("AST children test");
			// I THINK THIS WORKS?
			myASTree.printChildren(myASTree.getRoot().getChildren());

			/*
			 * System.out.println(""); System.out.println("::::::::::::::");
			 * System.out.println("CST children test");
			 * myCSTree.printChildren(myCSTree.getRoot().getChildren());
			 * System.out.println("");
			 */

			// print cst
			System.out.println("PRINTING CST");
			System.out.println("::::::::::::");
			myCSTree.getRoot().print("", true);
			System.out.println("");

			// print ast
			System.out.println("PRINTING AST");
			System.out.println("::::::::::::");
			myASTree.getRoot().print("", true);

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
			myCSTree.addBranchNode("block");
			myASTree.addASTBranchNode("block");

			System.out.println(myCSTree.getCurrItem().getData());
			matchAndAnnihilate("leftBrace");
			// myASTree.addLeafNode("leftBraces");

			myCSTree.climb();
			// myASTree.climb();

		}
		if (continueParse) {
			parseStatementList();
			// myASTree.climb();
		}
		if (continueParse) {
			matchAndAnnihilate("rightBrace");
			myCSTree.climb();
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
		if (Lexer.getTokenArray().get(0).getType() == "intWord"
				|| Lexer.getTokenArray().get(0).getType() == "stringWord"
				|| Lexer.getTokenArray().get(0).getType() == "booleanWord"
				|| Lexer.getTokenArray().get(0).getType() == "printWord"
				|| Lexer.getTokenArray().get(0).getType() == "alpha/ID"
				|| Lexer.getTokenArray().get(0).getType() == "ifWord"
				|| Lexer.getTokenArray().get(0).getType() == "whileWord"
				|| Lexer.getTokenArray().get(0).getType() == "leftBrace") {

			myCSTree.addBranchNode("statementList");
			System.out.println(myCSTree.getCurrItem().getData());
			parseStatement();
			myCSTree.climb();
			if (continueParse) {
				parseStatementList();
				myCSTree.climb();
			}
		} else {
			// comment

		}

	}

	public static void parseStatement() {
		if (Lexer.getTokenArray().get(0).getType() == "intWord"
				|| Lexer.getTokenArray().get(0).getType() == "stringWord"
				|| Lexer.getTokenArray().get(0).getType() == "booleanWord") {
			myCSTree.addBranchNode("statement");
			System.out.println(myCSTree.getCurrItem().getData());
			parseVarDecl();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "printWord") {
			myCSTree.addBranchNode("statement");
			System.out.println(myCSTree.getCurrItem().getData());
			parsePrintStatement();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
			myCSTree.addBranchNode("statement");
			System.out.println(myCSTree.getCurrItem().getData());
			parseAssignmentStatement();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
			myCSTree.addBranchNode("statement");
			System.out.println(myCSTree.getCurrItem().getData());
			parseIfStatement();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "whileWord") {
			myCSTree.addBranchNode("statement");
			System.out.println(myCSTree.getCurrItem().getData());
			parseWhileStatement();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "leftBrace") {
			// WHAT TO DO HERE
			parseBlock();
			// This climb() causes the issues of having the final right brace
			// and EOP be a child of root
			// myCSTree.climb();
		}
		// THIS CLIMB IS CAUSING ISSUES
		// myTree.climb();
		// comment
	}

	public static void parseVarDecl() {
		if (continueParse) {
			myCSTree.addBranchNode("varDecl");
			myASTree.addASTBranchNode("varDecl");

			System.out.println(myCSTree.getCurrItem().getData());

			myASTree.addLeafNode(Lexer.getTokenArray().get(0).getType());
			matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());

			myCSTree.climb();
			myASTree.climb();

			parseID();
			myASTree.climb();
		}

	}

	// print statement is a print followed by ( expr )
	public static void parsePrintStatement() {
		if (continueParse) {
			myCSTree.addBranchNode("printStatement");
			myASTree.addASTBranchNode("print");

			System.out.println(myCSTree.getCurrItem().getData());

			matchAndAnnihilate("printWord");
			// myASTree.addLeafNode("print");

			myCSTree.climb();
			// myASTree.climb();
		}
		if (continueParse) {
			matchAndAnnihilate("leftParen");
			myCSTree.climb();
		}
		if (continueParse) {
			myCSTree.addBranchNode("expression");
			System.out.println(myCSTree.getCurrItem().getData());
			parseExpression();
			myASTree.climb();
			myCSTree.climb();

			// parseBoolOp();
			// parseExpression();
		}
		if (continueParse) {
			matchAndAnnihilate("rightParen");
			myCSTree.climb();
		}

		myASTree.climb();

	}

	// Assignment Statements are id = expr
	public static void parseAssignmentStatement() {
		if (continueParse) {
			// may need another climb after making the branchNode
			myCSTree.addBranchNode("assignmentStatement");
			myASTree.addASTBranchNode("assignStatement");

			System.out.println(myCSTree.getCurrItem().getData());

			myASTree.addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("alpha/ID");

			myCSTree.climb();
			myASTree.climb();
		}
		if (Lexer.getTokenArray().get(0).getType() == "assign" && continueParse) {

			matchAndAnnihilate("assign");
			myCSTree.climb();

		} else {
			if (continueParse) {
				matchAndAnnihilate("assign");
				myCSTree.climb();
			}
		}

		if (continueParse) {
			parseExpression();
			myASTree.climb();
		}

	}

	// If statements consist of if booleanExpr block
	public static void parseIfStatement() {
		if (continueParse) {
			myCSTree.addBranchNode("ifStatement");
			myASTree.addASTBranchNode("if");

			System.out.println(myCSTree.getCurrItem().getData());

			matchAndAnnihilate("ifWord");

			myCSTree.climb();
			// myASTree.climb();

			parseBoolExpression();
			/*
			 * Still need to figure out how to get multiple blocks. I think it
			 * has to be in the statement check as a block is a statement as
			 * itself parseBlock();
			 */
			parseBlock();
			// THIS MAY BE IN THE WRONG PLACE
			myCSTree.climb();
			myASTree.climb();
		}
	}

	public static void parseWhileStatement() {
		if (continueParse) {
			myCSTree.addBranchNode("whileStatement");
			myASTree.addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
			System.out.println(myCSTree.getCurrItem().getData());
			matchAndAnnihilate("whileWord");
			myCSTree.climb();

			parseBoolExpression();

			parseBlock();

			myCSTree.climb();
		}
	}

	// Boolean expression = (expr boolop exp) OR boolval
	public static void parseBoolExpression() {
		if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false") {
			myASTree.addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
			myCSTree.climb();
		} else {
			if (Lexer.getTokenArray().get(0).getType() == "leftParen") {
				if (continueParse) {
					matchAndAnnihilate("leftParen");
					myCSTree.climb();
				}
			} else {
				continueParse = false;
				System.out.println("EXPECTING 'TRUE', 'FALSE', OR BOOL EXPRESSION");
				// continueParse = false;
			}

			if (continueParse) {
				myASTree.addASTBranchNode("comparison");
				parseExpression();
				// myASTree.climb();
				// myTree.climb();
			}

			if (continueParse) {
				// myASTree.addBranchNode("comparison");
				parseBoolOp();
				// myASTree.climb();
			}

			if (continueParse) {
				parseExpression();
				myASTree.climb();
			}

			if (Lexer.getTokenArray().get(0).getType() == "rightParen") {
				if (continueParse) {
					matchAndAnnihilate("rightParen");
					myCSTree.climb();
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
				myCSTree.climb();
				// myASTree.climb();
			}
		} else {
			matchAndAnnihilate("boolOp");

		}
	}

	// Expressions are intExprs, stringExpr, booleanExpr, ID
	// NOT WORRYING ABOUT STRINGS AT THE MOMENT..
	public static void parseExpression() {
		if (Lexer.getTokenArray().get(0).getType() == "digit") {
			myCSTree.addBranchNode("intExpression");
			System.out.println(myCSTree.getCurrItem().getData());
			parseIntExpression();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false"
				|| Lexer.getTokenArray().get(0).getType() == "leftParen") {
			myCSTree.addBranchNode("boolExpression");
			System.out.println(myCSTree.getCurrItem().getData());
			parseBoolExpression();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
			myCSTree.addBranchNode("ID");
			System.out.println(myCSTree.getCurrItem().getData());
			parseID();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
			// does the grammar allow for an if inside a print
			myCSTree.addBranchNode("ifExpression");
			System.out.println(myCSTree.getCurrItem().getData());
			parseIfStatement();
			myCSTree.climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "string") {
			myCSTree.addBranchNode("stringExpression");
			System.out.println(myCSTree.getCurrItem().getData());
			parseStringExpression();
			myCSTree.climb();
		} else {
			// System.out.println("BIG ERROR");
			// continueParse = false;
		}

	}

	public static void parseIntExpression() {
		if (continueParse) {
			myASTree.addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("digit");
			myASTree.climb();
			myCSTree.climb();
		}
		// How should the ast look for + operations
		if (Lexer.getTokenArray().get(0).getType() == "intOp" && continueParse) {
			// System.out.println("FOUND THE +");
			myASTree.addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("intOp");
			myCSTree.climb();
			myASTree.climb();
		}

		if (continueParse) {
			parseExpression();
			// Not sure if needed
			// myASTree.climb();
		}
		/*
		 * if (tokenArray.get(0).getType() == "addOp") { if (continueParse) {
		 * matchAndAnnihilate("addOp"); parseExpression(); } } else { //
		 * comments }
		 */
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
					myASTree.addLeafNode(Lexer.getTokenArray().get(0).getValue());
					matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());

					myCSTree.climb();
					myASTree.climb();
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

	/// Still needs to be added to the cst/ast
	public static void parseBoolVal() {
		// Handles the case of just true/false
		if (Lexer.getTokenArray().get(0).getType() == "false" || Lexer.getTokenArray().get(0).getType() == "true") {
			if (continueParse) {
				myASTree.addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
				matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
			}
		} else if (Lexer.getTokenArray().get(0).getType() == "leftParen") { // Handles
																			// case
			// of (expr
			// boolop
			// expr)
			matchAndAnnihilate("leftParen");
			parseExpression();
			matchAndAnnihilate("boolOp");

		}
	}

	/*
	 * public static void parseIntOp() { if (continueParse) {
	 * matchAndAnnihilate("intOp"); parseExpression(); } }
	 */

	// var declaration is a type followed by id

	public static void parseID() {
		if (continueParse) {
			// myTree.addBranchNode("ID");
			// System.out.println(myTree.getCurrItem().getData());
			myASTree.addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("alpha/ID");

			myCSTree.climb();
			myASTree.climb();
		}

	}


}
