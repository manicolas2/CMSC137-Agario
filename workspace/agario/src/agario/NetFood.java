package agario;

public final class NetFood {
	private int x, y;
	
	public NetFood(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String convertToString() {
		String string = "";
		string += x + "=" + y;
		return string;
	}
}
