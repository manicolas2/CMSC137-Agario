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

/***************************************
	
***************************************/
public class GameCanvas extends Canvas implements Runnable, Constants {
	/* ATTRIBUTES  */
	private Thread thread;
	private boolean running = false;
	private Handler handler;
	private Random r = new Random();
	private Blob player;
	private GameClient gameClient;
	private ChatPanel chatPanel;
	private GameWindow window;
	private String name;
	private String id;
	private ScoreBoard scoreBoard;
	/* CONSTRUCTOR */
	
	/***************************************
	The game constructor, it creates the window and the objects needed
	 in the game such as the blob and the food.
	***************************************/
	public GameCanvas(ChatPanel chatPanel, ScoreBoard scoreBoard, GameWindow window) {
		this.chatPanel = chatPanel;
		this.window = window;
		this.scoreBoard = scoreBoard;
	}

	/* METHODS */
	
	/***************************************
	Start thread
	***************************************/
	public void start(String pname, String ip) {
		this.name = pname;
		this.id = null;
		
		// Create handler for blobs and foods
		this.handler = new Handler();

		// Start game client
		this.gameClient = new GameClient(0, 0, this.name, this, this.chatPanel, ip);
		this.gameClient.setScoreBoard(this.scoreBoard);
		
		// Add keylistener
		this.addKeyListener(new KeyInput(handler));
		this.setFocusable(true);
		this.requestFocusInWindow(); 
	
		// Create player
		this.player = new Blob(r.nextInt(500), r.nextInt(500), ID.Blob, handler, pname);
		handler.player = this.player;
		this.player.setGameClient(this.gameClient);
		// Start Thread
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	/***************************************
	
	***************************************/
	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************
	
	***************************************/
	public void setRunning(boolean running) {
		this.running = running;
	}

	/***************************************
	
	***************************************/
	public void setId(String id) {
		this.id = id;
		this.player.setBlobId(id);
	}

	/***************************************
	
	***************************************/
	public Blob getPlayer() {
		return this.player;
	}

	/***************************************
	
	***************************************/
	public Handler getHandler() {
		return this.handler;
	}


	public GameWindow getWindow(){
		return this.window;
	}
	
	/***************************************
	This method clamps the screen, preventing objects to go over the borders of the window
	***************************************/
	public static int clamp(int var, int min, int max) {
		if(var >= max) return var = max;
		else if(var <= min) return var = min;
		else return var;
	}
	
	/***************************************
	***************************************/
	private void tick() {
		handler.tick();
	}

	/***************************************
	UI Stuff
	***************************************/
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.yellow);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		
		handler.render(g);

		g.dispose();
		bs.show();
	}
	
	/***************************************
	
	***************************************/
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountofTicks = 60.0;
		double ns = 1000000000 / amountofTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();

		while(this.player.dead == false) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while(delta >= 1) {
				tick();
				delta--;

				// Send update to server
				if(this.gameClient.getHasId() && this.player.dead == false) 
					this.gameClient.sendMessage("MOVE " + this.handler.player.convertToString());
			}
			if(running)
				render();
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
		BufferStrategy bs = this.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		g.dispose();
		bs.show();
		
		this.stop();


	}
	
}
