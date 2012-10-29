/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
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
    int triesLeft = 10;
    Client client = null;
    public MyGUI(Client client){
        this.client = client;
    }
    
    private void createGUI(){
        
        JFrame f = new JFrame("Hangman Game");
        f.setSize(600, 300);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                System.out.println("Starting game");
                client.sendMessage("START");
            }
        });   
        
        
        JTextArea letterTextArea = new JTextArea("*");
        JLabel wordLabel = new JLabel("N/A");
        JLabel triesLabel = new JLabel(Integer.toString(triesLeft));
        letterTextArea.setSize(20, 20);
               
        JPanel jpanel1 = new JPanel();
        JPanel jpanel2 = new JPanel();
        JPanel jpanel3 = new JPanel();
        jpanel1.add(startButton,BorderLayout.SOUTH);
        jpanel1.add(letterTextArea,BorderLayout.NORTH);
        jpanel2.add(wordLabel);
        jpanel3.add(triesLabel);

        f.add(jpanel1, BorderLayout.WEST);
        f.add(jpanel2, BorderLayout.CENTER);
        f.add(jpanel3, BorderLayout.EAST);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });*/
        f.pack();
        f.setVisible(true);
        
    }
    
    /*
     * 
     */
    public static MyGUI showGUI(Client client){
        /* Create and display the form */
        final MyGUI gui = new MyGUI(client);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.createGUI();
            }
        });
        return gui;
    }
   
}