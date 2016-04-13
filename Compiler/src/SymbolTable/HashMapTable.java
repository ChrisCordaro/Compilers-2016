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
			if (hashArray.get(scopeCounter - 1).containsKey(astNode.getChildren().get(0).getData())) {
				System.out.println("Declared");
				if (astNode.getChildren().get(1).getData().startsWith("\"")) {
					System.out.println("Variable set to a string");
					// Verify type compatible
					// get returns the value of a given key so given the example
					// string a
					// it would return stringWord for the variable a meaning a
					// was declared a string
					if (hashArray.get(scopeCounter - 1).get(astNode.getChildren().get(0).getData()) == "stringWord") {
						System.out.println("TYPE COMPATIBLE STRING!");
					} else {
						System.out.println("Type error :c ");
					}

				} else if (astNode.getChildren().get(1).getData().startsWith("t")
						|| astNode.getChildren().get(1).getData().startsWith("f")) {
					System.out.println("Varibale set to a boolean");

					if (hashArray.get(scopeCounter - 1).get(astNode.getChildren().get(0).getData()) == "booleanWord") {
						System.out.println("TYPE COMPATIBLE BOOLEAN VARIABLE !");
					} else {
						System.out.println("Type error :c ");
					}
				} else {
					// variable is assigned to an int
					// we know this because if it isn't a string or a boolean it
					// can only be an int. If it were something else the parser
					// would have caught it
					System.out.println("Variable declared as an int");
					if (hashArray.get(scopeCounter - 1).get(astNode.getChildren().get(0).getData()) == "intWord") {
						System.out.println("TYPE COMPATIBLE INT VARIABLE !");
					} else {
						System.out.println("Type error :c ");
					}
				}
			} else {
				// Check parent scopes
				// Still need to figure out what to do if its not found
				boolean found = false;
				System.out.println("Variable " + "'" + astNode.getChildren().get(0).getData() + "'"
						+ " not declared in given scope. Checking parent scopes");
				for (int i = 0; i < scopeCounter - 1; i++) {
					if (hashArray.get(i).containsKey(astNode.getChildren().get(0).getData())) {
						System.out.println("FOUND IT IN SCOPE " + (i + 1));
						found = true;
						
						//check now == intWord
						System.out.println(astNode.getChildren().get(1).getData());//3
						
						checkType(astNode.getChildren().get(1).getData());//3 is an int
						System.out.println(check);//intWord
						//a is being assigned to an int
						//check that a was declared as an int
						
						System.out.println(astNode.getChildren().get(0).getData());
						//System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
						if(hashArray.get(i).get(astNode.getChildren().get(0).getData()) == check){
							System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
						}else{
							System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");
						}
						varSetTo = astNode.getChildren().get(1).getData();
					
				

					
						//System.out.println(hashArray.get(i));
						//System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
						
					
					

						break;
					}
				}
				if (found == false) {
					System.out.println("VARIABLE NOT FOUND ERROR");
				}
			}

			//System.out.println("Assign Statment");

			// print statement
		} else if (astNode.getData() == "print") {
			if (hashArray.get(scopeCounter - 1).containsKey(astNode.getChildren().get(0).getData())) {
				System.out.println("Print variable found in current scope");
			} else {
				System.out.println("print statement");
				// print can take any type so there's no need to type check
				// still check the scope, of course
				// May want to put this is in a method but I'm not sure how to
				// reference the astNode as it is recursively called within this
				// method itself
				boolean found = false;
				for (int i = 0; i < scopeCounter - 1; i++) {
					if (hashArray.get(i).containsKey(astNode.getChildren().get(0).getData())) {
						System.out.println("FOUND PRINT VARIABLE IN SCOPE " + (i + 1));
						found = true;

						break;
					}
				}
				if (found == false) {
					System.out.println("VARIABLE NOT FOUND ERROR");
				}
			}
			// handles while and if
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
						
						//check now == intWord
						System.out.println(astNode.getChildren().get(1).getData());//false
						
						checkType(astNode.getChildren().get(1).getData());//false is a booleanWord
						System.out.println(check);//booleanWord
						//a is being assigned to an booleanWord
						//check that a was declared as an boolean
						
						System.out.println(astNode.getChildren().get(0).getData());
						System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));//declared as int word
						if(hashArray.get(i).get(astNode.getChildren().get(0).getData()) == check){
							System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
						}else{
							System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");
						}
						varSetTo = astNode.getChildren().get(1).getData();
					
				

					
						//System.out.println(hashArray.get(i));
						//System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
						
					
					

						break;
					}
				}
				if (found == false) {
					System.out.println("VARIABLE NOT FOUND ERROR");
				}
			}


		}

		while (iter.hasNext()) {
			scopeAST((TreeNode) iter.next());
		}

	}
	
	public static void checkType(String x){
		if (x.startsWith("\"")) {
			check = "stringWord";
		} else	if (x.startsWith("t") || x.startsWith("f")) {
			check = "booleanWord";
		}else{
			check = "intWord";
		}
	}

	public static boolean checkDifScopeVarTypes(String c, String varType) {
		c = check;
		if(check == varType){
			return true;
		}else{
			return false;
		}
		/*
		
		if (setTo.startsWith("\"")) {
			check = "string";
		} else	if (setTo.startsWith("t") || setTo.startsWith("f")) {
			check = "boolean";
		}else{
			check = "int";
		}
		if(check == varType){
			return true;
		}else{
			return false;
		}*/
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
