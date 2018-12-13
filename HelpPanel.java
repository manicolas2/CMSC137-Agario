package agario;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.File;

/***************************************
This class displays a help page.
***************************************/
public class HelpPanel extends JPanel{
	/* ATTRIBUTES */
	// Background image name
	private String MAINBACKGROUND = "help.png";
	// Store background image
	Image image;
	// Add window access to show menuPanel for back button
	GameWindow window;

	/* CONSTRUCTOR */
	public HelpPanel(GameWindow window){
		// Set
		this.window = window;
		
		// UI
		this.initUI();
	}


	/* METHODS */

	/***************************************
	This function is called to load the UI
	for this class.
	***************************************/
	private void initUI() {
		// Set
		this.setOpaque(false);
		this.setLayout(null);

		// Import background image
		try{
			image=ImageIO.read(new File(MAINBACKGROUND));
		}catch(Exception e){}

		// Add back button
		JButton back = GameWindow.createButton(30,139,100,80);
		this.add(back);
		// Add back button listener
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Show main menu
				window.showMenuPanel();
			}
		});
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.image,0,0,null);
		Toolkit.getDefaultToolkit().sync();
	}
}
