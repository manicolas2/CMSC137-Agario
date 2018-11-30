package agario;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.basic.*;

public class ChatPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4248861492763515952L;
	private static JPanel sendingArea;
    private static JScrollPane scrollArea;
    private static JTextField messageField;
    private static JTextArea messageBox;
    private static JPanel scoreBoard;
    private static JTextArea scores;
    
    public ChatPanel() {
    	super();
    	
    	setLayout(new BorderLayout());
    	
        scores = new JTextArea("                             SCORES");
        scores.setEditable(false);
        scores.setBackground(Color.WHITE);
        scores.setForeground(Color.BLACK);
        DefaultCaret scoreCaret = (DefaultCaret) scores.getCaret();
        scoreCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        sendingArea = new JPanel();
        
        messageBox = new JTextArea();
        messageBox.setEditable(false);
        messageBox.setLineWrap(true);
        messageBox.setBackground(Color.WHITE);
        messageBox.setForeground(Color.BLACK);

        messageField = new JTextField(20);
        messageField.setForeground(Color.BLACK);
        messageField.setBackground(Color.WHITE);
        messageField.addActionListener(createMessageAction());
        
        sendingArea.add(messageField);
        sendingArea.setBackground(Color.WHITE);
        
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

        displayMessage("                    WELCOME TO AGARIO\n");
        setPreferredSize(new Dimension(300,680));
        scoreBoard = new JPanel();
        scores.setPreferredSize(new Dimension(300,200));
        scoreBoard.setPreferredSize(new Dimension(300,225));
        scoreBoard.add(scores);
        add(scoreBoard, BorderLayout.NORTH);
        add(scrollArea,BorderLayout.CENTER);
        add(sendingArea, BorderLayout.SOUTH);
    }
    
    private ActionListener createMessageAction(){
        ActionListener temp = new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                if(messageField.getText().toLowerCase().equals("bye")){ //add && game over
                    // player _ disconnected
                    System.exit(0);
                }else if(!messageField.getText().equals("")){
                    displayMessage("You: " + messageField.getText() + "\n");
                         
                    messageBox.setCaretPosition(messageBox.getDocument().getLength());
                    messageField.setText("");
                }

                messageField.setBackground(Color.WHITE);
//                GameGUI.focusOnGame();
            }
        };

        return temp;
    }

	private void displayMessage(String message) {
		// TODO Auto-generated method stub
		messageBox.setSize(400,100);
        messageBox.append(message);
	}
	
	private JButton createZeroButton() {
        JButton zero = new JButton();
        zero.setPreferredSize(new Dimension(0, 0));
        zero.setMinimumSize(new Dimension(0, 0));
        zero.setMaximumSize(new Dimension(0, 0));
        return zero;
    }

}
