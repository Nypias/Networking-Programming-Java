package trader;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import tools.Utilities;

public class TraderGUIMenuBar extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1492702585796958285L;
	private JMenu fileMenu, accountMenu, statisticsMenu;
	private TraderImpl trader;
	private TraderGUI currentFrame;

	public TraderGUIMenuBar(TraderGUI jframe, TraderImpl trader) {
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
		JMenuItem unregister = new JMenuItem("Unregister");
		unregister.setActionCommand("unregister");
		unregister.addActionListener(this);
		this.fileMenu.add(unregister);
		
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
		
		JMenuItem balance = new JMenuItem("Get Balance");
		balance.setActionCommand("balance");
		balance.addActionListener(this);
		this.accountMenu.add(balance);
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

		JMenuItem numberItemsOnSale = new JMenuItem("Number of Items On Sale");
		numberItemsOnSale.setActionCommand("numberItemsOnSale");
		numberItemsOnSale.addActionListener(this);
		this.statisticsMenu.add(numberItemsOnSale);
		
		JMenuItem numberItemsWished = new JMenuItem("Number of Wished Items");
		numberItemsWished.setActionCommand("numberItemsWished");
		numberItemsWished.addActionListener(this);
		this.statisticsMenu.add(numberItemsWished);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "unregister":
				// We unregister the client
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getMarketObj().unregister(trader.getName());
                    	
                    	// We close the current frame to open the login frame
                    	currentFrame.setVisible(false);
                    	currentFrame.dispose();
                    	
                    	new LoginGUI(trader, trader.getMarketObj());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				break;
			case "logout":
				// We logout the client
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	trader.getMarketObj().logout(trader.getName());
                    	
                    	// We close the current frame to open the login frame
                    	currentFrame.setVisible(false);
                    	currentFrame.dispose();
                    	
                    	new LoginGUI(trader, trader.getMarketObj());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
			
				break;
			case "newAccount":
				// We create a new account for the existing trader
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    @Override
                    protected Integer doInBackground() throws Exception {
                        try {
                            trader.getBankObj().newAccount(trader.getName());
                            trader.getBankObj().deposit(trader.getName(), 0);
                            currentFrame.setBalanceTrader(""+trader.getBankObj().findAccount(trader.getName()).getBalance() + " €");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }

                    //runs on EDT, allowed to update gui
                    protected void done() {
                        try {
                            //textField.setText("UPDATE GUI");
                        } catch (Exception e) {
                            //this is where you handle any exceptions that occurred in the
                            //doInBackground() method
                        }
                    }
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
                    	currentFrame.setBalanceTrader(""+trader.getBankObj().findAccount(trader.getName()).getBalance());
                    	return 1;
                    }
                    
                    public void done() {
                    	
                    }
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
                    	currentFrame.setBalanceTrader(""+trader.getBankObj().findAccount(trader.getName()).getBalance());
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
			case "balance":
				new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                    	currentFrame.setBalanceTrader(""+trader.getBankObj().findAccount(trader.getName()).getBalance());
                    	return 1;
                    }
                    
                    public void done() {}
				}.execute();
				break;
			case "numberItemsBought":
				new SwingWorker<Integer, String>() {
	                @Override
	                protected Integer doInBackground() throws Exception {
	                    try {
	                        trader.getMarketObj().listItems(trader.getName(), Utilities.LISTITEMS_BOUGHT_TRADER);	// We want all the items
	                    } catch (RemoteException e) {
	                        e.printStackTrace();
	                    }
	                    return 1;
	                }
	            }.execute();
	            break;
				
			case "numberItemsOnSale":
				new SwingWorker<Integer, String>() {
	                @Override
	                protected Integer doInBackground() throws Exception {
	                    try {
	                        trader.getMarketObj().listItems(trader.getName(), Utilities.LISTITEMS_ON_SALE_TRADER);	// We want all the items
	                    } catch (RemoteException e) {
	                        e.printStackTrace();
	                    }
	                    return 1;
	                }
	            }.execute();
				break;
				
			case "numberItemsSold":
				new SwingWorker<Integer, String>() {
	                @Override
	                protected Integer doInBackground() throws Exception {
	                    try {
	                        trader.getMarketObj().listItems(trader.getName(), Utilities.LISTITEMS_SOLD_TRADER);	// We want all the items
	                    } catch (RemoteException e) {
	                        e.printStackTrace();
	                    }
	                    return 1;
	                }
	            }.execute();
				break;
			case "numberItemsWished":
				new SwingWorker<Integer, String>() {
	                @Override
	                protected Integer doInBackground() throws Exception {
	                    try {
	                        trader.getMarketObj().listItems(trader.getName(), Utilities.LISTITEMS_WISHED_TRADER);	
	                    } catch (RemoteException e) {
	                        e.printStackTrace();
	                    }
	                    return 1;
	                }
	            }.execute();
				break;
			}
	}
}
