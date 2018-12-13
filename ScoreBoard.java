package agario;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.text.DefaultCaret;
import java.awt.Color;
/***************************************

***************************************/
public class ScoreBoard extends JPanel {
    /* ATTRIBUTES */
    JTextArea scores;

    /* CONSTRUCTOR */
    public ScoreBoard() {
        // UI
        this.initUI();
    }

    /* METHODS */

    /***************************************

    ***************************************/
    private void initUI() {
        scores = new JTextArea("                             SCORES");
        scores.setEditable(false);
        scores.setBackground(Color.WHITE);
        scores.setForeground(Color.BLACK);
        DefaultCaret scoreCaret = (DefaultCaret) scores.getCaret();
        scoreCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scores.setPreferredSize(new Dimension(300,200));
        this.setPreferredSize(new Dimension(300,225));
        this.add(scores);
    }

    


}