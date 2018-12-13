package agario;

/***************************************
Food representation in UDP server
***************************************/
public final class NetFood {
	/* ATTRIBUTES */
	private int x, y;
	
	/* CONSTRUCTOR */
	public NetFood(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/* METHODS */
	/***************************************
	Convert NetFood to itsstring representation
	***************************************/
	public String convertToString() {
		String string = "";
		string += (x + "=");
		string += y;
		return string;
	}
}
