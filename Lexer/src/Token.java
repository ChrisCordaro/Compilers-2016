
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
			type = "booleanWord";
			break;
		case 7:
			type = "equality";
			break;
		case 8:
			type = "nonEquality";
			break;
		case 9:
			type = "alpha";
			break;
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
