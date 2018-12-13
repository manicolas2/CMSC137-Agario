package agario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/***************************************
	
***************************************/
public class GamePanel extends JPanel implements Constants {
	private static final long serialVersionUID = 1L;
	
	/* ATTRIBUTES */
	JPanel eastPanel;
	GameCanvas gameCanvas;

	// Scores and Chat
	ScoreBoard scoreBoard;
	ChatPanel chatPanel;

	// Window
	GameWindow window;

	// Lobby_id
	String lobby_id;
	
	/* CONSTRUCTOR */
	public GamePanel(GameWindow window) {
		this.window = window;

		// UI
		this.initUI();
	}
	/* METHODS */
	/***************************************
	
	***************************************/
	private void initUI() {
		// Set
		this.setLayout(new BorderLayout());
		this.setFocusable(true);
		this.requestFocusInWindow();

		// 
		this.initEastPanel();
		this.gameCanvas = new GameCanvas(this.chatPanel, this.scoreBoard, this.window);
		
		this.add(this.eastPanel, BorderLayout.LINE_END);
		this.add(this.gameCanvas, BorderLayout.CENTER);

		this.gameCanvas.requestFocusInWindow();
	}

	/***************************************
	Create the panel for the east part of the window.
	This panel will include the scoreboard 
	and the chat.
	***************************************/
	private void initEastPanel() {
		this.eastPanel = new JPanel();
		this.eastPanel.setLayout(new BorderLayout());

		this.scoreBoard = new ScoreBoard();
		this.chatPanel = new ChatPanel(window);
		this.eastPanel.add(this.scoreBoard, BorderLayout.NORTH);
		this.eastPanel.add(this.chatPanel, BorderLayout.SOUTH);
	}
	/***************************************
	
	***************************************/
	public Handler getHandler() {
		return this.gameCanvas.getHandler();
	}

	/***************************************
	
	***************************************/
	public void startGameCanvas(String pname, String ip) {
		this.gameCanvas.start(pname, ip);
		this.gameCanvas.requestFocusInWindow();
	}

}