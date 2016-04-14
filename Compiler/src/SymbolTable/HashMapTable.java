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
	private static boolean keepGoing = true;

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

		if (astNode.getData().contains("block")) {
			// Create new scope
			System.out.println("New Block/Scope");
			HashMap hm = new HashMap();
			hashArray.add(hm);
			scopeCounter = scopeCounter + 1;

		} else if (astNode.getData() == "varDecl") {
			// add to previously created scope
			System.out.println(astNode.getParent().getChildren().isEmpty());
			System.out.println(astNode.getData());

			System.out.println(scopeCounter);
			System.out.println("FINDING PARENT BLOCK");
			System.out.println(findParentBlock(astNode));
			// add to scopeCounter-1 as scopeCounter is initialized to 0 and
			// then is increased for each block
			System.out.println("block"+scopeCounter);
			//String test = "block"+scopeCounter;
			
			while(addVarDeclToCorrectScope(astNode )== false){
				addVarDeclToCorrectScope(astNode);
			}
		
			
			/*if(findParentBlock(astNode).equals("block"+scopeCounter)){
				System.out.println("AHFAJSFJASF");
				hashArray.get(scopeCounter - 1).put(astNode.getChildren().get(1).getData(),
						astNode.getChildren().get(0).getData());
			}else{
				scopeCounter--;

				if(findParentBlock(astNode).equals("block"+scopeCounter)){
					System.out.println("AHFAJSFJASF");
					hashArray.get(scopeCounter - 1).put(astNode.getChildren().get(1).getData(),
							astNode.getChildren().get(0).getData());
				}
			}*/
			

		} else if (astNode.getData() == "assignStatement" ) {
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
				// Variable not declared in the current scope, so check if it
				// has been previously declared
			
					findVariableInOtherScope(astNode, 0);
			

			}
		} else if (astNode.getData() == "print" ) {
			// no type checking on print if it contains one element being
			// printed
			checkType(astNode.getChildren().get(0).getData());
			if (astNode.getChildren().size() == 1) {

				if (checkKeyInCurrScope(scopeCounter - 1, astNode)) {
					System.out.println("VARIABLE FOUND IN CURRENT SCOPE");

				} else if (astNode.getChildren().get(0).getData().matches("(\\d)")) {
					System.out.println("PRINTING INT LITERAL");
				} else if (astNode.getChildren().get(0).getData().matches("(\"([^\"]*)\")")) {
					System.out.println("PRINTING STRING LITERAL");
				} else if (astNode.getChildren().get(0).getData().matches("(true)")
						|| astNode.getChildren().get(0).getData().matches("(false)")) {
					System.out.println("PRINTING BOOLEAN LITERAL");
				} else {
					// Variable not found in current scope and is not a literal
					// type, check previous scopes
					findPrintVariableInOtherScope(astNode, 0);
				}

			} else {
				// more than one variable in a print statement
				// iterate through the children of print and find the variables
				// and string literals
				// if theres more than one in the print statement then it is an
				// int expression
				// and the variable will always be at the end

				System.out.println("You have more than one variable in the print statment");
				printChildren(astNode.getChildren());
				findLastChild(astNode.getChildren());

				if (checkLastChildOfPrint(findLastChild(astNode.getChildren()))) {
					System.out.println("Last variable has been previously declared and it is the correct scope :)");
				} else if (checkLastChildOfPrintInOtherScope(findLastChild(astNode.getChildren())) == false) {
					System.out.println(
							"Last variable has NOT been previously declared and/or it is the incorrect type:(");
				
				}

			}
		} else if (astNode.getData() == "comparison") {
			// check if variable is declared in current scope
			// typical comparison: if(a==1) -> comparison a 1
			if (astNode.getChildren().size() == 2) {
				checkType(astNode.getChildren().get(0).getData());

				if (check == "stringWord" || check == "intWord" || check == "booleanWord") {
					System.out.println("Literal Value");
					System.out.println(astNode.getChildren().get(1).getData());

					checkType(astNode.getChildren().get(1).getData());
					System.out.println(check);

					if (check == "stringWord" || check == "intWord" || check == "booleanWord") {
						// variable check if declared
						System.out.println("Another literal");

					} else if (checkCompAssignVariable(1, astNode)) {
						// System.out.println("Assignment variable found in
						// currScope");
					} else {
						findVariableInOtherScope(astNode, 1);
					}

				} else if (checkKeyInCurrScope(scopeCounter - 1, astNode)) {
					System.out.println("Comparison variable found in current scope");
					// now check if it is type compatible to what it is being
					// set to
					// System.out.println(astNode.getChildren().get(1).getData());
					checkType(astNode.getChildren().get(1).getData());
					System.out.println(check);
					if (compareType(astNode, 0, check)) {
						System.out.println("Comparison type compatible :)");
					} else {
						System.out.println("Error on type compatibility :(");
						
					}

				} else {
					// check if variable is declared in previous scopes
					findVariableInOtherScope(astNode, 0);

				}
			}
		} else if (astNode.getData().equals("+")) {
			System.out.println("FOUND AN ADDITION");
			// check to make sure that the value to the left and the right
			// of
			// the + sign are ints
		}

		while (iter.hasNext())

		{
			
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

	public static boolean checkCompAssignVariable(int child, TreeNode t) {
		if (hashArray.get(scopeCounter - 1).containsKey(t.getChildren().get(child).getData())) {
			return true;
		} else {
			return false;
		}
	}

	// Checks what type the variable is assigned to
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

	// Compares the type that the variable is being set to, to the type the
	// variable is declared
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
		} else if (x.matches("(true)") || x.matches("(false)")) {
			check = "booleanWord";
		} else if (x.matches(("(\\d)"))) {
			check = "intWord";
		} else {
			check = "varDecl";
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

	public static void findPrintVariableInOtherScope(TreeNode t, int child) {
		boolean found = false;
		String declaredVarType;
		System.out.println("Variable " + "'" + t.getChildren().get(child).getData() + "'"
				+ " not declared in given scope. Checking parent scopes");
		for (int i = 0; i < scopeCounter - 1; i++) {
			if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {
				System.out.println("FOUND VARIABLE DECLARATION IN SCOPE " + (i + 1));
				found = true;

				break;
			}
			

		}
		if (found == false) {
			System.out.println("VARIABLE NOT FOUND ERROR1");
			
		}
	}

	public static boolean checkDifScopeVarTypes(String c, String varType) {
		c = check;
		if (check == varType) {
			return true;
		} else {
			return false;
		}
		
	}
	
	public static String findParentBlock(TreeNode t){
		while(t.getParent().getData().contains("block") == false){
			t.getParent();
		}return t.getParent().getData();
	}

	public void printHashArray(ArrayList<HashMap> hash) {
		for (int i = 0; i < hash.size(); i++) {
			System.out.println("Scope " + i + hash.get(i));
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

	public static void printChildren(ArrayList<TreeNode> t) {
		for (TreeNode x : t) {
			// System.out.println("");
			System.out.println("child: " + x.getData());
			// System.out.println("The parent of " + x.getData() + " is as
			// follows: " + x.getParent().getData());
			printChildren(x.getChildren());
		}
	}

	public static TreeNode findLastChild(ArrayList<TreeNode> t) {
		int size = t.size();
		System.out.println(t.get(size - 1).getData());
		return t.get(size - 1);

	}

	public static boolean checkLastChildOfPrint(TreeNode t) {
		if (hashArray.get(scopeCounter - 1).containsKey(t.getData())
				&& hashArray.get(scopeCounter - 1).get(t.getData()) == "intWord") {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkLastChildOfPrintInOtherScope(TreeNode t) {

		System.out.println(
				"Variable " + "'" + t.getData() + "'" + " not declared in given scope. Checking parent scopes");
		for (int i = 0; i < scopeCounter - 1; i++) {
			if (hashArray.get(i).containsKey(t.getData()) && hashArray.get(i).get(t.getData()) == "intWord") {
				System.out.println("FOUND VARIABLE DECLARATION IN SCOPE AND IT IS OF THe CORRECT TYPE " + (i + 1));
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean addVarDeclToCorrectScope(TreeNode t){

		if(findParentBlock(t).equals("block"+scopeCounter)){
			
			hashArray.get(scopeCounter - 1).put(t.getChildren().get(1).getData(),
					t.getChildren().get(0).getData());
			return true;
		}else{
			scopeCounter--;
			return false;
		}
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
