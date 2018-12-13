package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/***************************************
Class that represents the foods	
***************************************/
public class Food extends GameObject{
	/* ATTRIBUTES */
	public Color c;
	
	/* CONSTRUCTOR */
	public Food(int x, int y, ID id){
		super(x, y, id);
		
		Random rand = new Random();
		int r = rand.nextInt(255);
		int green = rand.nextInt(255);
		int b = rand.nextInt(255);
		this.c = new Color(r,green,b);
	}

	/* METHODS */
	/***************************************
	
	***************************************/
	public Rectangle getBounds() {
		return new Rectangle(x, y, 10, 10);
		
	}

	/***************************************
	
	***************************************/
	@Override
	public void render(Graphics g) {
		g.setColor(c);
		g.fillOval(this.x, this.y, 10, 10);
	}
}
