package agario;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/***************************************
This class is a JFrame where all instances 
of JPanel are added.
Uses CardLayout for showing different panels.
***************************************/
public final class GameWindow extends JFrame implements Constants {

	/* ATTRIBUTES */

	// Layout for handling panels
	CardLayout cardLayout;
	// Holds all panels
	JPanel panelHolder;
	// Game over
	JPanel gameOver;
	// MenuPanel
	MenuPanel menuPanel;
	// HelpPanel
	HelpPanel helpPanel;
	// GamePanel
	GamePanel gamePanel;

	// Chat module
	ChatModule chatModule;

	/* CONSTRUCTOR */
	public GameWindow() {
		// Set
		this.setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		// Instantiate card layout
		this.cardLayout = new CardLayout();
		// Implement card layout in panelHolder
		this.panelHolder = new JPanel(this.cardLayout);

		// Instantiate panels
		this.menuPanel = new MenuPanel(this);
		this.helpPanel = new HelpPanel(this);
		this.gamePanel = new GamePanel(this);
		
		// Add panels to panelHolder
		this.panelHolder.add(this.menuPanel, "Menu");
		this.panelHolder.add(this.helpPanel, "Help");
		this.panelHolder.add(this.gamePanel, "Game");

		// Show the menuPanel at the start of the game
		this.showMenuPanel();

		// Show
		this.setContentPane(panelHolder);
		this.setFocusable(true);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/* METHODS */


	/***************************************
	This function is called to show a MenuPanel
	**************************************/
	public void showMenuPanel(){
		this.cardLayout.show(this.panelHolder, "Menu");
	}


	/***************************************
	This function is called to show a HelpPanel
	**************************************/
	public void showHelpPanel(){
		this.cardLayout.show(this.panelHolder, "Help");
	}

	/***************************************
	
	**************************************/
	public void showGamePanel(String pname, String ip){
		this.chatModule.setGamePanel(this.gamePanel);
		this.cardLayout.show(this.panelHolder, "Game");
		this.gamePanel.startGameCanvas(pname, ip);
	}

	/***************************************
	
	**************************************/
	public void setChatModule(ChatModule chatModule) {
		this.chatModule = chatModule;
	}

	/***************************************
	
	**************************************/
	public ChatModule getChatModule() {
		return this.chatModule;
	}

	/***************************************
	
	**************************************/
	public GamePanel getGamePanel() {
		return this.gamePanel;
	}


	/***************************************
	This static function is called to create a button 
	with an image as a background.
	The parameters are the bounds for the Jbutton.
	**************************************/
	public static JButton createButton(int bound1, int bound2, int bound3, int bound4) {
		JButton button = new JButton();
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setLayout(null);
		button.setBounds(bound1, bound2, bound3, bound4);
		return button;
	}
}
