
public class Token {
	private String type;
	private int position;
	
	public Token(){
		type = null;
		position = 0;
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
