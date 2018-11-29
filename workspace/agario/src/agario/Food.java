package agario;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Food extends GameObject{
	public Color c;
	
	public Food(int x, int y, ID id){
		super(x, y, id);
		
		Random rand = new Random();
		int r = rand.nextInt(255);
		int green = rand.nextInt(255);
		int b = rand.nextInt(255);
		this.c = new Color(r,green,b);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 10, 10);
		
	}
	
	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(c);
		g.fillOval(this.x, this.y, 10, 10);
	}
}
