package agario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Blob extends JPanel implements Runnable{

	JFrame frame = new JFrame();
	Thread t = new Thread(this);
	BufferedImage offscreen;
	int x, y;
	final static int RIGHT_END = 700;
	final static int LEFT_END = 0;
	final static int TOP_END = 0;
	final static int DOWN_END = 400;

	/*Basic constructor for Blob*/
	public Blob(){
		this.x = 25;
		this.y = 25;
		frame.setTitle("AGARIO");
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(new KeyListener(){ //listen to the keyAction moved by the user
			public void keyPressed(KeyEvent ke){
			if(ke.getKeyCode()==KeyEvent.VK_W){
				goUp();
			}
			if(ke.getKeyCode()==KeyEvent.VK_A){
				goLeft();
			}
			if(ke.getKeyCode()==KeyEvent.VK_S){
				goDown();
			}
			if(ke.getKeyCode()==KeyEvent.VK_D){
				goRight();
			}
			}
			public void keyTyped(KeyEvent ke){}
			public void keyReleased(KeyEvent ke){}
		});
		frame.setSize(750,500);
		frame.setFocusable(true);
		frame.setVisible(true);
		t.start();
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.fillOval(x,y,50,50);
	}
	
	public void goUp(){
		if(this.y != TOP_END){
			this.y -= 5;
		}
	}
	public void goDown(){
		if(this.y != DOWN_END){
			this.y += 5;
		}
	}
	public void goLeft(){
		if(this.x != LEFT_END){
			this.x -= 5;
		}	
	}
	public void goRight(){
		if(this.x != RIGHT_END){
			this.x += 5;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception e){}
			
			
			frame.revalidate();
			frame.repaint();
		}
	}

}
