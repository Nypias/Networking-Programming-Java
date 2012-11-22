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

import marketplace.Market;

public class LoginGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = -6149892272579912177L;
	private JTextField usernameJT, passwordJT;
	private JButton newAccount, login;
	private JPanel mainPanel, titlePanel;
	private Market market;
	private Trader trader;
	
	
	public LoginGUI(Trader trader, Market market) {
		super();
		this.market = market;
		this.trader = trader;
        this.setSize(400, 300);
        this.setTitle("MarketPlace - Created by Theo and Thomas --- ");
        this.setLocationRelativeTo(this);
        this.setLayout(new BorderLayout());
        
        this.createTitlePanel();
        this.add(titlePanel, BorderLayout.NORTH);
        
        this.createTextFields();
        this.add(mainPanel, BorderLayout.CENTER);
        
        this.setVisible(true);
        
        addWindowListener(new WindowAdapter() {	// Close the window
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
	}
	
	public void createTitlePanel() {
		titlePanel = new JPanel();
		Font font;
        try {
            FileInputStream in = new FileInputStream(new File("Lobster.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40f);
        } catch (Exception e) {
            font = new Font("serif", Font.PLAIN, 20); //Default
        }
        JLabel title = new JLabel("Login");
        title.setFont(font);
        titlePanel.add(title);
	}
	
	public void createTextFields() {
		mainPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.gridwidth=1;
		c.gridheight=1;
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
		c.insets = new Insets(10, 15, 0, 10);
		this.usernameJT = new JTextField("", 10);
		mainPanel.add(this.usernameJT, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth=1;
		c.gridheight=1;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 0, 40, 0);
		JLabel passwordLabel = new JLabel("Password : ");
		mainPanel.add(passwordLabel, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.BASELINE;
		c.insets = new Insets(10, 15, 40, 10);
		this.passwordJT = new JPasswordField(10);
		mainPanel.add(this.passwordJT, c);
		
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 3;
		newAccount = new JButton("New Account");
		newAccount.setActionCommand("newAccount");
		newAccount.addActionListener(this);
		mainPanel.add(this.newAccount, c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		login = new JButton("Login");
		login.setPreferredSize(newAccount.getPreferredSize());
		login.setMinimumSize(newAccount.getMinimumSize());
		login.setActionCommand("login");
		login.addActionListener(this);
		mainPanel.add(this.login, c);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("newAccount")) {
			this.setVisible(false);
			this.dispose();
			new NewTrader(this.trader);
			
			
		} else if (arg0.getActionCommand().equals("login")) {
			String loginText = this.usernameJT.getText();
			String passwordText = new String(((JPasswordField) this.passwordJT).getPassword());
			System.out.println(passwordText);
			if (!(loginText.isEmpty() && passwordText.isEmpty())) {
				try {
					trader.setName(loginText);
					// We create a Sha1 version of the password
					String newPasswordEncoded = Utilities.passwordToSha1(passwordText);
					boolean result = market.login(trader, newPasswordEncoded);
					System.out.print(result);
					if (result) {
						this.setVisible(false);
						this.dispose();
						
						TraderGUI gui = new TraderGUI((TraderImpl)trader);
						((TraderImpl)trader).setGUI(gui);
						gui.setBalanceTrader(((TraderImpl)trader).getBankObj().findAccount(trader.getName()).getBalance()+"");
					} else {
						this.passwordJT.setText("");
						JOptionPane.showMessageDialog(this, "Your login/password is not correct");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	

}
