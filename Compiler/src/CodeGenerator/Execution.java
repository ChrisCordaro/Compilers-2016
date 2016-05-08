package CodeGenerator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import Tree.TreeNode;

public class Execution {
	private ArrayList<String> exeArray = new ArrayList<String>();
	private ArrayList<StaticTable> staticArray = new ArrayList<StaticTable>();
	private ArrayList<JumpTable> jumpArray = new ArrayList<JumpTable>();
	private ArrayList<String> stringArray = new ArrayList<String>();

	private int tempCounter = -1;
	private int staticCounter = 0;
	private int scopeCounter;
	private int scopeOfIf;
	private int programCounter;
	private String varAddress;

	private boolean insideIf = false;

	private String staticTemp = "T" + getTempCounter() + "XX";
	private int jumpCounter = 0;
	private ArrayList<Integer> calculatedJumps = new ArrayList<Integer>();

	public void loadEnviornment(TreeNode t) {
		// System.out.println(programCounter);

		// if (programCounter == 25) {
		// calculateJump();
		// }
		
		Iterator iter = t.getChildren().iterator();
		
	

		if (t.getData().contains("block")) {
			// Create new scope
			// System.out.println("New Block/Scope");

		} else if (t.getData().equals("varDecl")) {
			updateScope(t);

			// if (scopeCounter == scopeOfIf) {
			// System.out.println("VAR " + t.getChildren().get(1).getData() + "
			// YOU ARE OUT OF THE IF");
			// this works because even though the first var decls happen before
			// the if statement is even processed, there is no jump foor
			// calculateJump to work with
			// and calculateJump checks agains the jumps it has already run so a
			// jump wont be rewrittenb
			// HOWEVER THIS DOESNT WORK IF THERE IS NOTHING AFTER THE IF

			// }
			tempCounter++;
			if (t.getChildren().get(0).getData().equals("intWord")) {

				// generate code to initialize ints to 0
				// load accumulator with 0
				// A9 00 8D T0 XX
				addExeEntry("A9", "00", "8D", "XX");
				addStaticEntry(t.getChildren().get(1).getData(), "idk", scopeCounter);
				staticCounter++;

			} else if (t.getData().equals("booleanWord")) {
				addExeEntry("A9", "00", "8D", "XX");
				addStaticEntry(t.getChildren().get(1).getData(), "idk", scopeCounter);
			} else if (t.getChildren().get(0).getData().equals("stringWord")) {
				addStaticEntry(t.getChildren().get(1).getData(), "idk", scopeCounter);
			}
		} else if (t.getData().equals("assignStatement")) {
			updateScope(t);
			// Anything BUT comparing one variable to another
			if(t.getChildren().get(1).getData().startsWith("\"")){
				System.out.println("assignment to a string");
				dealWithString(t.getChildren().get(1));
			}else if ( t.getChildren().get(1).getData().matches("(true)")
					|| t.getChildren().get(1).getData().matches("(false)")
					|| t.getChildren().get(1).getData().matches("(\\d)")) {
				addExeEntry("A9", t.getChildren().get(1).getData(), "8D", "XX");

			} else {
				// Comparing two variable
				// Load accumulator with contents of the second value
				// Store the accumulator in the address for the first one
				// which

			
				twoVarAssign(t.getChildren().get(0), t.getChildren().get(1));
			}
		} else if (t.getData().equals("print")) {
			updateScope(t);
			checkChildOfIf(t);
			// printing a literal
			if (t.getChildren().get(0).getData().startsWith("\"") || t.getChildren().get(0).getData().matches("(true)")
					|| t.getChildren().get(0).getData().matches("(false)")
					|| t.getChildren().get(0).getData().matches("(\\d)")) {

			} else {
				// printing a var

				loadPrint(t.getChildren().get(0));

			}
		} else if (t.getData().equals("==")) {
			updateScope(t);
			scopeOfIf = scopeCounter;
			// if(a==b)
			// load the X register with the contents of a
			// compare the X register to the contents of b
			loadComparision(t);

		}

		while(iter.hasNext())

		{
				programCounter++;
				if (insideIf) {
					if (!checkChildOfIf(t)) {
						calculateJump();
					}
				}
				loadEnviornment((TreeNode) iter.next());
		

		}
		

	}
	
	public void dealWithString(TreeNode t){
		String[] brokenT = t.getData().split("(?!^)");
		ArrayList<String> s = new ArrayList<String> ();
		for(int i = 0; i < brokenT.length; i ++){
			
			s.add(brokenT[i]);
		}
		
	
		
		
		
		for(int i = 0; i < s.size(); i ++){
			if(s.get(i) == "\"" || s.get(i) == "00"){
				
			}else{
				//convert values 
			//	convertStringArray(s);
			}
		}
		
		exeArray.add("A9");
		int strLength = t.getData().length() + 1;//for "00"
		String stringLength = Integer.toString(strLength);
		exeArray.add(stringLength);
	
		//convertStringArray(stringArray);
	}
	
	public void convertStringArray(ArrayList<String> s){
		for(int i = 0; i < s.size(); i ++){
			System.out.println("ASD");
			String x =  Integer.decode(s.get(i));
			stringArray.add(i, x);
			
		}
		s.add("00");
		System.out.println(s);
	}

	public void calculateStaticAddress() {
		int endValue = exeArray.size() + 1; // this wont work when strings are
											// introduced
		String endHex = convert(endValue);
		// System.out.println("END ADDRESS IS " + endHex);

		for (int i = 0; i < staticArray.size(); i++) {

			// System.out.println(endHex);
			endHex = convert(endValue++);

			staticArray.get(i).setAddress(endHex + "00");
		}

	}

	public void replaceStaticAddress() {
		for (int i = 0; i < staticArray.size(); i++) {
			String tempT = staticArray.get(i).getTemp().substring(0, 2);
			String tempXX = staticArray.get(i).getTemp().substring(2, 4);
			// System.out.println(tempT);
			// System.out.println(tempXX);

			String tempAdd = staticArray.get(i).getAddress().substring(0, 2);
			String tempAddXX = staticArray.get(i).getAddress().substring(2, 4);
			// System.out.println(tempAdd);
			// System.out.println(tempAddXX);

			Collections.replaceAll(exeArray, tempT, tempAdd);
			Collections.replaceAll(exeArray, tempXX, tempAddXX);
		}

	}

	public boolean checkChildOfIf(TreeNode t) {
		while (!t.getData().equals("goal")) {
			
			if (t.getParent().getData().equals("if")) {
				System.out.println(t.getData() + " YOURE A CHILD OF AN IF STATMENET");
				insideIf = true;
				return true;
			} else {

				return checkChildOfIf(t.getParent());
			}

		}
		insideIf = false;

		return false;
	}

	public void replaceJumpAddress() {
		for (int i = 0; i < jumpArray.size(); i++) {
			Collections.replaceAll(exeArray, jumpArray.get(i).getTemp(), jumpArray.get(i).getDistance());
		}
	}

	public String convert(int n) {
		if (Integer.toHexString(n).length() == 1) {
			return "0" + Integer.toHexString(n);
		} else {
			return Integer.toHexString(n);
		}
	}

	public void updateScope(TreeNode t) {

		if (t.getParent().getData().contains("block")) {
			char lastChar = t.getParent().getData().charAt(t.getParent().getData().length() - 1);
			scopeCounter = Character.getNumericValue(lastChar);
		}
	}

	public void addExeEntry(String opCode, String value, String accu, String xx) {
		String tempLocal = "T" + tempCounter;
		exeArray.add(opCode);
		exeArray.add(value);
		exeArray.add(accu);
		exeArray.add(tempLocal);
		exeArray.add(xx);
		// tempCounter++;

	}

	public void addStaticEntry(String var, String add, int scope) {
		StaticTable t = new StaticTable();
		staticArray.add(t);
		String tempLocal = "T" + tempCounter + "XX";
		// staticArray.add(tempLocal);
		staticArray.get(staticCounter).setVar(var);
		staticArray.get(staticCounter).setTemp(tempLocal);
		staticArray.get(staticCounter).setAddress(add);
		staticArray.get(staticCounter).setScope(scope);
	}

	public void twoVarAssign(TreeNode left, TreeNode right) {
		String leftAddress;
		String rightAddress;

		for (int i = 0; i < staticArray.size(); i++) {
			if (staticArray.get(i).getVar().equals(right.getData())) {
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);

				exeArray.add("AD");
				exeArray.add(tempT);
				exeArray.add(tempXX);

			}

		}

		for (int i = 0; i < staticArray.size(); i++) {
			if (staticArray.get(i).getVar().equals(left.getData())) {
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);

				exeArray.add("8D");
				exeArray.add(tempT);
				exeArray.add(tempXX);
			}
		}
	}

	public void loadPrint(TreeNode t) {
		// Load the Y register with the contents of whats in the print statement
		// print(a)
		exeArray.add("AC");
		System.out.println(scopeCounter);
		System.out.println(t.getData());
		for (int i = staticArray.size() - 1; i >= 0; i--) {
			if (staticArray.get(i).getVar().equals(t.getData())) {
				System.out.println("ASDDDDDDDDDDDdd");
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);

				exeArray.add(tempT);
				exeArray.add(tempXX);
				exeArray.add("A2");
				exeArray.add("01");
				exeArray.add("FF");

			}
		}

	}

	public void loadComparision(TreeNode t) {
		// if(a==b)
		// load the X register with the contents of a
		// compare the X register to the contents of b
		exeArray.add("AE");
		for (int i = 0; i < staticArray.size(); i++) {
			// System.out.println(t.getChildren().get(0).getData());
			if (staticArray.get(i).getVar().equals(t.getChildren().get(0).getData())) {
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);

				exeArray.add(tempT);
				exeArray.add(tempXX);

			}

			if (staticArray.get(i).getVar().equals(t.getChildren().get(1).getData())) {
				exeArray.add("EC");

				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);

				exeArray.add(tempT);
				exeArray.add(tempXX);
			}
		}
		exeArray.add("D0");
		exeArray.add("J" + jumpCounter); // This should be determined by some
											// sort of jump
		// counter
		jumpCounter++;

	}

	public void calculateJump() {

		int start = 0;
		int finish = 0;

		for (int i = 0; i < exeArray.size(); i++) {
			if (exeArray.get(i).equals("J" + (jumpCounter - 1)) && !calculatedJumps.contains(jumpCounter - 1)) {
				System.out.println("GOT A JUMP FOR JUMP " + (jumpCounter - 1));
				start = i;
				finish = exeArray.size();
				calculatedJumps.add(jumpCounter - 1);
				String finalValue = Integer.toString(finish - start);
				addJumpEntry("J" + (jumpCounter - 1), finalValue);
			}

		}

		if (finish - start != 0) {
			System.out.println(finish - start);
			for (int i = 0; i < calculatedJumps.size(); i++) {
				System.out.println("Already calculated Jump : " + calculatedJumps.get(i));

			}
		}

	}

	public boolean findIfInIf(TreeNode t) {

		while (t.getData().equals("goal")) {
			if (t.getParent().getData().equals("if") == false) {
				findIfInIf(t.getParent());
			} else {
				System.out.println("STILL IN THE IF STATEMENT");
				return true;
			}

			System.out.println("NOT IN THE IF");
			return false;
		}
		System.out.println("ASD");
		return false;
	}

	public void addJumpEntry(String temp, String distance) {
		JumpTable j = new JumpTable();
		j.setTemp(temp);
		j.setDistance(distance);
		jumpArray.add(j);

	}

	public void printExe() {
		for (int i = 0; i < exeArray.size(); i++) {
			if (i % 8 == 0 && i != 0) {
				System.out.print("\n");

				System.out.print(exeArray.get(i));
				System.out.print(" ");

			} else {

				System.out.print(exeArray.get(i));
				System.out.print(" ");
			}
		}
	}

	public void printStatic() {
		for (int i = 0; i < staticArray.size(); i++) {

			System.out.print("\n");
			System.out.print(staticArray.get(i).getTemp());
			System.out.print(" ");
			System.out.print(staticArray.get(i).getVar());
			System.out.print(" ");
			System.out.print(staticArray.get(i).getAddress());
			System.out.print(" ");
			System.out.print(staticArray.get(i).getScope());
			System.out.print(" ");
			System.out.print(" ");
		}
	}

	public void printJump() {
		for (int i = 0; i < jumpArray.size(); i++) {
			System.out.print("\n");
			System.out.print(jumpArray.get(i).getTemp());
			System.out.print(" ");
			System.out.print(jumpArray.get(i).getDistance());
			System.out.print(" ");
		}
	}

	public int getTempCounter() {
		return tempCounter;
	}

}
