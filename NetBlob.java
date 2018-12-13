package agario;

import java.net.InetAddress;

/***************************************
Class for representing the players/blobs
in the UDP server
***************************************/
public final class NetBlob {
	/* ATTRIBUTES */
	private InetAddress address;
	private int port;
	private String name;
	private int x, y, size, score;
	private String id;
	private boolean dead;
		
	/* CONSTRUCTOR */
	public NetBlob(String name, InetAddress address, int port) {
		this.address = address;
		this.port 	 = port;
		this.name 	 = name;
		this.dead    = false;
	}
	
	/* METHODS */

	/***************************************
	Setters
	***************************************/
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	public void setDead(){
		this.dead = true;
	}

	/***************************************
	Getters
	***************************************/
	public InetAddress getAddress() {
		return this.address;
	}

	public int getPort() {
		return port;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public boolean getDead(){
		return this.dead;
	}
	
	/***************************************
	Converts a NetBlob to its string representation
	***************************************/
	public String convertToString(){
		String string = "";

		string += id + "=";
		string += name + "=";
		string += x + "=";
		string += y + "=";
		string += size + "=";
		string += score;

		return string;
	}
}
