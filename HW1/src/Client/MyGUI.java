package Client;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Class implementing the client GUI
 * @author teo, thomas
 */
public class MyGUI extends JFrame implements Runnable {

    private static final long serialVersionUID = 8437247200839907136L;
    private JPanel wordContainer;
    private JPanel triesContainer;
    private JPanel scoreContainer;
    private JPanel letterContainer;
    private JPanel lettersSentContainer;
    private JPanel newGameContainer;
    private JLabel clientWord;
    private JLabel clientTries;
    private JLabel clientScore;
    private JLabel lettersSentLabel;
    private JTextField clientLetter;
    private JButton submitLetter;
    private JButton newGameButton;
    private JMenuBar menuBar;
    private JMenu menuOptions;
    private JMenuItem quit;
    private Client client;
    private boolean gameStarted = false;
    private int tries = 10;
    private int score = 0;
    private String lettersSentStr = "";

    /**
     * Constructor
     * @param client 
     */
    public MyGUI(Client client) {
        super();
        this.client = client;
    }

    /**
     * Creates the New Game container
     */
    public void createNewGameContainer() {
        this.newGameContainer = new JPanel(new FlowLayout());
        this.newGameButton = new JButton("New Game");

        this.newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Executed when new game button is pressed
                System.out.println("---- Starting a new game ----");
                lettersSentStr = "";
                lettersSentLabel.setText("Letters Tried: " + lettersSentStr);
                if (client.isServerStopped()) {
                    System.out.println("Server was stopped");
                    client.setServerStopped(false);
                    
                    Thread clientThread = new Thread(client);
                    clientThread.start();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
                client.sendMessage("START\n");
                gameStarted = true;
            }
        });

        this.newGameContainer.add(this.newGameButton);
    }

    /**
     * Creates container for the missing word
     */
    public void createWordContainer() {
        this.wordContainer = new JPanel(new FlowLayout());

        // Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        gridbag.setConstraints(this.wordContainer, constraints);
        this.wordContainer.setLayout(gridbag);

        this.clientWord = new JLabel("     ");
        this.clientWord.setFont(new Font("Arial", 1, 40));
        this.wordContainer.add(this.clientWord);

    }

    /**
     * Creates the container for the
     * remaining tries the client has left
     */
    public void createTriesContainer() {
        this.triesContainer = new JPanel();

        // Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;

        gridbag.setConstraints(this.triesContainer, constraints);
        this.triesContainer.setLayout(gridbag);

        this.clientTries = new JLabel("Tries: " + this.tries);
        this.clientTries.setFont(new Font("Arial", 1, 20));

        this.triesContainer.add(this.clientTries);

    }

    /**
     * Creates the container with 
     * letters sent by the client
     */
    public void createLettersSentContainer() {
        this.lettersSentContainer = new JPanel();

        // Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;

        gridbag.setConstraints(this.lettersSentContainer, constraints);
        this.lettersSentContainer.setLayout(gridbag);

        this.lettersSentLabel = new JLabel("Letters Tried: " + this.lettersSentStr);
        this.lettersSentLabel.setFont(new Font("Arial", 1, 20));

        this.lettersSentContainer.add(this.lettersSentLabel);
    }

    /**
     * Creates the score container
     */
    public void createScoreContainer() {
        this.scoreContainer = new JPanel();

        // Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        gridbag.setConstraints(this.scoreContainer, constraints);
        this.scoreContainer.setLayout(gridbag);

        this.clientScore = new JLabel("Score: " + this.score);
        this.clientScore.setFont(new Font("Arial", 1, 20));
        this.scoreContainer.add(this.clientScore);
    }

    /**
     * Creates the letter Container
     */
    public void createLetterContainer() {
        this.letterContainer = new JPanel();

        // Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.CENTER;
        gridbag.setConstraints(this.letterContainer, constraints);
        this.letterContainer.setLayout(gridbag);

        this.clientLetter = new JTextField("", 5);
        this.letterContainer.add(clientLetter);

        this.submitLetter = new JButton("Submit");

        this.submitLetter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Execute when button is pressed

                if (gameStarted == false) {
                    JOptionPane.showMessageDialog(getF(), "Start a new game first!", "Hangman Result", JOptionPane.WARNING_MESSAGE);
                } else if (clientLetter.getText() == null || clientLetter.getText().equals("")) {
                    JOptionPane.showMessageDialog(getF(), "Insert Letter or Word", "Hangman Result", JOptionPane.WARNING_MESSAGE);
                } else {
                    String letterToSend = clientLetter.getText().toLowerCase();
                    if (lettersSentStr.indexOf(letterToSend) > -1) {
                        JOptionPane.showMessageDialog(getF(), "You have tried this letter", "Hangman Result", JOptionPane.WARNING_MESSAGE);
                        clientLetter.setText("");
                    } else {
                        System.out.println("Sending Letter=" + letterToSend);
                        if(lettersSentStr.equals(""))
                        {
                            lettersSentStr += letterToSend;
                        }
                        else {
                            lettersSentStr += "," + letterToSend ;
                        }
                        lettersSentLabel.setText("Letters Tried: " + lettersSentStr);
                        client.sendMessage("LETTER|" + letterToSend + "\n");
                        // Reset the letter textfield
                        clientLetter.setText("");
                    }
                }
            }
        });

        this.letterContainer.add(this.submitLetter);
    }

    /**
     * Creates the Menu
     */
    private void createMenuBar() {
        this.menuBar = new JMenuBar();

        this.menuOptions = new JMenu("Options");
        this.quit = new JMenuItem("Quit");
        this.quit.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        client.sendMessage("QUIT");
                        client.terminate();
                        System.exit(0);
                    }
                });
        this.menuOptions.add(this.quit);

        this.menuBar.add(menuOptions);
    }
    
     /**
     * Initializes the client GUI
     */
    @Override
    public void run() {
        this.setSize(800, 300);
        this.setTitle("HangMan Game");
        this.setLocationRelativeTo(this);

        this.setLayout(new GridLayout(3,5));

        // Add the new game container
        this.createNewGameContainer();
        
        this.add(this.newGameContainer);
        this.add(new JLabel(""));
        // Add the word container
        this.add(new JLabel(""));
        this.createWordContainer();
        this.add(this.wordContainer);

        // Add the tries container
        this.createTriesContainer();
        this.add(this.triesContainer);

        // Add the tries container
        this.createLettersSentContainer();
        this.add(this.lettersSentContainer);
        this.add(new JLabel(""));
        // Add the score container
        this.createScoreContainer();
        this.add(this.scoreContainer);

        // Add the letter container
        this.createLetterContainer();
        this.add(this.letterContainer);

        // Set the menu bar
        this.createMenuBar();
        this.setJMenuBar(this.menuBar);

        this.setVisible(true);

        // Close the window
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                client.sendMessage("QUIT");
                System.exit(0);
            }
        });

    }

    /**
     * 
     * @return 
     */
    public JLabel getWordLabel() {
        return this.clientWord;
    }
    /**
     * 
     * @return 
     */
    public JLabel getTriesLabel() {
        return this.clientTries;
    }
    /**
     * 
     * @return 
     */
    public JLabel getScoreLabel() {
        return this.clientScore;
    }
    /**
     * 
     * @return 
     */
    public JFrame getF() {
        return this;
    }
}