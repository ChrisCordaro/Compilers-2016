package Parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import CodeGenerator.Execution;
import Lexer.Lexer;
import SymbolTable.HashMapTable;
import Tree.TreeNodeList;

public class Parser {
	private static boolean continueParse = true;
	private TreeNodeList myCST;
	// private static ArrayList<ArrayList<TreeNodeList>> myCSTarray = new
	// ArrayList<ArrayList<TreeNodeList>>();
	private ArrayList<TreeNodeList> myCSTarray;
	private HashMapTable myHMT;
	private ArrayList<HashMap> hashArray;
	public ArrayList<TreeNodeList> myASTarray = new ArrayList();
	// private static TreeNodeList myCSTree = new TreeNodeList();
	// private static ArrayList<TreeNodeList> myCSTarray = new ArrayList();
	// private static TreeNodeList myASTree = new TreeNodeList();
	private static int blockNum = 0;

	// private HashMapTable myHMT = new HashMapTable();
	// private static ArrayList<HashMap> hashArray = new ArrayList();
	// private static ArrayList<TreeNodeList> myASTarray = new ArrayList();

	private static int counter = 0;
	private Execution myEXE;
	
	private int testCounter = myASTarray.size();

	public void Parser() {

	}

	// Here is where you create a leaf node
	public void matchAndAnnihilate(String expectedToken) {
	
		
		if (Lexer.getTokenArray().get(0).getType() == expectedToken) {
			if (Lexer.getVerbose()) {
				System.out.println(
						"PARSED!: EXPECTING " + expectedToken + " AND GOT " + Lexer.getTokenArray().get(0).getType());

				System.out.println("Creating leaf node of " + expectedToken);
			}
			myCSTarray.get(counter).addLeafNode(expectedToken);
			// System.out.println("Leaf Node Parent Test " +
			// myCSTarray.get(counter).getCurrItem().getParent().getData());

			Lexer.getTokenArray().remove(0);

		} else {
			System.out.println(
					"PARSE ERROR: EXPECTING " + expectedToken + " BUT GOT " + Lexer.getTokenArray().get(0).getType());
			System.out.println("STOPPING ALL PARSING");
			continueParse = false;

		}
	}

	public void parseProgram() {
		myCST = new TreeNodeList();
		myCSTarray = new ArrayList();
		myHMT = new HashMapTable();
		hashArray = new ArrayList();
		myASTarray = new ArrayList();
		myEXE = new Execution();
		// counter = 0;

		if (continueParse) {

			TreeNodeList myAST = new TreeNodeList();
			myASTarray.add(myAST);

			TreeNodeList myCST = new TreeNodeList();
			myCSTarray.add(myCST);
			// System.out.println(counter);
			// System.out.println();
			// System.out.println(myCSTarray.size());
			myCSTarray.get(counter).addRootNode("goal");

			myASTarray.get(counter).addASTRootNode("goal");

			// System.out.println(myCSTarray.get(counter).getRoot().getData());
			parseBlock();

		}

		if (continueParse) {
			matchAndAnnihilate("endProgram");
			myCSTarray.get(counter).climb();
			if (Lexer.getVerbose()) {
				System.out.println("FINAL ROOT CHECK " + myCSTarray.get(counter).getRoot().getData());

				System.out.println("Children of root ");
			}
			myCSTarray.get(counter).rootChildren();
			// System.out.println("Children of Main Block: ");

			// myCSTarray.get(counter).blockChildren();
		}
		// System.out.println("::::::::::::::::");
		// System.out.println("AST");
		// System.out.println(counter);
		// myASTarray.get(counter).blockChildren();

		// System.out.println("AST children test");

		if (continueParse) {
			System.out.println("PRINTING CST");
			System.out.println("::::::::::::");
			for (int i = 0; i < myCSTarray.size(); i++) {
				System.out.println("Print CST number: " + i);
				myCSTarray.get(counter).getRoot().print("", true);
				System.out.println("");
			}

			// print ast
			System.out.println("PRINTING AST");
			System.out.println("::::::::::::");
			for (int i = 0; i < myASTarray.size(); i++) {
				System.out.println("PRINTING AST");
				myASTarray.get(counter).getRoot().print("", true);
			}

			for (int i = 0; i < myASTarray.size(); i++) {
				// HashMapTable myHMT = new HashMapTable();
				System.out.println("-----START ANALYSIS FOR SYMBOL TABLE ----------");
				myHMT.scopeAST(myASTarray.get(i).getRoot());
				System.out.println("------END---------");
				System.out.println("");
				myEXE.loadEnviornment(myASTarray.get(i).getRoot());
			
				System.out.println();
				System.out.println();
				System.out.println("STATIC TABLE");
				myEXE.printStatic();
				System.out.println();
				System.out.println();
				System.out.println("JUMP TABLE");
				myEXE.printJump();
				System.out.println();
				System.out.println();
				myEXE.replaceJumpAddress();
			//	System.out.println("Execution with replaced jumps");
				
				//myEXE.printExe();
				//System.out.println(" ");
				//System.out.println();
				//replace static addresses and insert them into the exe run time
				//
				//myEXE.printExe();
				
				/*System.out.println("STATIC TABLE WITH CALCULATIONS");
				
			
				System.out.println();
				System.out.println();
				System.out.println("Run time with replaced static addresses");
				*/
				//myEXE.calculateStaticAddress();
				//myEXE.replaceStaticAddress();
				//myEXE.printExe();
				
				//myEXE.fillInExecution();
				myEXE.printExe();
				System.out.println();
				myEXE.printStatic();
				
			}
			for (int i = 0; i < myASTarray.size(); i++) {
				// HashMapTable myHMT = new HashMapTable();
				System.out.println("Printing hash array for program: ");
				System.out.println("-------------START--------------");
				myHMT.printHashArray(myHMT.getHashArray());
				System.out.println("--------------END---------------");
			}
			System.out.println("NEW PROGRAM NEW PROGRAM NEW PROGRAM");

		}

		if (!Lexer.getTokenArray().isEmpty() && continueParse) {

			blockNum = 0;
			// counter = counter +1;
			parseProgram();

		} else {

			continueParse = false;
		}
		// print cst

		// hashtest

		// myHMT.verticlePrintAst(myASTree.getRoot());
		System.out.println();
		// myHMT.scopeCheckAnalyze(myHMT.getScopeCheckList());

	}

	// Might have to remove the token instead of using a parseIndex
	public void parseBlock() {
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("block");
			// System.out.println(blockNum);
			myASTarray.get(counter).addASTBranchNode("block" + blockNum);
			blockNum++;

			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			matchAndAnnihilate("leftBrace");
			// myASTree.addLeafNode("leftBraces");

			myCSTarray.get(counter).climb();
			// myASTree.climb();

		}
		if (continueParse) {
			parseStatementList();
			// myASTree.climb();
		}
		if (continueParse) {
			// myASTree.addLeafNode("endBlock");
			matchAndAnnihilate("rightBrace");

			myASTarray.get(counter).climb();
			myCSTarray.get(counter).climb();
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
	public void parseStatementList() {
		if (Lexer.getTokenArray().get(0).getType() == "intWord"
				|| Lexer.getTokenArray().get(0).getType() == "stringWord"
				|| Lexer.getTokenArray().get(0).getType() == "booleanWord"
				|| Lexer.getTokenArray().get(0).getType() == "printWord"
				|| Lexer.getTokenArray().get(0).getType() == "alpha/ID"
				|| Lexer.getTokenArray().get(0).getType() == "ifWord"
				|| Lexer.getTokenArray().get(0).getType() == "whileWord"
				|| Lexer.getTokenArray().get(0).getType() == "leftBrace") {

			myCSTarray.get(counter).addBranchNode("statementList");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseStatement();
			myCSTarray.get(counter).climb();
			if (continueParse) {
				parseStatementList();
				myCSTarray.get(counter).climb();
			}
		} else {
			// comment

		}

	}

	public void parseStatement() {
		if (Lexer.getTokenArray().get(0).getType() == "intWord"
				|| Lexer.getTokenArray().get(0).getType() == "stringWord"
				|| Lexer.getTokenArray().get(0).getType() == "booleanWord") {
			myCSTarray.get(counter).addBranchNode("statement");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseVarDecl();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "printWord") {
			myCSTarray.get(counter).addBranchNode("statement");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parsePrintStatement();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
			myCSTarray.get(counter).addBranchNode("statement");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseAssignmentStatement();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
			myCSTarray.get(counter).addBranchNode("statement");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseIfStatement();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "whileWord") {
			myCSTarray.get(counter).addBranchNode("statement");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseWhileStatement();
			myCSTarray.get(counter).climb();
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

	public void parseVarDecl() {
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("varDecl");
			myASTarray.get(counter).addASTBranchNode("varDecl");

			System.out.println(myCSTarray.get(counter).getCurrItem().getData());

			myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getType());
			matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());

			myCSTarray.get(counter).climb();
			myASTarray.get(counter).climb();

			parseID();
			myASTarray.get(counter).climb();
		}

	}

	// print statement is a print followed by ( expr )
	public void parsePrintStatement() {
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("printStatement");
			myASTarray.get(counter).addASTBranchNode("print");

			System.out.println(myCSTarray.get(counter).getCurrItem().getData());

			matchAndAnnihilate("printWord");
			// myASTree.addLeafNode("print");

			myCSTarray.get(counter).climb();
			// myASTree.climb();
		}
		if (continueParse) {
			matchAndAnnihilate("leftParen");
			myCSTarray.get(counter).climb();
		}
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("expression");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			/// this still allows print(007) without quotes
			parseExpression();
			// myASTarray.get(counter).climb();
			myCSTarray.get(counter).climb();

			// parseBoolOp();
			// parseExpression();
		}
		if (continueParse) {
			matchAndAnnihilate("rightParen");
			myCSTarray.get(counter).climb();
		}

		myASTarray.get(counter).climb();

	}

	// Assignment Statements are id = expr
	public void parseAssignmentStatement() {
		if (continueParse) {
			// may need another climb after making the branchNode
			myCSTarray.get(counter).addBranchNode("assignmentStatement");
			myASTarray.get(counter).addASTBranchNode("assignStatement");

			System.out.println(myCSTarray.get(counter).getCurrItem().getData());

			myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("alpha/ID");

			myCSTarray.get(counter).climb();
			myASTarray.get(counter).climb();
		}
		if (Lexer.getTokenArray().get(0).getType() == "assign" && continueParse) {

			matchAndAnnihilate("assign");
			myCSTarray.get(counter).climb();

		} else {
			if (continueParse) {
				matchAndAnnihilate("assign");
				myCSTarray.get(counter).climb();
			}
		}

		if (continueParse) {
			parseExpression();
			myASTarray.get(counter).climb();
		}

	}

	// If statements consist of if booleanExpr block
	public void parseIfStatement() {
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("ifStatement");
			myASTarray.get(counter).addASTBranchNode("if");

			System.out.println(myCSTarray.get(counter).getCurrItem().getData());

			matchAndAnnihilate("ifWord");

			myCSTarray.get(counter).climb();
			// myASTree.climb();

			parseBoolExpression();
			/*
			 * Still need to figure out how to get multiple blocks. I think it
			 * has to be in the statement check as a block is a statement as
			 * itself parseBlock();
			 */
			parseBlock();
			// THIS MAY BE IN THE WRONG PLACE
			myCSTarray.get(0).climb();
			myASTarray.get(counter).climb();
		}
	}

	public void parseWhileStatement() {
		if (continueParse) {
			myCSTarray.get(counter).addBranchNode("whileStatement");
			myASTarray.get(counter).addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			matchAndAnnihilate("whileWord");
			myCSTarray.get(counter).climb();

			parseBoolExpression();

			parseBlock();

			myCSTarray.get(counter).climb();
			myASTarray.get(counter).climb();
		}
	}

	// Boolean expression = (expr boolop exp) OR boolval
	public void parseBoolExpression() {
		if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false") {
			myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getValue());
			myASTarray.get(counter).climb();
			matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());
			myCSTarray.get(counter).climb();
		} else {
			if (Lexer.getTokenArray().get(0).getType() == "leftParen") {
				if (continueParse) {
					matchAndAnnihilate("leftParen");
					myCSTarray.get(counter).climb();
				}
			} else {
				continueParse = false;
				System.out.println("PARSE ERROR: EXPECTING 'TRUE', 'FALSE', OR BOOL EXPRESSION");
				// continueParse = false;
			}

			if (continueParse) {
				// myASTree.addASTBranchNode("comparison");
				myASTarray.get(counter).addASTBranchNode(Lexer.getTokenArray().get(1).getValue());
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
				myASTarray.get(counter).climb();
			}

			if (Lexer.getTokenArray().get(0).getType() == "rightParen") {
				if (continueParse) {
					matchAndAnnihilate("rightParen");
					myCSTarray.get(counter).climb();
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

	public void parseBoolOp() {
		if (Lexer.getTokenArray().get(0).getType() == "boolOp") {
			if (continueParse) {
				matchAndAnnihilate("boolOp");
				myCSTarray.get(counter).climb();
				// myASTree.climb();
			}
		} else {
			matchAndAnnihilate("boolOp");

		}
	}

	// Expressions are intExprs, stringExpr, booleanExpr, ID

	public void parseExpression() {
		if (Lexer.getTokenArray().get(0).getType() == "digit") {
			myCSTarray.get(counter).addBranchNode("intExpression");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseIntExpression();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "true" || Lexer.getTokenArray().get(0).getType() == "false"
				|| Lexer.getTokenArray().get(0).getType() == "leftParen") {
			myCSTarray.get(counter).addBranchNode("boolExpression");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseBoolExpression();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "alpha/ID") {
			myCSTarray.get(counter).addBranchNode("ID");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseID();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "ifWord") {
			// does the grammar allow for an if inside a print
			myCSTarray.get(counter).addBranchNode("ifExpression");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseIfStatement();
			myCSTarray.get(counter).climb();
		} else if (Lexer.getTokenArray().get(0).getType() == "string") {
			myCSTarray.get(counter).addBranchNode("stringExpression");
			System.out.println(myCSTarray.get(counter).getCurrItem().getData());
			parseStringExpression();
			myCSTarray.get(counter).climb();
		} else {
			// System.out.println("BIG ERROR");
			// continueParse = false;
		}

	}

	public void parseIntExpression() {
		if (continueParse && (Lexer.getTokenArray().get(1).getType() != "intOp")) {
			myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("digit");
			myASTarray.get(counter).climb();
			myCSTarray.get(counter).climb();
		} else if (continueParse && (Lexer.getTokenArray().get(1).getType() == "intOp")) {
			myASTarray.get(counter).addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("digit");
			// myASTarray.get(counter).climb();
			myCSTarray.get(counter).climb();
		}
		// How should the ast look for + operations
		if (Lexer.getTokenArray().get(0).getType() == "intOp" && continueParse) {
			// System.out.println("FOUND THE +");
			// myASTarray.get(counter).climb();
			myASTarray.get(counter).addASTBranchNode("intExpr");
			matchAndAnnihilate("intOp");
			myCSTarray.get(counter).climb();
			// myASTarray.get(counter).climb();

			if (continueParse) {
				parseExpression();
				myASTarray.get(counter).climb();
				// Not sure if needed
				// myASTree.climb();
			}
			myASTarray.get(counter).climb();
		}

		/*
		 * if (tokenArray.get(0).getType() == "addOp") { if (continueParse) {
		 * matchAndAnnihilate("addOp"); parseExpression(); } } else { //
		 * comments }
		 */
	}

	// String expressions are " charList "
	// Char list is a char Charlist or space Charlist or Nothing

	public void parseStringExpression() {
		Pattern p = Pattern.compile("\"([^\"\\d]*)\"");
		Matcher m = p.matcher(Lexer.getTokenArray().get(0).getValue());
		if (continueParse) {
			if (m.find()) {
				if (parseCharList(m.group(1))) {
					myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getValue());
					matchAndAnnihilate(Lexer.getTokenArray().get(0).getType());

					myCSTarray.get(counter).climb();
					myASTarray.get(counter).climb();
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
	public void parseBoolVal() {
		// Handles the case of just true/false
		if (Lexer.getTokenArray().get(0).getType() == "false" || Lexer.getTokenArray().get(0).getType() == "true") {
			if (continueParse) {
				myASTarray.get(counter).addASTBranchNode(Lexer.getTokenArray().get(0).getValue());
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

	public void parseID() {
		if (continueParse) {
			// myTree.addBranchNode("ID");
			// System.out.println(myTree.getCurrItem().getData());
			myASTarray.get(counter).addLeafNode(Lexer.getTokenArray().get(0).getValue());
			matchAndAnnihilate("alpha/ID");

			myCSTarray.get(counter).climb();
			myASTarray.get(counter).climb();
		}

	}

	public TreeNodeList getAST() {
		return myASTarray.get(counter);
	}
	
	
	
	

}