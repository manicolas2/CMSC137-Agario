package agario;

import java.lang.Integer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import java.io.File;

/***************************************
The class MenuPanel is a JPanel that shows
the options that the user can do when starting 
the game.
An instance of the MenuPanel is shown at the 
start of the game.
***************************************/
public class MenuPanel extends JPanel {
	/* ATTRIBUTES */

	// Name of background image
	private String MAINBACKGROUND = "background.png";
	// Stores background image
	Image image;
	// Window that has all the panels
	private GameWindow window;
	
	/* CONSTRUCTOR */
	public MenuPanel(GameWindow window){
		// Set window
		this.window = window;

		// UI
		this.initUI();

		this.requestFocusInWindow();
	}

	/* METHODS */

	/***************************************
	This function loads the UI for this class
	***************************************/
	private void initUI() {
		this.setOpaque(false);
		this.setLayout(null);

		// Import background image
		try{
			image=ImageIO.read(new File(MAINBACKGROUND));
		}catch(Exception e){}

		// Initialize buttons
		this.initButtons();
		this.requestFocusInWindow();
	}

	/***************************************
	This function is called to create the buttons 
	for this class.
	**************************************/
	private void initButtons() {
		// Create JButtons
		JButton create_game = GameWindow.createButton(424,282,225,75);
		JButton join_game = GameWindow.createButton(424,362,225,75);
		JButton help = GameWindow.createButton(424,444,225,75);
		JButton exit = GameWindow.createButton(424,531,225,75);
		
		// Add buttons to panel
		this.add(create_game);
		this.add(join_game);
		this.add(help);
		this.add(exit);

		// Add ActionListeners
		create_game.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Show a modal for the creating a game
				showCreateModal();
			}
		});
		
		join_game.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Show a modal for joining a game
				showJoinModal();
			}
		});
		
		help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Show helpPanel
				window.showHelpPanel();
			}
		});
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Close window
				dispose();
			}
		});
	}

	/***************************************
	This function is called to create a modal
	for the Create Game button.
	***************************************/
	private void showCreateModal() {
		// Create game modal
		final JDialog createModal = new JDialog(window, "Create Game");
		Container modalContainer = createModal.getContentPane();
		modalContainer.setLayout(new BorderLayout());
		
		// Panel for input
		JPanel formPanel = new JPanel(new GridLayout(2,2));
		// Add label for entering name
		formPanel.add(new JLabel("Enter name: "));
		// Add textfield for name input
		JTextField name = new JTextField();
		formPanel.add(name);
		// Add label for entering max players
		formPanel.add(new JLabel("Enter number of players: "));
		// Add textfield for maxplayer input
		JTextField maxPlayer = new JTextField();
		formPanel.add(maxPlayer);

		// Create button for creating the game
		JButton finalCreate = new JButton("Host Game");

		//Add listener to creating the game
		finalCreate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				// Get inputs
				String pnameInput = name.getText();
				String maxInput	  = maxPlayer.getText();

				// Dispose modal
				createModal.dispose();
				createGame(Integer.parseInt(maxInput), pnameInput);
			}
		});

		// Add panel and button to container of modal
		modalContainer.add(formPanel, BorderLayout.PAGE_START);
		modalContainer.add(finalCreate, BorderLayout.PAGE_END);

		// Show modal
		createModal.setLocationRelativeTo(null);
		createModal.pack();
		createModal.setVisible(true);
	}

	/***************************************
	This function is called to create and show 
	a modal for the Join Game button.
	***************************************/
	private void showJoinModal() {
		// Join game modal
		final JDialog joinModal = new JDialog(window, "Join Game");
		Container modalContainer = joinModal.getContentPane();
		modalContainer.setLayout(new BorderLayout());
		
		// Panel for inputs
		JPanel formPanel = new JPanel(new GridLayout(2,2));

		// Add label for name
		formPanel.add(new JLabel("Enter name"));
		// Add textfield for input name
		JTextField name = new JTextField();
		formPanel.add(name);
		// Add label for ip
		formPanel.add(new JLabel("Enter IP Address"));
		//Add textfield for ip
		JTextField ipAddress = new JTextField();
		formPanel.add(ipAddress);
		// Create join button
		JButton finalJoin = new JButton("Join Game"); 
		// Add listener to join button
		finalJoin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				// Get inputs
				String pnameInput = name.getText();
				String ipInput = ipAddress.getText();

				//Dispose modal
				joinModal.dispose();
				
				// Set chat module
				window.setChatModule(new ChatModule("202.92.144.45", 80, pnameInput));
				
				// Start client and show game panel
				window.showGamePanel(pnameInput, ipInput);
			}
		});
		// Add panel and button to container of modal
		modalContainer.add(formPanel, BorderLayout.PAGE_START);
		modalContainer.add(finalJoin, BorderLayout.PAGE_END);
		// Show modal
		joinModal.setLocationRelativeTo(null);
		joinModal.pack();
		joinModal.setVisible(true);	
	}

	/***************************************
	This function is for the modal for creating
	a game.
	This function starts a GameServer and creates a 
	GamePanel
	***************************************/
	private void createGame(int maxInput, String pnameInput) {
		// Create a chat lobby 
		window.setChatModule(new ChatModule("202.92.144.45", 80, pnameInput));
		String lobby_id;
		if(maxInput > 3) lobby_id = window.getChatModule().createALobby(maxInput);
		else lobby_id = window.getChatModule().createALobby(3);
		
		// Start server
		new GameServer(maxInput, lobby_id);

		// Start client and show gamePanel
		window.showGamePanel(pnameInput, "localhost");
	}

	/***************************************
	
	***************************************/
	private void dispose() {
		// TODO Auto-generated method stub
		((Window) this.getTopLevelAncestor()).dispose();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.image,0,0,null);
		Toolkit.getDefaultToolkit().sync();
	}
}