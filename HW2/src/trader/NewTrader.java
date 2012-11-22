package trader;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import tools.Utilities;

public class NewTrader extends JFrame implements ActionListener {
	private static final long serialVersionUID = -311508449207755917L;
	private Trader trader;
	private JPanel mainPanel, titlePanel;
	private JButton createAccount;
	private JTextField usernameJT, firstPassword, secondPassword;

	public NewTrader(Trader trader) {
		super();
		this.trader = trader;
        this.setSize(400, 300);
        this.setTitle("MarketPlace - Created by Theo and Thomas --- ");
        this.setLocationRelativeTo(this);
        this.setLayout(new BorderLayout());
        
        this.createTitleFrame();
        this.add(titlePanel, BorderLayout.NORTH);
        
        this.createTextFields();
        this.add(mainPanel, BorderLayout.CENTER);
        
        System.out.println("coucou");
        
        this.setVisible(true);
        
        addWindowListener(new WindowAdapter() {	// Close the window
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
	}
	
	public void createTitleFrame() {
		titlePanel = new JPanel();
		Font font;
        try {
            FileInputStream in = new FileInputStream(new File("Lobster.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40f);
        } catch (Exception e) {
            font = new Font("serif", Font.PLAIN, 20); //Default
        }
        JLabel title = new JLabel("New Account");
        title.setFont(font);
        titlePanel.add(title);
	}
	
	public void createTextFields() {
		mainPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(10, 0, 10, 0);
		JLabel usernameLabel = new JLabel("Username : ");
		mainPanel.add(usernameLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		this.usernameJT = new JTextField("", 10);
		mainPanel.add(this.usernameJT, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(10, 0, 10, 0);
		JLabel passwordLabel = new JLabel("Password : ");
		mainPanel.add(passwordLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		this.firstPassword = new JPasswordField(10);
		mainPanel.add(this.firstPassword, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(10, 0, 10, 0);
		JLabel password2Label = new JLabel("Confirm your password : ");
		mainPanel.add(password2Label, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		this.secondPassword = new JPasswordField(10);
		mainPanel.add(this.secondPassword, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		c.insets = new Insets(20, 0, 0, 0);
		createAccount = new JButton("Create Account");
		createAccount.setActionCommand("createAccount");
		createAccount.addActionListener(this);
		mainPanel.add(this.createAccount, c);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("createAccount")) {
			// We retrieve the username
			String username = this.usernameJT.getText();
			
			// We check if the two password are equals
			String firstPass = new String(((JPasswordField) this.firstPassword).getPassword());
			String secondPass = new String(((JPasswordField) this.secondPassword).getPassword());
			if (firstPass.length() >= 8) {
				if (firstPass.equals(secondPass) && !username.equals("")) {
					// We create the account
					try {
						this.trader.setName(username);
						((TraderImpl)this.trader).getMarketObj().register(this.trader, Utilities.passwordToSha1(firstPass));
						
						this.setVisible(false);
						this.dispose();
						// We load the main window
						TraderGUI gui = new TraderGUI((TraderImpl) this.trader);
						((TraderImpl) trader).setGUI(gui);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} else if (username.equals("")) {
					JOptionPane.showMessageDialog(this, "Your login is empty");
					this.firstPassword.setText("");
					this.secondPassword.setText("");
				} else if (!firstPass.equals(secondPass)) {
					JOptionPane.showMessageDialog(this, "Your passwords are not the same");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Your password must have a length greater than 8");
				this.firstPassword.setText("");
				this.secondPassword.setText("");
			}
		
		}
		
	}
	
}
