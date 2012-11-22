package trader;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class TraderGUIMenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1492702585796958285L;
	private JMenu fileMenu, accountMenu, statisticsMenu;
	private TraderImpl trader;
	private Frame currentFrame;

	public TraderGUIMenuBar(Frame jframe, TraderImpl trader) {
		super();
		this.trader = trader;
		this.currentFrame = jframe;

		// File Menu
		this.fileMenu = new JMenu("File");
		this.add(fileMenu);
		this.createFileItemMenu();

		// Account Menu
		this.accountMenu = new JMenu("Account");
		this.add(accountMenu);
		this.createAccountItemMenu();

		// Statistics Menu
		this.statisticsMenu = new JMenu("Statistics");
		this.add(statisticsMenu);
		this.createStatisticsItemMenu();

		this.revalidate();
	}

	public void createFileItemMenu() {
		JMenuItem logout = new JMenuItem("Logout");
		logout.setActionCommand("logout");
		logout.addActionListener(this);
		this.fileMenu.add(logout);
	}

	public void createAccountItemMenu() {
		JMenuItem newAccount = new JMenuItem("New Account");
		newAccount.setActionCommand("newAccount");
		newAccount.addActionListener(this);
		this.accountMenu.add(newAccount);

		JMenuItem deposit = new JMenuItem("Deposit");
		deposit.setActionCommand("deposit");
		deposit.addActionListener(this);
		this.accountMenu.add(deposit);

		JMenuItem withdraw = new JMenuItem("Withdraw");
		withdraw.setActionCommand("withdraw");
		withdraw.addActionListener(this);
		this.accountMenu.add(withdraw);

		JMenuItem delete = new JMenuItem("Delete Account");
		delete.setActionCommand("deleteAccount");
		delete.addActionListener(this);
		this.accountMenu.add(delete);
	}

	public void createStatisticsItemMenu() {
		JMenuItem numberItemsBought = new JMenuItem("Number of Items Bought");
		numberItemsBought.setActionCommand("numberItemsBought");
		numberItemsBought.addActionListener(this);
		this.statisticsMenu.add(numberItemsBought);

		JMenuItem numberItemsSold = new JMenuItem("Number of Items Sold");
		numberItemsSold.setActionCommand("numberItemsSold");
		numberItemsSold.addActionListener(this);
		this.statisticsMenu.add(numberItemsSold);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "logout":
				// We unregister the client
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getMarketObj().unregister(trader.getName());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
			
				break;
			case "newAccount":
				// We create a new account for the existing trader
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getBankObj().newAccount(trader.getName());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				
				break;
			case "deposit":
				// We create a JOptionPane to ask the trader how much money he wants to deposit
				String depositAmount = (String)JOptionPane.showInputDialog(
	                    this.currentFrame,
	                    "How much money do you want to deposit ?",
	                    "Deposit Money",
	                    JOptionPane.QUESTION_MESSAGE);
				final int amountDeposit = Integer.parseInt(depositAmount);
				
				// We deposit the amount into the bank
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getBankObj().deposit(trader.getName(), (float) amountDeposit);
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				
				break;
			case "withdraw":
				// We create a JOptionPane to ask the trader how much money he wants to withdraw
				String withdrawAmount = (String)JOptionPane.showInputDialog(
	                    this.currentFrame,
	                    "How much money do you want to withdraw ?",
	                    "Withdraw Money",
	                    JOptionPane.QUESTION_MESSAGE);
				final int amountWithdraw = Integer.parseInt(withdrawAmount);
				
				// We withdraw the amount into the bank
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getBankObj().withdraw(trader.getName(), (float) amountWithdraw);
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				
				break;
			case "deleteAccount":
				// We remove the bank account
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getBankObj().deleteAccount(trader.getName());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				
				break;
			case "numberItemsBought":
			case "numberItemsSold":
				// We display the same window for bought items and sold items
				//int numberItemsBought = this.trader.getMarketObj()
				//int numberItemsSold = this.trader.getMarketObj()
				
				 //JOptionPane.showMessageDialog(this.currentFrame,
				//	        "Number of product bought: '" + numberItemsBought + "\nNumber of products sold:" +
				//		numberItemsSold);
				break;
			}


	}

}
