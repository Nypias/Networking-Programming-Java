/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author teo
 */
public class MyGUI {

    int tries = 10;
    JLabel wordLabel = null;
    private JLabel triesLabel = null;
    private JFrame f = null;
    Client client = null;
    private JTextArea letterTextArea = null;

    public MyGUI(Client client) {
        this.client = client;
        this.f = new JFrame("Hangman Game");
        this.letterTextArea = new JTextArea("*");
        this.wordLabel = new JLabel("N/A");
        this.triesLabel = new JLabel(Integer.toString(tries));

    }

    private void createGUI() {
        f.setSize(600, 300);

        JButton sendLetterButton = new JButton("Send Letter");
        JButton startButton = new JButton("Start Game");

        letterTextArea.setSize(20, 20);

        JPanel jpanel1 = new JPanel();
        JPanel jpanel2 = new JPanel();
        JPanel jpanel3 = new JPanel();
        jpanel1.add(startButton, BorderLayout.SOUTH);
        jpanel1.add(sendLetterButton, BorderLayout.CENTER);
        jpanel1.add(letterTextArea, BorderLayout.NORTH);
        jpanel2.add(wordLabel);
        jpanel3.add(triesLabel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed
                System.out.println("Starting game");
                client.sendMessage("START\n");
            }
        });

        sendLetterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed
                System.out.println("Sending Letter=" + letterTextArea.getText());
                client.sendMessage("LETTER|" + letterTextArea.getText() + "\n");
            }
        });

        f.add(jpanel1, BorderLayout.WEST);
        f.add(jpanel2, BorderLayout.CENTER);
        f.add(jpanel3, BorderLayout.EAST);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*f.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
         System.exit(0);
         }
         });*/
        //f.pack();
        f.setVisible(true);

    }

    /*
     * 
     */
    public static MyGUI showGUI(Client client) {
        /* Create and display the form */
        final MyGUI gui = new MyGUI(client);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.createGUI();
            }
        });
        return gui;
    }

    public JLabel getWordLabel() {
        return wordLabel;
    }

    public JLabel getTriesLabel() {
        return triesLabel;
    }

    public JFrame getF() {
        return f;
    }
}