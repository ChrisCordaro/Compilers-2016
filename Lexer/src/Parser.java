import java.util.ArrayList;

public class Parser {
	
	public static void Parser(){
		
	}
	public static void parseProgram(){
		
		parseBlock(ArrayList<Token> t);
	}
	
	public static void parseBlock(ArrayList<Token> t){
		
		if(t.get(1).getType() == "leftBrace"){
			System.out.println("LEFT BRACE FOUND");
		}else{
			System.out.println("ERROR ON PARSEBLOCK, NO LEFT BRACE FOUND");
		}
	}
	
	

}
