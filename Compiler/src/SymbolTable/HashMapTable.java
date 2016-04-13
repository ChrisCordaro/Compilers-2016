package SymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Parse.Parser;
import Tree.TreeNode;
import Tree.TreeNodeList;

public class HashMapTable {

	private static TreeNodeList AST;
	private static HashMap myHashMap;
	private static ArrayList<String> scopeCheckList = new ArrayList();

	private static ArrayList<HashMap> hashArray = new ArrayList();
	private static int scopeCounter = 0;

	private static String varSetTo;
	private static String varType;
	private static String check;

	public void HashMap() {
		TreeNodeList AST = Parser.getAST();
		HashMap myHashMap = new HashMap();
	}

	public static void verticlePrintAst(TreeNode astRoot) {
		System.out.println(" " + astRoot.getData());
		scopeCheckList.add(astRoot.getData());
		Iterator iter = astRoot.getChildren().iterator();
		while (iter.hasNext()) {

			verticlePrintAst((TreeNode) iter.next());
		}
	}

	public static void scopeAST(TreeNode astNode) {
		Iterator iter = astNode.getChildren().iterator();
		// System.out.println(astNode.getData());
		// System.out.println(astNode.getChildren().toString());

		/// System.out.println( ": " + astNode.getData());
		if (astNode.getData() == "block") {
			// Create new scope
			System.out.println("New Block/Scope");
			HashMap hm = new HashMap();
			hashArray.add(hm);
			scopeCounter = scopeCounter + 1;
			// System.out.println("ScopeCounter: " + scopeCounter);
		} else if (astNode.getData() == "varDecl") {
			// add to previously created scope
			System.out.println(astNode.getData());

			System.out.println(scopeCounter);
			// add to scopeCounter-1 as scopeCounter is initialized to 0 and
			// then is increased for each block
			hashArray.get(scopeCounter - 1).put(astNode.getChildren().get(1).getData(),
					astNode.getChildren().get(0).getData());
		} else if (astNode.getData() == "assignStatement") {
			// Look up symbol to the right(astNode.getChildren().get(1)) in the
			// current scope
			// Check types

			// Checks is variable assigned is in curr scope = scopeCounter -1
			if (checkKeyInCurrScope(scopeCounter - 1, astNode)) {
				System.out.println("Declared");
				if (assignTypeCheck(astNode, 1) == 1) {
					System.out.println("Variable set to a string");
					// Verify type compatible
					// get returns the value of a given key so given the example
					// string a
					// it would return stringWord for the variable a meaning a
					// was declared a string
					if (compareType(astNode, 0, "stringWord")) {
						System.out.println("TYPE COMPATIBLE STRING!");
					} else {
						System.out.println("Type error :c ");
					}

				} else if (assignTypeCheck(astNode, 1) == 2) {
					System.out.println("Varibale set to a boolean");

					if (compareType(astNode, 0, "booleanWord")) {
						System.out.println("TYPE COMPATIBLE BOOLEAN VARIABLE !");
					} else {
						System.out.println("Type error :c ");
					}
				} else if (assignTypeCheck(astNode, 1) == 3) {
					// variable is assigned to an int
					// we know this because if it isn't a string or a boolean it
					// can only be an int. If it were something else the parser
					// would have caught it
					System.out.println("Variable declared as an int");
					if (compareType(astNode, 0, "intWord")) {
						System.out.println("TYPE COMPATIBLE INT VARIABLE !");
					} else {
						System.out.println("Type error :c ");
					}
				}
			} else {
				findVariableInOtherScope(astNode, 0);
				// Check parent scopes
				// Still need to figure out what to do if its not found

				// System.out.println("Assign Statment");

				// print statement
			}
		} else if (astNode.getData() == "print") {
			if (hashArray.get(scopeCounter - 1).containsKey(astNode.getChildren().get(0).getData())) {
				System.out.println(hashArray.get(scopeCounter - 1));
				System.out.println((astNode.getChildren().get(0).getData()));
				System.out.println("Print variable found in current scope");
				// System.out.println(astNode.getChildren().get(1).getData());
			} else if (astNode.getChildren().size() > 1) {
				// more than one thing inside the print expression. Meaning that
				// it's attempting to print(7+x)
				System.out.println("MORE THAN ONE VAR IN PRINT STATEMENT");
			} else {
				System.out.println(hashArray.get(scopeCounter - 1).containsKey(astNode.getChildren().get(0).getData()));
				System.out.println("print statement");
				// print can take any type so there's no need to type check
				// still check the scope, of course
				// May want to put this is in a method but I'm not sure how to
				// reference the astNode as it is recursively called within this
				// method itself
				boolean found = false;
				System.out.println("Variable " + "'" + astNode.getChildren().get(0).getData() + "'"
						+ " not declared in given scope. Checking parent scopes");
				for (int i = 0; i < scopeCounter - 1; i++) {
					if (hashArray.get(i).containsKey(astNode.getChildren().get(0).getData())) {
						System.out.println("FOUND IT IN SCOPE " + (i + 1));
						found = true;

						// check now == intWord
						System.out.println(astNode.getChildren().get(1).getData());// 3

						checkType(astNode.getChildren().get(1).getData());// 3
																			// is
																			// an
																			// int
						System.out.println(check);// intWord
						// a is being assigned to an int
						// check that a was declared as an int

						System.out.println(astNode.getChildren().get(0).getData());
						// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
						if (hashArray.get(i).get(astNode.getChildren().get(0).getData()) == check) {
							System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
						} else {
							System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");
						}
						varSetTo = astNode.getChildren().get(1).getData();

						// System.out.println(hashArray.get(i));
						// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));

						break;
					}
					// handles while and if
				}
				if (found == false) {
					System.out.println("ERROR: VARIABLE NEVER DECLARED");
				}
			}
		} else if (astNode.getData() == "comparison") {

			if (hashArray.get(scopeCounter - 1).containsKey(astNode.getChildren().get(0).getData())) {
				System.out.println("Comparison variable found in current scope");// Variable
																					// found
																					// in
																					// currScope
			} else {
				/*
				 * for example of: {int b{if(b == false)}}
				 */
				boolean found = false;
				System.out.println("Variable " + "'" + astNode.getChildren().get(0).getData() + "'"
						+ " not declared in given scope. Checking parent scopes");
				for (int i = 0; i < scopeCounter - 1; i++) {
					if (hashArray.get(i).containsKey(astNode.getChildren().get(0).getData())) {
						System.out.println("FOUND IT IN SCOPE " + (i + 1));
						found = true;

						// check now == intWord
						System.out.println(astNode.getChildren().get(1).getData());// false

						checkType(astNode.getChildren().get(1).getData());// false
																			// is
																			// a
																			// booleanWord
						System.out.println(check);// booleanWord
						// a is being assigned to an booleanWord
						// check that a was declared as an boolean

						System.out.println(astNode.getChildren().get(0).getData());
						System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));// declared
																											// as
																											// int
																											// word
						if (hashArray.get(i).get(astNode.getChildren().get(0).getData()) == check) {
							System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
						} else {
							System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");
						}
						varSetTo = astNode.getChildren().get(1).getData();

						// System.out.println(hashArray.get(i));
						// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));

						break;
					}
				}
				if (found == false) {
					System.out.println("VARIABLE NOT FOUND ERROR");
				}
			}

		} else if (astNode.getData().equals("+")) {
			System.out.println("FOUND AN ADDITION");
			// check to make sure that the value to the left and the right of
			// the + sign are ints
		}

		while (iter.hasNext()) {

			scopeAST((TreeNode) iter.next());
		}

	}

	public static boolean checkKeyInCurrScope(int scope, TreeNode t) {
		if (hashArray.get(scopeCounter - 1).containsKey(t.getChildren().get(0).getData())) {
			return true;
		} else {
			return false;
		}
	}

	public static int assignTypeCheck(TreeNode t, int child) {
		if (t.getChildren().get(child).getData().startsWith("\"")) {
			return 1;
			// string
		} else if (t.getChildren().get(child).getData().startsWith("t")
				|| t.getChildren().get(child).getData().startsWith("f")) {
			return 2;
			// boolean literal
		} else {
			return 3;
			// int
		}
	}

	public static boolean compareType(TreeNode t, int child, String type) {
		if (hashArray.get(scopeCounter - 1).get(t.getChildren().get(child).getData()) == type) {
			return true;
		} else {
			return false;
		}
	}

	public static void checkType(String x) {
		if (x.startsWith("\"")) {
			check = "stringWord";
		} else if (x.startsWith("t") || x.startsWith("f")) {
			check = "booleanWord";
		} else {
			check = "intWord";
		}
	}

	public static void findVariableInOtherScope(TreeNode t, int child) {

		// Check parent scopes
		// Still need to figure out what to do if its not found
		boolean found = false;
		System.out.println("Variable " + "'" + t.getChildren().get(child).getData() + "'"
				+ " not declared in given scope. Checking parent scopes");
		for (int i = 0; i < scopeCounter - 1; i++) {
			if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {
				System.out.println("FOUND IT IN SCOPE " + (i + 1));
				found = true;

				// check now == intWord
				System.out.println(t.getChildren().get(child + 1).getData());// 3

				checkType(t.getChildren().get(child + 1).getData());// 3
																	// is
																	// an
																	// int
				System.out.println(check);// intWord
				// a is being assigned to an int
				// check that a was declared as an int

				System.out.println(t.getChildren().get(child).getData());
				// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
				if (hashArray.get(i).get(t.getChildren().get(child).getData()) == check) {
					System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
				} else {
					System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");
				}
				varSetTo = t.getChildren().get(child + 1).getData();

				// System.out.println(hashArray.get(i));
				// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));

				break;
			}
		}
		if (found == false) {
			System.out.println("VARIABLE NOT FOUND ERROR");
		}
	}

	public static boolean checkDifScopeVarTypes(String c, String varType) {
		c = check;
		if (check == varType) {
			return true;
		} else {
			return false;
		}
		/*
		 * 
		 * if (setTo.startsWith("\"")) { check = "string"; } else if
		 * (setTo.startsWith("t") || setTo.startsWith("f")) { check = "boolean";
		 * }else{ check = "int"; } if(check == varType){ return true; }else{
		 * return false; }
		 */
	}

	public void printHashArray(ArrayList<HashMap> hash) {
		for (int i = 0; i < hash.size(); i++) {
			System.out.println(hash.get(i));
		}
	}

	public void scopeCheckAnalyze(ArrayList<String> SCL) {
		int counter = 0;
		Iterator<String> iter = SCL.iterator();

		while (iter.hasNext()) {

			if (SCL.get(counter) == "block") {
				HashMap hm = new HashMap();
				hashArray.add(hm);
				counter++;

			} else {
				counter++;
			}

			/*
			 * for(int i = 0; i < SCL.size(); i++){ if(SCL.get(i) == "block" &&
			 * SCL.get(i) != "goal"){ HashMap hm = new HashMap();
			 * hashArray.add(hm);
			 * 
			 * }else if(SCL.get(i) == "varDecl"){ //.put(SCL.get(i+1),
			 * SCL.get(i)); } }
			 */

		}
		System.out.println(hashArray.size());
	}

	public static HashMap getMyHashMap() {
		return myHashMap;
	}

	public static void setMyHashMap(HashMap myHashMap) {
		HashMapTable.myHashMap = myHashMap;
	}

	public static ArrayList<String> getScopeCheckList() {
		return scopeCheckList;
	}

	public static void setScopeCheckList(ArrayList<String> scopeCheckList) {
		HashMapTable.scopeCheckList = scopeCheckList;
	}

	public static ArrayList<HashMap> getHashArray() {
		return hashArray;
	}

	public static void setHashArray(ArrayList<HashMap> hashArray) {
		HashMapTable.hashArray = hashArray;
	}

	public int getScopeCounter() {
		return scopeCounter;
	}

	public void setScopeCounter(int scopeCounter) {
		this.scopeCounter = scopeCounter;
	}

}
