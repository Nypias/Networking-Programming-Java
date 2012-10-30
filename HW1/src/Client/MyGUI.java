package Client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MyGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 8437247200839907136L;
	
	private JPanel wordContainer;
	private JPanel scoreContainer;
	private JPanel letterContainer;
	private JPanel newGameContainer;
	
	private JLabel clientWord;
	private JLabel clientScore;
	private JTextField clientLetter;
	private JButton submitLetter;
	private JButton newGameButton;
	
	private JMenuBar menuBar;
	private JMenu menuOptions;
	private JMenuItem newGame, resetGame, quit;
	
	
	private Client client;
	private int tries = 10;

    
	/*int tries = 10;
    private JLabel wordLabel = null;
    private JLabel triesLabel = null;
    private Client client = null;
    private JTextArea letterTextArea = null;*/
	

    public MyGUI(Client client) {
        /*this.client = client;
        this.f = new JFrame("Hangman Game");
        this.letterTextArea = new JTextArea("*");
        this.wordLabel = new JLabel("N/A");
        this.triesLabel = new JLabel(Integer.toString(tries));*/
        
       super();
       this.client = client;

    }
    
    
    public void createNewGameContainer() {
		this.newGameContainer = new JPanel(new FlowLayout());
		this.newGameButton = new JButton("New Game");
		
		this.newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Executed when new game button is pressed
                System.out.println("---- Starting a new game ----");
                client.sendMessage("START\n");
            }
        });
		
		this.newGameContainer.add(this.newGameButton);
	}
    
    public void createWordContainer () {
		this.wordContainer = new JPanel(new FlowLayout());
		
		// Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.CENTER;
	    gridbag.setConstraints(this.wordContainer, constraints);
	    this.wordContainer.setLayout(gridbag);
	    
		this.clientWord = new JLabel("     ");
		this.clientWord.setFont(new Font("Arial", 1, 50));
		this.wordContainer.add(this.clientWord);
		
	}
	
	public void createScoreContainer() {
		this.scoreContainer = new JPanel();
		
		// Definition a GridBagLayout to positionate the JLabel component in the center of the JPanel
		GridBagLayout gridbag = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    constraints.fill = GridBagConstraints.CENTER;
	    gridbag.setConstraints(this.scoreContainer, constraints);
	    this.scoreContainer.setLayout(gridbag);
		
		this.clientScore = new JLabel("Score: " + this.tries);
		this.clientScore.setFont(new Font("Arial", 1, 30));
		this.scoreContainer.add(this.clientScore);
	}
	
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
                System.out.println("Sending Letter=" + clientLetter.getText().toLowerCase());
                client.sendMessage("LETTER|" + clientLetter.getText().toLowerCase() + "\n");
                // Reset the letter textfield
                clientLetter.setText("");
            }
        });
	    
	    this.letterContainer.add(this.submitLetter);
	}
	
	private void createMenuBar() {
		this.menuBar = new JMenuBar();
		
		this.menuOptions = new JMenu ("Options");
		
		this.newGame = new JMenuItem("New Game");
		this.menuOptions.add(this.newGame);
		
		this.resetGame = new JMenuItem("Reset Game");
		this.menuOptions.add(this.resetGame);
		
		this.quit = new JMenuItem("Quit");
		this.menuOptions.add(this.quit);
		
		this.menuBar.add(menuOptions);
	}


	@Override
	public void run() {
			this.setSize(600, 200);
			this.setTitle("HangMan Game");
			this.setLocationRelativeTo(this);
			
			this.setLayout(new BorderLayout());
			
			// Add the new game container
			this.createNewGameContainer();
			this.add(this.newGameContainer, BorderLayout.NORTH);
			
			// Add the word container
			this.createWordContainer();
			this.add(this.wordContainer, BorderLayout.CENTER);
			
			// Add the score container
			this.createScoreContainer();
			this.add(this.scoreContainer, BorderLayout.EAST);
			
			// Add the letter container
			this.createLetterContainer();
			this.add(this.letterContainer, BorderLayout.WEST);
			
			// Set the menu bar
			this.createMenuBar();
			this.setJMenuBar(this.menuBar);
			
			this.setVisible(true);
			
			// Close the window
			addWindowListener (new WindowAdapter() {
				public void windowClosing (WindowEvent ev){	System.exit(0);	}
			});
		
	}

    /*private void createGUI() {
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
         });
        //f.pack();
        f.setVisible(true);

    }

    public static MyGUI showGUI(Client client) {
        // Create and display the form 
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
    } */
	
	public JLabel getWordLabel() {
		return this.clientWord;
	}
	
	public JLabel getTriesLabel() {
        return this.clientScore;
    }
	
	public JFrame getF() {
        return this;
    }
}