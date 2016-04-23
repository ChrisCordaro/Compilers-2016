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

	//
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

	public void HashMapTable() {

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
			// System.out.println("New Block/Scope");
			HashMap hm = new HashMap();
			hashArray.add(hm);
			scopeCounter = 0;

		} else if (astNode.getData() == "varDecl") {
			updateScope(astNode);
			// add to previously created scope

			// System.out.println(astNode.getParent().getChildren().isEmpty());
			// System.out.println(astNode.getData());

			// System.out.println(scopeCounter);
			// System.out.println("FINDING PARENT BLOCK");
			// System.out.println(findParentBlock(astNode));
			// add to scopeCounter-1 as scopeCounter is initialized to 0 and
			// then is increased for each block
			// System.out.println("block" + scopeCounter);
			// String test = "block"+scopeCounter;
			if (hashArray.get(scopeCounter).containsKey(astNode.getChildren().get(1).getData())) {
				System.out.println(
						"VARIABLE: " + astNode.getChildren().get(1).getData() + " :ALREADY DECLARED IN CUR SCOPE");
			} else {
				while (addVarDeclToCorrectScope(astNode) == false) {
					// check to make sure that variable is not already declared
					// in the scope

					addVarDeclToCorrectScope(astNode);
				}
			}

			/*
			 * if(findParentBlock(astNode).equals("block"+scopeCounter)){
			 * System.out.println("AHFAJSFJASF"); hashArray.get(scopeCounter -
			 * 1).put(astNode.getChildren().get(1).getData(),
			 * astNode.getChildren().get(0).getData()); }else{ scopeCounter--;
			 * 
			 * if(findParentBlock(astNode).equals("block"+scopeCounter)){
			 * System.out.println("AHFAJSFJASF"); hashArray.get(scopeCounter -
			 * 1).put(astNode.getChildren().get(1).getData(),
			 * astNode.getChildren().get(0).getData()); } }
			 */

		} else if (astNode.getData() == "assignStatement") {
			updateScope(astNode);
			//System.out.println(scopeCounter);
			// Look up symbol to the right(astNode.getChildren().get(1)) in the
			// current scope
			// Check types

			if ((checkKeyInCurrScope(astNode, 0) || findVariableInOtherScopeNoTypeCheck(astNode, 0))
					&& (checkKeyInCurrScope(astNode, 1) || findVariableInOtherScopeNoTypeCheck(astNode, 1))) {
				// assigning a variable to a variable, variables have been found
				System.out.println("ASDASD");
				System.out.println(
						"You're attempting to assign one variable to another. Let me just check that they are both declared and that they are type compatible.");
				if (checkTwoVarAssign(astNode, 0, 1)) {
					System.out.println("Variable " + astNode.getChildren().get(0).getData() + " and variable "
							+ astNode.getChildren().get(1).getData() + " have been found and are type compatible! :)");
				}else{
					
				}

			} else if (checkKeyInCurrScope(astNode, 0)) {
			
				// System.out.println("Variable " +
				// astNode.getChildren().get(0).getData() + " declared in
				// current scope");
				if (assignTypeCheck(astNode, 1) == 1) {
					System.out.println("Variable " + astNode.getChildren().get(0).getData() + " set to a string");
					// Verify type compatible
					// get returns the value of a given key so given the example
					// string a
					// it would return stringWord for the variable a meaning a
					// was declared a string
					if (compareType(astNode, 0, "stringWord")) {
						System.out.println("TYPE COMPATIBLE STRING!");
					} else {
						System.out.println("Type error :c Variable " + astNode.getChildren().get(0).getData()
								+ " cannot be assigned to a string");

					}

				} else if (assignTypeCheck(astNode, 1) == 2) {
					System.out.println("Variable " + astNode.getChildren().get(0).getData() + " set to a boolean");

					if (compareType(astNode, 0, "booleanWord")) {
						System.out.println("TYPE COMPATIBLE BOOLEAN VARIABLE !");
					} else {
						System.out.println("Type error :c Variable " + astNode.getChildren().get(0).getData()
								+ " cannot be assigned to a boolean");

					}
				} else if (assignTypeCheck(astNode, 1) == 3) {
					// variable is assigned to an int
					// we know this because if it isn't a string or a boolean it
					// can only be an int. If it were something else the parser
					// would have caught it
					System.out.println("Variable " + astNode.getChildren().get(0).getData() + " set to an integer");
					if (compareType(astNode, 0, "intWord")) {
						System.out.println("TYPE COMPATIBLE INT VARIABLE !");
					} else {
						System.out.println("Type error :c Variable " + astNode.getChildren().get(0).getData()
								+ " cannot be assigned to an integer ");

					}
				
				}else if(assignTypeCheck(astNode, 1) == 4){
					findVariableInOtherScope(astNode, 1);
				}
			} else {
				// Variable not declared in the current scope, so check if it
				// has been previously declared

				findVariableInOtherScope(astNode, 0);

			}
		} else if (astNode.getData() == "print") {
			updateScope(astNode);
			// no type checking on print if it contains one element being
			// printed
			checkType(astNode.getChildren().get(0).getData());
			if (astNode.getChildren().size() == 1 && (astNode.getChildren().get(0).getChildren().isEmpty())) {
				
				if (checkKeyInCurrScope(astNode, 0)) {
					// System.out.println("VARIABLE FOUND IN CURRENT SCOPE");

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
					if (!astNode.getData().matches("\\d")) {
						findPrintVariableInOtherScope(astNode, 0);
					}
				}
			} else {
				// more than one variable in a print statement
				// iterate through the children of print and find the variables
				// and string literals
				// if theres more than one in the print statement then it is an
				// int expression
				// and the variable will always be at the end

				// System.out.println("You have more than one variable in the
				// print statment: Checki");
				// check if the last child is either an int variable or an int
				// literal
				// printChildren(astNode.getChildren());
				findLastChild(astNode);

				if (hashArray.get(scopeCounter).containsKey(findLastChild(astNode).getData())) {

					System.out.println("Last var of print statement declared in current scope");
				} else {

				}
				/*
				 * if
				 * (checkLastChildOfPrint(findLastChild(astNode.getChildren())))
				 * { System.out.println(
				 * "Last variable has been previously declared and it is the correct scope :)"
				 * ); } else if
				 * (checkLastChildOfPrintInOtherScope(findLastChild(astNode.
				 * getChildren())) == false) { System.out.println(
				 * "Last variable has NOT been previously declared and/or it is the incorrect type:("
				 * );
				 * 
				 * }
				 */

			}
		} else if (astNode.getData().equals("==") || astNode.getData().equals("!=")) {
			
			// check if variable is declared in current scope
			// typical comparison: if(a==1) -> comparison a 1
			// either the first or second variable is in the current scope

			// If both variables are previously declared just type check
			if (checkKeyInCurrScopeNotExplicit(astNode, 0) && checkKeyInCurrScopeNotExplicit(astNode, 1)) {

				scopeTypeCheckCompVariables(astNode);
			} else if (findVariableInOtherScopeNoTypeCheck(astNode, 0)
					&& findVariableInOtherScopeNoTypeCheck(astNode, 1)) {

				scopeTypeCheckCompVariables(astNode);
			} else if (checkKeyInCurrScopeNotExplicit(astNode, 0) && findVariableInOtherScopeNoTypeCheck(astNode, 1)) {

				scopeTypeCheckCompVariables(astNode);
			} else if (findVariableInOtherScopeNoTypeCheck(astNode, 0) && checkKeyInCurrScopeNotExplicit(astNode, 1)) {

				scopeTypeCheckCompVariables(astNode);
			} else if (checkKeyInCurrScope(astNode, 0) || findVariableInOtherScopeNoTypeCheck(astNode, 0)) {
				System.out.println("Continuing");
				// System.out.println("found " +
				// astNode.getChildren().get(0).getData() + " in current
				// scope");
				if (checkCompType(astNode)[0].equals("var") && checkCompType(astNode)[1].equals("literal")) {
					System.out.println("Attempting to compare a VARIABLE to an LITERAL.");
					checkType(astNode.getChildren().get(1).getData());
					System.out.println("Type of " + astNode.getChildren().get(1).getData() + " is " + check);
					if (compareType(astNode, 0, check)) {
						System.out.println("Type comparison for VARIABLE to a LITERAL is CORRECT :)");
					} else {
						System.out.println("ERROR: Type comparison for a VARIABLE to a LITERAL is INCORRECT :(");
					}
				}
			} else {
				checkType(astNode.getChildren().get(0).getData());

				// System.out.println(check);
				if (check == "varDecl") {
					// would have found the scope previously so at this point
					// its an error
					System.out.println("ERROR: " + astNode.getChildren().get(0).getData() + " was never declared");
				} else {
					System.out.println("Literal Value present");
				}
			}
			if (checkKeyInCurrScope(astNode, 1) || findVariableInOtherScopeNoTypeCheck(astNode, 1)) {
				if (checkCompType(astNode)[0].equals("literal") && checkCompType(astNode)[1].equals("var")) {
					System.out.println("Attempting to compare a LITERAL to an VARIABLE.");
					checkType(astNode.getChildren().get(0).getData());
					System.out.println(check);
					if (compareType(astNode, 1, check)) {
						System.out.println("Type comparison for LITERAL to a VARIABLE is CORRECT :)");
					} else {
						System.out.println("ERROR: Type comparison for a LITERAL to a VARIABLE is INCORRECT :(");
					}
				} else if (checkCompType(astNode)[0].equals("literal") && checkCompType(astNode)[1].equals("literal")) {
					System.out.println("Comparing literal to literal no need to scope check");
				}

			} else {
				checkType(astNode.getChildren().get(1).getData());

				// System.out.println(check);
				if (check == "varDecl") {
					// would have found the scope previously so at this point
					// its an errora

					System.out.println("ERROR: " + astNode.getChildren().get(1).getData() + " was never declared");
				} else {
					System.out.println("Literal Value present");
				}

			}
			// scopeTypeCheckCompVariables(astNode);
			/*
			 * if (checkKeyInCurrScope(astNode, 1)) { System.out.println(
			 * "found " + astNode.getChildren().get(1).getData() +
			 * " in current scope"); }else{ findVariableInOtherScope(astNode,
			 * 1); }
			 */
			// System.out.println(hashArray.get(scopeCounter - 1));

		} else if (astNode.getData().equals("intExpr")) {

			// check to make sure the last value is an int or a variable
			// if its a variable type and scope check it
			// if its not a variable check to make sure it is of type int
			checkIntExpression(astNode);

		}

		while (iter.hasNext())

		{

			scopeAST((TreeNode) iter.next());
		}

	}

	public static void checkIntExpression(TreeNode t) {

		if ((!t.getChildren().isEmpty()) && t.getChildren().get(0).getData().matches("\\d")) {
			// System.out.println(t.getChildren().get(0).getData());
			// System.out.println("matched an int");
			checkIntExpression(t.getChildren().get(0));

		} else if (!t.getData().matches("\\d")) {
			System.out.println(
					"Found a variable: " + t.getChildren().get(0).getData() + " :checking it's scope and type");
			// System.out.println(scopeCounter);
			// System.out.println(t.getChildren().get(0).getData());
			// type check for int
			if (hashArray.get(scopeCounter).containsKey(t.getChildren().get(0).getData())) {
				System.out.println("Variable " + t.getChildren().get(0).getData() + " found");
				if (hashArray.get(scopeCounter).get(t.getChildren().get(0).getData()) == "intWord") {
					System.out.println("Variable type of int :)");
				} else {
					System.out.println("HOWEVER, there is a type error :c cannot add "
							+ t.getChildren().get(0).getData() + " which is of type "
							+ hashArray.get(scopeCounter - 1).get(t.getChildren().get(0).getData())
							+ " to an Integer Expression");
				}
			} else {

				findVariableInOtherScope1(t, 0);
			}
		} else {

		}

	}

	public static boolean checkKeyInCurrScope(TreeNode t, int childNum) {
		if (hashArray.get(scopeCounter).containsKey(t.getChildren().get(childNum).getData())) {
		//	System.out.println("Variable: " + t.getChildren().get(childNum).getData() + " :found in current scope");
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkKeyInCurrScopeNotExplicit(TreeNode t, int childNum) {
		if (hashArray.get(scopeCounter).containsKey(t.getChildren().get(childNum).getData())) {
			// System.out.println("Variable: " +
			// t.getChildren().get(childNum).getData() + " :found in current
			// scope");
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkCompAssignVariable(TreeNode t, int child) {
		if (hashArray.get(scopeCounter - 1).containsKey(t.getChildren().get(child).getData())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkTwoVarAssign(TreeNode t, int child1, int child2) {
		boolean found1 = false;
		boolean found2 = false;
		String child1Type = null;
		String child2Type = null;
		if (checkKeyInCurrScope(t, child1)) {
			//System.out.println("Found " + t.getChildren().get(child1).getData() + " in current scope");
			//child1Type = t.getChildren().get(child1).getData();
			child1Type = (String) hashArray.get(scopeCounter).get(t.getChildren().get(child1).getData());
			found1 = true;
		} else {
			System.out.println("CHECKING");
			for (int i = scopeCounter-1; i < scopeCounter; i--) {
				if (hashArray.get(i).containsKey(t.getChildren().get(child1).getData())) {
					System.out.println("Found " + t.getChildren().get(child1).getData() + " in scope " + i);
					child1Type = (String) hashArray.get(i).get(t.getChildren().get(child1).getData());
					found1 = true;
					break;
				}
			}
		}

		if (checkKeyInCurrScope(t, child2)) {
			//System.out.println("Found " + t.getChildren().get(child2).getData() + " in current scope");
			//System.out.println( t.getChildren().get(child2).getData());
			//System.out.println();
			child2Type = (String) hashArray.get(scopeCounter).get(t.getChildren().get(child2).getData());
			found2 = true;
		} else {
			
			for (int j = scopeCounter-1; j >= 0; j--) {

				if (hashArray.get(j).containsKey(t.getChildren().get(child2).getData())) {
					System.out.println("Found " + t.getChildren().get(child2).getData() + " in scope " + j);
					child2Type = (String) hashArray.get(j).get(t.getChildren().get(child2).getData());
					found2 = true;
					break;
				}
			}
		}
		if ((found1 == true) && (found2 == true)) {
			System.out.println(child1Type);
			System.out.println(child2Type);
			// System.out.println("Child 1 type = " + child1Type);
			// System.out.println("Child 2 type = " + child2Type);
			if (child1Type == child2Type) {
				return true;
			} else {
				System.out.println("Variable " + t.getChildren().get(0).getData() + " and variable "
						+ t.getChildren().get(1).getData() + " have been FOUND, but they are NOT type compatible");
			}
		} else {
			if(found1 == false){
				System.out.println("SCOPE ERROR: VARIABLE " + t.getChildren().get(child1).getData() + " NOT FOUND");
			}
			
			if(found2 == false){
				System.out.println("SCOPE ERROR: VARIABLE " + t.getChildren().get(child2).getData() + " NOT FOUND");
			}
			return false;
		}
		return false;

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
		} else  if(t.getChildren().get(child).getData().matches("//d")){
			return 3;
			//variable 
		} else{
			return 4;
			//int
		}
	}

	// Compares the type that the variable is being set to, to the type the
	// variable is declared
	public static boolean compareType(TreeNode t, int child, String type) {
		// for(int i = 0; i < scopeCounter - 1; i ++){
		if (hashArray.get(scopeCounter).get(t.getChildren().get(child).getData()) == type) {
			return true;
		} else {
			for (int i = 0; i < scopeCounter - 1; i++) {
				if (hashArray.get(i).get(t.getChildren().get(child).getData()) == type) {
					//return true;
				}
			}
			return false;
		}
		// }
		// return false;
	}

	public static boolean compareTwoLiterals(TreeNode t) {
		String literal1 = t.getChildren().get(0).getData();
		String literal2 = t.getChildren().get(1).getData();
		if (literal1.startsWith("\"")) {
			literal1 = "stringWord";
		} else if (literal1.matches("(true)") || literal1.matches("(false)")) {
			literal1 = "booleanWord";
		} else if (literal1.matches(("(\\d)"))) {
			literal1 = "intWord";
		} else {
			literal1 = "varDecl";
		}
		if (literal2.startsWith("\"")) {
			literal2 = "stringWord";
		} else if (literal2.matches("(true)") || literal2.matches("(false)")) {
			literal2 = "booleanWord";
		} else if (literal2.matches(("(\\d)"))) {
			literal2 = "intWord";
		} else {
			literal2 = "varDecl";
		}

		if (literal1.equals(literal2)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * public static boolean compareTwoVars(TreeNode t){
	 * 
	 * }
	 */

	public static String checkType(String x) {
		if (x.startsWith("\"")) {
			check = "stringWord";
		} else if (x.matches("(true)") || x.matches("(false)")) {
			check = "booleanWord";
		} else if (x.matches(("(\\d)"))) {
			check = "intWord";
		} else {
			check = "varDecl";
		}
		return check;

	}

	public static void updateScope (TreeNode t) {
	
		if(t.getParent().getData().contains("block")){
			char lastChar = t.getParent().getData().charAt(t.getParent().getData().length()-1);
			scopeCounter = Character.getNumericValue(lastChar);
		}
	}

	public static String[] checkCompType(TreeNode t) {
		String x = t.getChildren().get(0).getData();
		String y = t.getChildren().get(1).getData();
		String resultX;
		String resultY;
		String[] results = new String[2];
		if (x.startsWith("\"") || x.matches("(true)") || x.matches("(false)") || x.matches(("(\\d)"))) {
			resultX = "literal";
		} else {
			resultX = "var";
		}
		if (y.startsWith("\"") || y.matches("(true)") || y.matches("(false)") || y.matches(("(\\d)"))) {
			resultY = "literal";
		} else {
			resultY = "var";
		}
		results[0] = resultX;
		results[1] = resultY;
		return results;
	}

	public static boolean findVariableInOtherScopeNoTypeCheck(TreeNode t, int child) {
		for (int i = scopeCounter-1; i >= 0; i--) {

			if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {

				// System.out.println("FOUND: " +
				// t.getChildren().get(child).getData() + " IN SCOPE " + (i));
				return true;
			}
		}
		// System.out.println("ERROR: Variable " +
		// t.getChildren().get(child).getData() + " not found in any scope");
		return false;
	}

	public static boolean findVariableInOtherScope(TreeNode t, int child) {

		// Check parent scopes
		// Still need to figure out what to do if its not found
		boolean found = false;
		int astChildAddOp = 0;
		if (child == 1) {
			astChildAddOp = child - 1;
		}
		System.out.println("Variable " + "'" + t.getChildren().get(child).getData() + "'"
				+ " not declared in given scope. Checking parent scopes");
		if (child == 1) {
			for (int i = 0; i < scopeCounter - 1; i++) {
				if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {

					System.out.println("FOUND IT IN SCOPE " + (i));
					found = true;

					// check now == intWord

					System.out.println(t.getChildren().get(child).getData());// 3

					checkType(t.getChildren().get(child).getData());

					System.out.println(t.getChildren().get(astChildAddOp).getData());// 3

					checkType(t.getChildren().get(astChildAddOp).getData());

					System.out.println(check);// intWord
					// a is being assigned to an int
					// check that a was declared as an int

					System.out.println(t.getChildren().get(child).getData());
					// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
					if (hashArray.get(i).get(t.getChildren().get(child).getData()) == check) {
						System.out.println("TYPE MATCH ON DIFFERENT SCOPED VALUES :) " + t.getChildren().get(child).getData() + " and " + t.getChildren().get(child-1).getData());
					} else {
						System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES: Cannot assign "
								+ t.getChildren().get(child).getData() + " which is "
								+ hashArray.get(i).get(t.getChildren().get(child).getData()) + " to a " + check);

					}
					// varSetTo = t.getChildren().get(child + 1).getData();

					// System.out.println(hashArray.get(i));
					// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
					return true;
					// break;
				}
			}
			if (found == false) {

				System.out.println("VARIABLE " + t.getChildren().get(1).getData() + " NOT FOUND ERROR");
				return false;
			}
		} else {
			//System.out.println(scopeCounter );
			for (int i = scopeCounter-1; i >= 0; i--) {
				
				if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {
					System.out.println("FOUND IT IN SCOPE " + (i));
					found = true;

					// check now == intWord

					// System.out.println(t.getChildren().get(child).getData());

					checkType(t.getChildren().get(child).getData());

					// System.out.println(t.getChildren().get(child +
					// 1).getData());// 3

					checkType(t.getChildren().get(child + 1).getData());

					// System.out.println(check);// intWord
					// a is being assigned to an int
					// check that a was declared as an int

					System.out.println(t.getChildren().get(child).getData());
					// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
					if (hashArray.get(i).get(t.getChildren().get(child).getData()) == check) {
						System.out.println("TYPE MATCH ON DIFFERENT SCOPED VALUES :)" + t.getChildren().get(child).getData() + " and " + t.getChildren().get(child+1).getData() );
					} else {
						System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES: Cannot assign "
								+ t.getChildren().get(child).getData() + " which is "
								+ hashArray.get(i).get(t.getChildren().get(child).getData()) + " to a " + check);

					}

					if (check == "varDecl") {
						// some method to check the type of
					}
					// varSetTo = t.getChildren().get(child + 1).getData();

					// System.out.println(hashArray.get(i));
					// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
					return true;
					// break;
				}
			}
			if (found == false) {

				System.out.println("VARIABLE " + t.getChildren().get(0).getData() + " NOT FOUND ERROR");
				return false;
			}
			return false;
		}
		return false;
	}

	public static void scopeTypeCheckCompVariables(TreeNode t) {
		int found = 0;
		int scope1 = 0;
		int scope2 = 0;

		/*
		 * if (checkKeyInCurrScope(t, 0)) { System.out.println("Found: " +
		 * t.getChildren().get(0).getData() + " in current scope"); found++; if
		 * (checkKeyInCurrScope(t, 1)) { System.out.println("Found: " +
		 * t.getChildren().get(1).getData() + " in current scope"); found++; }
		 * else {
		 * 
		 * if (findVariableInOtherScope(t, 1)) { found++; } } } else {
		 * 
		 * if (findVariableInOtherScope(t, 0)) { found++; } }
		 */
		// if (found == 2) {
		for (int i = 0; i < scopeCounter - 1; i++) {
			if (hashArray.get(i).containsKey(t.getChildren().get(0).getData())) {
				System.out.println("FOUND: " + t.getChildren().get(0).getData() + " :IN SCOPE " + (i));
				scope1 = i;

			}
		}
		for (int i = 0; i < scopeCounter - 1; i++) {
			if (hashArray.get(i).containsKey(t.getChildren().get(1).getData())) {
				System.out.println("FOUND: " + t.getChildren().get(1).getData() + " :IN SCOPE " + (i));
				scope2 = i;

			}
		}
		if (hashArray.get(scope1).get(t.getChildren().get(0).getData()) == hashArray.get(scope2)
				.get(t.getChildren().get(1).getData())) {
			System.out.println("SUCCESSFUL! Variable: " + t.getChildren().get(0).getData() + " : and variable "
					+ t.getChildren().get(1).getData() + " type compatible");
		} else {
			System.out.println("FAILURE! Variable: " + t.getChildren().get(0).getData() + " : and variable "
					+ t.getChildren().get(1).getData() + " NOT type compatible");
		}
		// }
	}

	public static void findVariableInOtherScope1(TreeNode t, int child) {

		// Check parent scopes
		// Still need to figure out what to do if its not found
		boolean found = false;

		System.out.println("Variable " + "'" + t.getChildren().get(child).getData() + "'"
				+ " not declared in given scope. Checking parent scopes");

		for (int i = scopeCounter-1; i >= 0; i--) {
			if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {
				System.out.println("FOUND IT IN SCOPE " + (i));
				found = true;

				// check now == intWord

				//System.out.println(t.getChildren().get(child).getData());

				checkType(t.getChildren().get(child).getData());

				//System.out.println(t.getChildren().get(child).getData());

				checkType(t.getChildren().get(child).getData());

				//System.out.println(check);

				//System.out.println(t.getChildren().get(child).getData());
				// System.out.println(hashArray.get(i).get(astNode.getChildren().get(0).getData()));
				if (hashArray.get(i).get(t.getChildren().get(child).getData()) == "intWord") {

					System.out.println("TYPE MATCH ON DIFFERENT SCOPED VARIABLES :)");
				} else {

					System.out.println("ERROR: TYPE MISMATCH ON DIFFERENT SCOPED VARIABLES");

				}
				// varSetTo = t.getChildren().get(child + 1).getData();

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
		System.out.println(scopeCounter);
		System.out.println("Variable " + "'" + t.getChildren().get(child).getData() + "'"
				+ " not declared in given scope. Checking parent scopes");
		
		for (int i = scopeCounter-1; i >= 0; i--) {
			if (hashArray.get(i).containsKey(t.getChildren().get(child).getData())) {
				System.out.println("FOUND VARIABLE DECLARATION IN SCOPE " + (i));
				found = true;

				break;
			}

		}
		if (found == false) {
			System.out.println("VARIABLE " + t.getChildren().get(child).getData() + " NOT FOUND ERROR");

		}
	}
	
	public static void findParentBlocks(TreeNode t){
		int counter = 0;
		ArrayList<Integer> parentBlocks = new ArrayList<Integer>();
		while(t.getParent().getData() != null){
			if(t.getParent().getData().contains("block")){
				
			}
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

	public static String findParentBlock(TreeNode t) {
		while (t.getParent().getData().contains("block") == false) {
			t.getParent();
		}
		return t.getParent().getData();
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

	public static TreeNode findLastChild(TreeNode t) {
		if (!t.getChildren().get(0).getChildren().isEmpty()) {
			// System.out.println("checking");
			findLastChild(t.getChildren().get(0));
		} else {
			// System.out.println(t.getChildren().get(0).getData());
			return t.getChildren().get(0);
		}
		return t;

		// int size = t.size();
		// System.out.println(t.get(size - 1).getData());
		// return t.get(size - 1);

	}

	public static boolean checkLastChildOfPrint(TreeNode t) {
		if (t.getData().matches("(\\d)")) {
			System.out.println("last value is an int :)");
		} else {
			if (hashArray.get(scopeCounter - 1).containsKey(t.getData())
					&& hashArray.get(scopeCounter - 1).get(t.getData()) == "intWord") {
				return true;
			} else {
				return false;
			}
		}
		return false;
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

	public static boolean addVarDeclToCorrectScope(TreeNode t) {

		if (findParentBlock(t).equals("block" + scopeCounter)) {
			// System.out.println(scopeCounter);
			hashArray.get(scopeCounter).put(t.getChildren().get(1).getData(), t.getChildren().get(0).getData());
			return true;
		} else {
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