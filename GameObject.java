package agario;

import java.awt.Graphics;
import java.awt.Rectangle;

/***************************************
Superclass for Blob and Food
***************************************/
public abstract class GameObject {
	/* ATTRIBUTES */
	protected int x, y;
	protected ID id;
	protected int speedX, speedY;
	protected int netIndex;
	
	/* CONSTRUCTOR */
	public GameObject(int x, int y, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	/* METHODS */

	/***************************************
	Abstract functions
	***************************************/
	public abstract void render(Graphics g);
	public abstract Rectangle getBounds();
	
	/***************************************
	Setters
	***************************************/
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setID(ID id) {
		this.id = id;
	}
	public void setNetIndex(int index) {
		this.netIndex = index;
	}
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	/***************************************
	Getters
	***************************************/
	public int getX() {
		return x;
	}
	public int geTY() {
		return y;
	}
	public ID getID() {
		return id;
	}
	public int getSpeedX() {
		return speedX;
	}
	public int getSpeedY() {
		return speedY;
	}
	public int getNetIndex() {
		return this.netIndex;
	}
}
 