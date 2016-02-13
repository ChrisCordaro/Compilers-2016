import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	private static String intPattern = "(int)";
	private static String alpha = "([a-z])";
	private static String space = "(\\s)";
	
	private static String test = "(int)|([a-z])";
	
	private static ArrayList<Character> intArray = new ArrayList<Character>();
	private static ArrayList<Token> tokenArray = new ArrayList<Token>();
	private static ArrayList<String> testArray = new ArrayList<String>();
	
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
		System.out.println(analyzeList(intArray));
		testString(analyzeList(intArray));
		//makeTokens(testArray);
		makeTokens(testArray);
		for(int i = 0; i < tokenArray.size(); i ++){
			System.out.println("Token " + i + " : " + tokenArray.get(i).getType());
		}
	
		
		fileInput.close();
		
		

	}

	// Iterates through arraylist creating a new array list of characters
	// Until a space is found. (May have to fix how it recognizes a space)
	public static void testForSpace(ArrayList<Character> x) {
		// ArrayList<Character> intArray = new ArrayList<Character>();
		// Pattern p = Pattern.compile(space);
		for (int i = 0; i < x.size(); i++) {

			if (x.get(i) != ' ') {

				intArray.add(x.get(i));

			}

			if (x.get(i) == ' ') {
				System.out.println(intArray);

				break;
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
		
		Pattern r = Pattern.compile(intPattern);
		Matcher m = r.matcher(s);
		
		Pattern r1 = Pattern.compile(alpha);
		Matcher m1 = r1.matcher(s);
		
		Pattern r2 = Pattern.compile(test);
		Matcher m2 = r2.matcher(s);
		
		
	
		if(m.find()){
			System.out.println(m);
			System.out.println("I FOUND AN INT");
			//testArray.add(m.group());
			System.out.println(testArray);
		
		}
		if(m1.find()){
			System.out.println(m1);
			System.out.println("I FOUND AN ALPHA");
			//testArray.add(m1.group());
			System.out.println(testArray);
		}
		
		//in the example of xint this will find x and stop looking at the string
		//Figure out a way to get this to loop and not look at the thing already found
		/*if(m2.find()){
			System.out.println("MATCH INT/CHAR FOUND");
			
			testArray.add(m2.group());
			System.out.println(testArray);
		}*/
		while(m2.find()){
			testArray.add(m2.group());
			System.out.println(testArray);
		}
		return testArray;
	}
	
	
	//take in the testArray and tokenize it
	public static ArrayList<Token> makeTokens(ArrayList<String> a){
		for(int i = 0; i < a.size(); i++){
			Token token = new Token();
			token.setPosition(i);
			token.setType(a.get(i));
			tokenArray.add(token);
		}
		return tokenArray;
	}

}