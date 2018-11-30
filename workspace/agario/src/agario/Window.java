package agario;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends Canvas{
	private static final long serialVersionUID = 1L;
	

	public Window(int width, int height, String title, Game game) {
		JFrame frame = new JFrame(title);
//		JPanel gamePanel = new JPanel();
		JPanel chatPanel = new JPanel();
		ChatPanel chat = new ChatPanel();
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setLayout(new BorderLayout());
		
		chatPanel.add(chat);
//		gamePanel.add(game);
	
		frame.add(game,BorderLayout.CENTER);
		frame.add(chatPanel,BorderLayout.LINE_END);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		
		frame.setVisible(true);
		game.start();
	}
	
}
