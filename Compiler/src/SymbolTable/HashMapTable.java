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
					System.out.println("Type String");
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
					System.out.println("Varibale declared as a boolean");
					
					if (hashArray.get(scopeCounter - 1).get(astNode.getChildren().get(0).getData()) == "booleanWord") {
						System.out.println("TYPE COMPATIBLE BOOLEAN VARIABLE !");
					} else {
						System.out.println("Type error :c ");
					}
				}
			} else {
				// Check parent scopes
				System.out.println("Variable not declared in given scope. Checking parent scopes");
			}

			System.out.println("Assign Statment");
		}

		while (iter.hasNext()) {
			scopeAST((TreeNode) iter.next());
		}

		// System.out.println(" " + astRoot.getData());
		// scopeCheckList.add(astRoot.getData());
		/*
		 * Iterator iter = astNode.getChildren().iterator(); while
		 * (iter.hasNext()) {
		 * 
		 * verticlePrintAst((TreeNode) iter.next()); }
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
