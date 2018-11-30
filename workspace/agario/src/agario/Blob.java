package agario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Blob extends GameObject implements Runnable{
	public static int size = 0;
	public static int score = 0;
	Handler handler;
	
	/*Basic constructor for Blob*/
	public Blob(int x, int y, ID id, Handler handler){
		super(x, y, id);
		this.handler = handler;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, 50+size, 50+size);
		
	}
	
	public void tick() {
		x += speedX;
		y += speedY;
		
		x = Game.clamp(x, 0, 750 - (50 + size));
		y = Game.clamp(y, 0, Game.HEIGHT -(80 + size));
		
		collision();
	}
	
	/*
	 Method for checking collision of blob and food
	 */
	private void collision() {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getID() == ID.Food) {
				if(getBounds().intersects(tempObject.getBounds())) {
					size++;
					score++;
					handler.removeObject(tempObject);
					
					System.out.println(size);
				}
			}
		}
	}
	/*
	 UI Stuff
	 */
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(x, y, 50+size, 50+size);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
