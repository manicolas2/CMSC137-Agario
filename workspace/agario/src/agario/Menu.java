package agario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Menu extends JFrame{
	Container contentPane;
	private static final int PADDING = 100;
	private JPanel titlePanel;
	private JPanel buttonsPanel;
	private JLabel titleLabel;
	private JPanel[] buttonInPanel = new JPanel[4];
	private JButton[] menuButtons = new JButton[4];
	
	
	public Menu() {
		super("Agario");
		this.setLayout(new GridLayout(2,1));
		this.setPreferredSize(new Dimension(1600,800));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		contentPane = this.getContentPane();	
//		contentPane.setBackground(Color.decode("#0C0000"));
//		
		//outer panels
		titlePanel = new JPanel(new BorderLayout());
		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		contentPane.add(titlePanel);
		contentPane.add(buttonsPanel);
		
		//title panel
		titleLabel = new JLabel();
	    URL imgUrl = Menu.class.getResource("title_image.jpg");
//		titleLabel.setIcon(new ImageIcon(imgUrl));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVerticalAlignment(JLabel.CENTER);
//		titlePanel.setBackground(Color.decode("#0C0000"));
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		
		//buttons panel
		for(int i=0; i<4; i++){
			buttonInPanel[i] = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonInPanel[i].setPreferredSize(new Dimension(800, 50));
			
			menuButtons[i] = new JButton();
			menuButtons[i].setPreferredSize(new Dimension(150, 50));
			
			buttonInPanel[i].add(menuButtons[i]);
//			buttonInPanel[i].setBackground(Color.decode("#0C0000"));
			buttonsPanel.add(buttonInPanel[i]);
//			buttonsPanel.setBackground(Color.decode("#0C0000"));
		}
		//create game button
		menuButtons[0].setText("Create Game");
		menuButtons[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				//create game modal
				final JDialog createModal = new JDialog(Menu.this, "Create Game", true);
				Container modalContainer = createModal.getContentPane();
				modalContainer.setLayout(new BorderLayout());
				
				//forms for inputs
				JPanel formPanel = new JPanel(new GridLayout(1,2));
				JTextField portOption = new JTextField();
				formPanel.add(new JLabel("Enter max players"));
				formPanel.add(portOption);
				
				JButton finalCreate = new JButton("Create Game"); 
				final JTextField inPort = portOption;
				finalCreate.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a) {
						String portInput = inPort.getText();
						
						//CREATE LOBBY
					}
				});
				modalContainer.add(formPanel, BorderLayout.PAGE_START);
				modalContainer.add(finalCreate, BorderLayout.PAGE_END);
				createModal.pack();
				createModal.setVisible(true);	
			}
		});
		
		//join game button
		menuButtons[1].setText("Join Game");		
		menuButtons[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				//join game modal
				final JDialog joinModal = new JDialog(Menu.this, "Join Game", true);
				Container modalContainer = joinModal.getContentPane();
				modalContainer.setLayout(new BorderLayout());
				
				//forms for inputs
				JPanel formPanel = new JPanel(new GridLayout(3,2));
				JTextField[] options = new JTextField[3];
				final JTextField[] inOpts = new JTextField[3];
				for(int i=0; i<3; i++){
					options[i] = new JTextField();
					inOpts[i] = options[i];
				}
				formPanel.add(new JLabel("IP Address: "));
				formPanel.add(options[0]);
				formPanel.add(new JLabel("Port Number: "));				
				formPanel.add(options[1]);
				formPanel.add(new JLabel("Name: "));				
				formPanel.add(options[2]);
				JButton finalJoin = new JButton("Join Game"); 
				finalJoin.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent a) {
						String ipInput = inOpts[0].getText();
						String portInput = inOpts[1].getText();
						String nameInput = inOpts[2].getText();
						//change to game lobby page here
						
						
					}
				});
				modalContainer.add(formPanel, BorderLayout.PAGE_START);
				modalContainer.add(finalJoin, BorderLayout.PAGE_END);
				joinModal.pack();
				joinModal.setVisible(true);	
			}
		});		
		
		//help button
		menuButtons[2].setText("Help");
		
		//exit button
		menuButtons[3].setText("Exit");	
		menuButtons[3].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				//close game
				Menu.this.dispose();
			}
		});
		this.pack();
	}
}
