import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	//Keywords
	private static String intPattern = "(int)";
	private static String stringPattern = "(string)";
	private static String booleanPattern = "(boolean)";
	private static String ifPattern = "(if)";
	private static String whilePattern = "(while)";
	private static String printPattern = "(print)";
	

	
	
	
	private static String assign = "(=)";
	private static String digit = "(\\d)";
	private static String alpha = "([a-z])";
	private static String space = "(\\s)";
	private static String leftParen = "(()";
	private static String rightParen = "())";
	private static String leftBrace = "({)";
	private static String rightBrace = "(})";
	private static String quote = "(\")";
	
	private static String equality = "(==)";
	private static String notEqual = "(!=)";
	private static String intOp = "(+)";
	
	//private static String test = "(int)|([a-z])";
	
	private static ArrayList<Character> charArray = new ArrayList<Character>();
	private static ArrayList<Token> tokenArray = new ArrayList<Token>();
	private static ArrayList<String> validArray = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {
		FileInputStream fileInput = new FileInputStream("C:/Users/Chris/Desktop/test.txt");
		
		

		ArrayList<Character> charArray = new ArrayList<Character>();

		int r;
		while ((r = fileInput.read()) != -1) {
			char c = (char) r;

			System.out.println(c);
			charArray.add(c);

		}
		//HOW TO LOOP THIS
		
		testForSpace(charArray);
		System.out.println(analyzeList(charArray));
		testString(analyzeList(charArray));
		//makeTokens(testArray);
		//makeTokens(validArray);
		//setTokenPos(validArray);
		//System.out.println(tokenArray);
		System.out.println("Here are the valid characters that will be analyzed " + validArray);
		for(int i = 0; i < tokenArray.size(); i ++){
		
			//System.out.println("Token " + i + " : " + tokenArray.get(i).getValue());
			System.out.println("Token " + i + " : " + tokenArray.get(i).getType());
		}
	
		//System.out.println(tokenArray.get(2).getType());
		
		fileInput.close();
		
		
	}

	// Iterates through arraylist creating a new array list of characters
	// Until a space is found. (May have to fix how it recognizes a space)
	public static void testForSpace(ArrayList<Character> x) {
		// ArrayList<Character> intArray = new ArrayList<Character>();
		// Pattern p = Pattern.compile(space);
		int counter = 1;
		for (int i = 0; i < x.size(); i++) {
			
			

			if (x.get(i) != ' ') {

				charArray.add(x.get(i));

			}
			
			if(x.get(i) == '\n'){
				System.out.println("ON LINE " + counter);
				
				counter++;
				//intArray.add(x.get(i));
				
			}

			if (x.get(i) == ' ') {
				System.out.println("Valid characters");
				System.out.println(charArray);
				
				
			}
		}
	}

	// Takes in the array list created in testInt
	// Converts it to a string
	public static String analyzeList(ArrayList<Character> list) {
		StringBuilder builder = new StringBuilder(list.size());
		for (Character ch : list) {
			builder.append(ch);
		}
		return builder.toString();
	}

	// Test the newly formed string against regex and build tokens
	// Remember longest match
	public static ArrayList<String> testString(String s) {
		
		/*private static String intPattern = "(int)";
		private static String stringPattern = "(string)";
		private static String booleanPattern = "(boolean)";
		private static String ifPattern = "(if)";
		private static String whilePattern = "(while)";
		private static String printPattern = "(print)";
		
		
		
		private static String assign = "(=)";
		private static String digit = "(\\d)";
		private static String alpha = "([a-z])";
		private static String space = "(\\s)";
		private static String leftParen = "(()";
		private static String rightParen = "())";
		private static String leftBrace = "({)";
		private static String rightBrace = "(})";
		private static String quote = "(\")";
		
		private static String equality = "(==)";
		private static String notEqual = "(!=)";
		private static String intOp = "(+)";*/
		
	
		
		Pattern r2 = Pattern.compile(printPattern + "|" + whilePattern + "|" + intPattern + "|" + ifPattern + "|" + stringPattern + "|" + booleanPattern + "|" + equality +  
				 "|" + notEqual  + "|"  + alpha);
		
		
		Matcher m2 = r2.matcher(s);
		
		Token token;
		int counter = 0;
		boolean found = false;
		
		while(m2.find()){
			while(counter < 11 && !found){
				if(m2.group(counter) != null){
					validArray.add(m2.group());
					System.out.println("valid array");
					System.out.println(validArray);
					tokenArray.add(token = new Token(counter, m2.group(counter), 1));
					found = true;
				}
				counter++;
				
			}
			counter = 1;
			found = false;
		}
		//return tokenArray;
		return validArray;
	};

		
		
	//Need to figure out how to add line number. 
		/*while(m2.find()){
			Token token = new Token();
			//token.setType("keyword");
			token.setType("chicken");
			tokenArray.add(token);
			validArray.add(m2.group());
			System.out.println(validArray);
		}
		return validArray;
	} //This chunk of code currently works but not perfect
		
	
	
	//take in the testArray and tokenize it
	//Might have to combine this method with testString so when m.find() add the token immediatley 
	
	/*public static ArrayList<Token> makeTokens(ArrayList<String> a){
		for(int i = 0; i < a.size(); i++){
			Token token = new Token();
			token.setPosition(i);
			token.setType(a.get(i));
			tokenArray.add(token);
		}
		return tokenArray;
	}*/
	
	//Idea of this is to read in the token list and analyze what kind of token it is
	public static void analyzeTokenList(ArrayList<Token> t){
		for(int i = 0; i < t.size(); i++){
			if(t.get(i)){
				
			}
		}
	}
	

}