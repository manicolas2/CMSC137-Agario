package agario;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.*;

/***************************************
This JPanel has the chat
***************************************/
public class ChatPanel extends JPanel implements Runnable{
    /* ATTRIBUTES */

	private static final long serialVersionUID = -4248861492763515952L;
	private static JPanel sendingArea;
    private static JScrollPane scrollArea;
    private static JTextField messageField;
    private static JTextArea messageBox;
    private ChatModule chatModule;
    private String lobby_id;
    private GameWindow window;
    private GamePanel gamePanel;
    
    /* CONSTRUCTOR */
    public ChatPanel(GameWindow window) {
        this.window = window;

        // UI
        this.initUI();
    }
    
    /* METHODS */
    /***************************************
     
    ***************************************/
    public String start(String lobby_id) {
        this.chatModule = this.window.getChatModule();
        this.lobby_id = lobby_id;
        System.out.println("lobby" + lobby_id);
        if(this.chatModule.connectToLobby(this.lobby_id, this)) {
            // Start thread
            Thread thread = new Thread(this);
            thread.start();
            return this.chatModule.getId();
        }

        return null;
    }

    /***************************************
     
    ***************************************/
    private void initUI() {
        this.setPreferredSize(new Dimension(300,680));

        // Layout
        this.setLayout(new BorderLayout());
        
        // JPanel1: Sending Area 
        this.initSendingArea();

        // JPanel2: Scroll Area - where messages are shown
        this.initScrollArea();

        // JPanel3: Temp - to leave space of scoreboard
        JPanel temp = new JPanel();
        temp.setPreferredSize(new Dimension(300,225));
        
        // Display message in Scroll Area
        this.displayMessage("                    WELCOME TO AGARIO\n");

        // Add panels
        add(temp, BorderLayout.NORTH);
        add(scrollArea,BorderLayout.CENTER);
        add(sendingArea, BorderLayout.SOUTH);
    }

    /***************************************
     
    ***************************************/
    private void initSendingArea() {
        sendingArea = new JPanel();

        messageField = new JTextField(20);
        messageField.setForeground(Color.BLACK);
        messageField.setBackground(Color.WHITE);
        messageField.addActionListener(createMessageAction());
        
        sendingArea.add(messageField);
        sendingArea.setBackground(Color.WHITE);
    }

    /***************************************
     
    ***************************************/
    private void initScrollArea() {
        messageBox = new JTextArea(10, 20);
        messageBox.setEditable(false);
        messageBox.setLineWrap(true);
        messageBox.setBackground(Color.WHITE);
        messageBox.setForeground(Color.BLACK);

        DefaultCaret caret = (DefaultCaret) messageBox.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollArea = new JScrollPane(messageBox);
        scrollArea.setBackground(Color.WHITE);

        scrollArea.getVerticalScrollBar().setUI(new BasicScrollBarUI(){   
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override    
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override 
            protected void configureScrollBarColors(){
                this.thumbColor = new Color(40,40,40,255);
            }

        });
    }

    /***************************************
     
    ***************************************/
    private ActionListener createMessageAction(){
        ActionListener temp = new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
            	String msg = messageField.getText();
                if(msg.toLowerCase().equals("bye")){ //add && game over
                    if(chatModule.getConnected()) chatModule.disconnectFromLobby();
                	
                    System.exit(0);
                } else if(msg.toLowerCase().equals(":q")){
                	chatModule.setConnected();
                	
                	messageBox.setCaretPosition(messageBox.getDocument().getLength());
                    messageField.setText("");
                } else if(!msg.equals("")){
                    displayMessage("You: " + msg + "\n");
                    if(chatModule.getConnected()) chatModule.sendChat(msg);
                         
                    messageBox.setCaretPosition(messageBox.getDocument().getLength());
                    messageField.setText("");
                }
            }
        };

        return temp;
    }

    /***************************************
     
    ***************************************/
	public void displayMessage(String message) {
		messageBox.setSize(400,100);
        messageBox.append(message);
	}
    
    /***************************************
     
    ***************************************/
	private JButton createZeroButton() {
        JButton zero = new JButton();
        zero.setPreferredSize(new Dimension(0, 0));
        zero.setMinimumSize(new Dimension(0, 0));
        zero.setMaximumSize(new Dimension(0, 0));
        return zero;
    }

    
    
    /***************************************
     
    ***************************************/
	@Override
	public void run() {
        this.displayMessage("type :q to quit chat lobby\n");
        while(this.chatModule.getConnected()) {
            this.chatModule.receiveChat(this);
        }
        chatModule.disconnectFromLobby();
        displayMessage("Disconnected from chat lobby.\n");
	}

}
