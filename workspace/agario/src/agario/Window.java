package agario;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends Canvas{
	private static final long serialVersionUID = 1L;
	JPanel panel;
	JFrame frame;
	public Window(int width, int height, String title, Game game) {
		this.frame = new JFrame(title);
		
//		this.panel = new JPanel();
//		JPanel gamePanel = new JPanel();
//		gamePanel.setPreferredSize(new Dimension(750, 750));
//		gamePanel.add(game);
//		
		
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.setVisible(true);
		frame.add(game, BorderLayout.CENTER);
		game.start();
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
}
