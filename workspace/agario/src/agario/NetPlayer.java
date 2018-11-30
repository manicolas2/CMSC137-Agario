package agario;

import java.net.InetAddress;

public final class NetPlayer {
	// Attributes
	private InetAddress address;
	private int port;
	private String name;
	private int x, y;
	
	// Constructor
	public NetPlayer(String name, InetAddress address, int port) {
		this.address = address;
		this.port 	 = port;
		this.name 	 = name;
	}
	
	// Methods
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
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public String toString(){
		String retval="";
		retval += "PLAYER ";
		retval += name + " ";
		retval += x + " ";
		retval += y;
		return retval;
	}
}
