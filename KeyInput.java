package agario;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{

	private Handler handler;
	
	public KeyInput(Handler handler) {
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) handler.player.setSpeedY(-5);
		if(key == KeyEvent.VK_S) handler.player.setSpeedY(5);
		if(key == KeyEvent.VK_D) handler.player.setSpeedX(5);
		if(key == KeyEvent.VK_A) handler.player.setSpeedX(-5);
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(1); 
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W) handler.player.setSpeedY(0);
		if(key == KeyEvent.VK_S) handler.player.setSpeedY(0);
		if(key == KeyEvent.VK_D) handler.player.setSpeedX(0);
		if(key == KeyEvent.VK_A) handler.player.setSpeedX(0);
	}
	
}
