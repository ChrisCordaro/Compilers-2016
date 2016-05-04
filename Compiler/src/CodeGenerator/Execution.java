package CodeGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Tree.TreeNode;

public class Execution {
	private ArrayList<String> exeArray = new ArrayList<String>();
	private ArrayList<StaticTable> staticArray = new ArrayList<StaticTable>();
	
	
	private int tempCounter = -1;
	private int staticCounter = 0;
	private String staticTemp = "T" + getTempCounter() + "XX";

	public void loadEnviornment(TreeNode t) {
		Iterator iter = t.getChildren().iterator();
		if (t.getData().equals("varDecl")) {
			tempCounter++;
			if (t.getChildren().get(0).getData().equals("intWord")) {

				// generate code to initialize ints to 0
				// load accumulator with 0
				// A9 00 8D T0 XX
				addExeEntry("A9", "00", "8D", "XX");
				addStaticEntry(t.getChildren().get(1).getData(), "idk");
				staticCounter++;

			} else if (t.getData().equals("booleanWord")) {
				addExeEntry("A9", "00", "8D", "XX");
				addStaticEntry(t.getChildren().get(1).getData(), "idk");
			}
		} else if (t.getData().equals("assignStatement")) {
			//Anything BUT comparing one variable to another
			if (t.getChildren().get(1).getData().startsWith("\"") || t.getChildren().get(1).getData().matches("(true)")
					|| t.getChildren().get(1).getData().matches("(false)")
					|| t.getChildren().get(1).getData().matches("(\\d)")) {
				addExeEntry("A9", t.getChildren().get(1).getData(), "8D", "XX");
		
			}else{
				//Comparing two variable
				//Load accumulator with contents of the second value 
				//Store the accumulator in the address for the first one which
				
				System.out.println("YOOOOOOOOOOOoo");
				twoVarAssign(t.getChildren().get(0), t.getChildren().get(1));
			}
		}else if(t.getData().equals("print")){
			//printing a literal
			if (t.getChildren().get(0).getData().startsWith("\"") || t.getChildren().get(0).getData().matches("(true)")
					|| t.getChildren().get(0).getData().matches("(false)")
					|| t.getChildren().get(0).getData().matches("(\\d)")){
				
			}else{
				//printing a var
				
				loadPrint(t.getChildren().get(0));
			}
		}

		while (iter.hasNext())

		{

			loadEnviornment((TreeNode) iter.next());
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
	

	public void addStaticEntry(String var, String add) {
		StaticTable t = new StaticTable();
		staticArray.add(t);
		String tempLocal = "T" + tempCounter + "XX";
		//staticArray.add(tempLocal);
		staticArray.get(staticCounter).setVar(var);
	    staticArray.get(staticCounter).setTemp(tempLocal);
		staticArray.get(staticCounter).setAddress(add);
	}
	
	public void twoVarAssign(TreeNode left, TreeNode right){
		String leftAddress;
		String rightAddress;
		
		for(int i = 0; i < staticArray.size(); i++){
			if(staticArray.get(i).getVar().equals(right.getData())){
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);
				
				exeArray.add("AD");
				exeArray.add(tempT);
				exeArray.add(tempXX);
				
				
			}
			
			
		}
		
		for(int i = 0; i < staticArray.size(); i++){
			if(staticArray.get(i).getVar().equals(left.getData())){
				String tempT = staticArray.get(i).getTemp().substring(0, 2);
				String tempXX = staticArray.get(i).getTemp().substring(2, 4);
				
				exeArray.add("8D");
				exeArray.add(tempT);
				exeArray.add(tempXX);
			}
		}
	}
	
	public void loadPrint(TreeNode t){
		//Load the Y register with the contents of whats in the print statement print(a)
		exeArray.add("AC");
		for(int i = 0; i < staticArray.size(); i ++){
			if(staticArray.get(i).getVar().equals(t.getData())){
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
			if (i % 3 == 0 && i != 0) {
				System.out.print("\n");
				System.out.print(staticArray.get(i).getTemp());
				System.out.print(" ");
			} else {
				System.out.print(staticArray.get(i).getTemp());
				System.out.print(" ");
				System.out.print(staticArray.get(i).getVar());
				System.out.print(" ");
				System.out.print(staticArray.get(i).getAddress());
				System.out.print(" ");
			}
		}
	}

	public int getTempCounter() {
		return tempCounter;
	}

}
