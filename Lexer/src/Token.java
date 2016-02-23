
public class Token {
	private String type;
	private int position;
	private String value;
	
	public Token(){
		type = null;
		position = 0;
	}
	//group is the group that the matcher has successfully matched on defined int
	public Token(int group, String tokenVal, int lineNum){
		value = tokenVal;
		position = lineNum;
		createToken(group);
	}
	
	public void createToken(int group){
		switch(group){
		case 1:
			type = "printWord";
			break;
		case 2:
			type = "whileWord";
			break;
		case 3:
			type = "intWord";
			break;
		case 4:
			type = "ifWord";
			break;
		case 5:
			type = "stringWord";
			break;
		case 6:
			type = "false";
			break;
		case 7:
			type = "true";
			break;
		case 8:
			type = "booleanWord";
			break;
		case 9:
			type = "alpha/ID";
			break;
		case 10:
			type = "boolOp";
			break;
		case 11:
			type = "boolOp";
			break;
		case 12:
			type = "assign";
			break;
		case 13:
			type = "leftParen";
			break;
		case 14:
			type = "rightParen";
			break;
		case 15:
			type = "digit";
			break;
		case 16:
			type = "leftBrace";
			break;
		case 17:
			type = "rightBrace";
			break;
		case 18:
			type = "quote";
			break;
		case 19:
			type = "intOp";
			break;
		case 20:
			type = "endProgram";
		}
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Token(String s, int x){
		type = s;
		position = x;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
