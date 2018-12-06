package agario;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Game extends Canvas implements Runnable{
	// variables and constants
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1050;
	public static final int HEIGHT = 750;
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private Random r;
	private Blob player;
	private GameClient gameClient;
	/*
	 The game constructor, it creates the window and the objects needed
	 in the game such as the blob and the food.
	 */
	
	public Game() {
		Window gameWindow = new Window(WIDTH, HEIGHT, "Agario", this);	

		this.gameClient = new GameClient(0, 0, "jem", this);
		
		handler = new Handler();
		r = new Random();
		this.addKeyListener(new KeyInput(handler));
		this.player = new Blob(r.nextInt(1050), r.nextInt(750), ID.Blob, handler);
		handler.player = this.player;
		
		this.requestFocusInWindow(true); // this will make the game focus on the window for interaction
		// add food
//		while(true) {
//			r = new Random();
//			try {
//				Thread.sleep(r.nextInt(200));
//			}catch(Exception e){
//				System.out.println(e);
//            }
//			
//			if(r.nextInt(10) == 5){
//                handler.addFood(new Food(r.nextInt(WIDTH), r.nextInt(HEIGHT), ID.Food));
//            }
//		}
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000 / amountofTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
				if(this.gameClient.getConnected()) 
					this.gameClient.sendMessage("MOVE " + this.handler.player.convertToString());
			}
			if(running)
				render();
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
		stop();
	}
	
	private void tick() {
		handler.tick();
	}
	
	/*
	 UI Stuff
	 */
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.yellow);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	/*
	 This method clamps the screen, preventing objects to go over the borders of the window
	 */
	public static int clamp(int var, int min, int max) {
		if(var >= max) return var = max;
		else if(var <= min) return var = min;
		else return var;
	}
	
	public Blob getPlayer() {
		return this.player;
	}
	
	public Handler getHandler() {
		return this.handler;
	}
	public static void main(String args[]) {
		new Game();
	}
	
	
	
}
