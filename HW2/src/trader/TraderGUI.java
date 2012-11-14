package trader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import marketplace.*;
import bank.Account;

public class TraderGUI extends JFrame implements MouseListener, ActionListener {

    private static final long serialVersionUID = 3786034680888498980L;
    private JPanel listProductsPanel, pendingSellProductsPanel, panelLeft,
            panelRight, sellProductPanel, titleProductPanel,
            panelAccountBalance, titleBuyPanel, panelBuy, titleWishPanel, panelWish;
    private JTextField nameProduct, priceProduct, balanceTrader, nameProductWished, priceProductWished;
    private JButton sellProduct, newAccountButton, newBuyButton, addWishedProduct;
    private JTable listItems, listPendingItems;
    private List<Item> productsMarket;
    private List<Item> productsPending;
    private TraderImpl trader;
    private Account account;
    
    public TraderGUI(TraderImpl trader) {
        super();
        this.trader = trader;
        this.productsMarket = new ArrayList<Item>();
        this.productsPending = new ArrayList<Item>();
        this.account = null;
        this.setSize(1000, 800);
        this.setTitle("MarketPlace - Created by Theo and Thomas");
        //
        this.setLocationRelativeTo(this);
        this.setLayout(new BorderLayout());
        this.createListItems(transformTable(productsMarket));
        this.createListPendingItem(transformTable(productsPending));
        this.createPanelLeft();

        this.createSellProduct();
        this.createTitleSellProduct();
        this.createAccountAndBalance();
        this.createTitleWishProduct();
        this.createPanelWish();
        this.createTitleBuyProduct();
        this.createButtonBuy();
        this.createPanelRight();

        this.add(panelLeft, BorderLayout.WEST);
        this.add(panelRight, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {	// Close the window
            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });

    }

    public void createListItems(Object[][] data) {
        listProductsPanel = new JPanel();

        String[] columns = {"Name", "Price"};
        listItems = new JTable(data, columns);
        listItems.setPreferredScrollableViewportSize(new Dimension(450, 350));
        listItems.setFillsViewportHeight(true);
        listItems.addMouseListener(this);

        JScrollPane scrollPane = new JScrollPane(listItems);
        scrollPane.addMouseListener(this);
        listProductsPanel.removeAll();
        listProductsPanel.add(scrollPane);
        this.revalidate();
        this.repaint();
    }

    public void createListPendingItem(Object[][] data) {
        pendingSellProductsPanel = new JPanel();

        String[] columns = {"Name", "Price"};
        listPendingItems = new JTable(data, columns);
        listPendingItems.setPreferredScrollableViewportSize(new Dimension(450, 350));
        listPendingItems.setFillsViewportHeight(true);
        listPendingItems.addMouseListener(this);

        JScrollPane scrollPane = new JScrollPane(listPendingItems);
        scrollPane.addMouseListener(this);
        pendingSellProductsPanel.removeAll();
        pendingSellProductsPanel.add(scrollPane);
        this.revalidate();
        this.repaint();
    }

    public void createPanelLeft() {
        panelLeft = new JPanel(new BorderLayout());
        panelLeft.add(listProductsPanel, BorderLayout.NORTH);
        panelLeft.add(pendingSellProductsPanel, BorderLayout.CENTER);
    }

    public void createSellProduct() {
        sellProductPanel = new JPanel();
        JPanel textFieldPanel = new JPanel(new GridLayout(4, 2));
        JLabel name = new JLabel("Name : ");
        nameProduct = new JTextField("", 10);
        JLabel price = new JLabel("Price : ");
        priceProduct = new JTextField("", 10);
        sellProduct = new JButton("Sell Product");
        sellProduct.setActionCommand("sellProductButton");
        //sellProduct.addActionListener(this);
        sellProduct.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    protected Integer doInBackground() throws Exception {
                        try {
                            //publish(file.getName); //passes the name of the file to process()
                            trader.getMarketObj().register(trader);
                            Item item1 = new Item(nameProduct.getText(), Integer.parseInt(priceProduct.getText()), trader.getName());
                            trader.getMarketObj().sell(trader.getName(), item1);

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }

                    //runs on EDT, allowed to update gui
                    protected void process(String fileName) {
                        //receives the name of the file from publish() and sets it on the textfield.
                        // textField.setText("scanning file: " + fileName);
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


                System.out.println("Sell clicked! after");
            }
        });
        textFieldPanel.add(name);
        textFieldPanel.add(nameProduct);
        textFieldPanel.add(price);
        textFieldPanel.add(priceProduct);
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(sellProduct);
        sellProductPanel.add(textFieldPanel);
    }

    public void createTitleSellProduct() {
        titleProductPanel = new JPanel();
        Font font;
        try {
            FileInputStream in = new FileInputStream(new File("Lobster.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40f);
        } catch (Exception e) {
            font = new Font("serif", Font.PLAIN, 20); //Default
        }
        JLabel title = new JLabel("=== Sell a new product ===");
        title.setFont(font);
        titleProductPanel.add(title);
    }

    public void createTitleBuyProduct() {
        titleBuyPanel = new JPanel();
        Font font;
        try {
            FileInputStream in = new FileInputStream(new File("Lobster.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40f);
        } catch (Exception e) {
            font = new Font("serif", Font.PLAIN, 20); //Default
        }
        JLabel title = new JLabel("=== Buy a product ===");
        title.setFont(font);
        titleBuyPanel.add(title);
    }

    public void createAccountAndBalance() {
        panelAccountBalance = new JPanel();
        JPanel panelContent = new JPanel(new GridLayout(3, 2));
        newAccountButton = new JButton("Create a new account");
        newAccountButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    @Override
                    protected Integer doInBackground() throws Exception {
                        try {
                            account = trader.getBankObj().newAccount(trader.getName());
                            account.deposit(1000f);
                            balanceTrader.setText(account.getBalance()+" €");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }

                    //runs on EDT, allowed to update gui
                    protected void process(String fileName) {
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


                System.out.println("Sell clicked! after");
            }
        });
        panelContent.add(newAccountButton);
        panelContent.add(new JPanel());
        panelContent.add(new JPanel());
        panelContent.add(new JPanel());
        panelContent.add(new JLabel("Current Balance : "));
        balanceTrader = new JTextField("", 10);
        balanceTrader.setEditable(false);
        panelContent.add(balanceTrader);
        panelAccountBalance.add(panelContent);
    }

    public void createButtonBuy() {
        panelBuy = new JPanel();
        JPanel smallPanelBuy = new JPanel(new GridLayout(2, 1));
        JLabel instructions = new JLabel("Click on an item in the left table to buy it !");
        smallPanelBuy.add(instructions);
        newBuyButton = new JButton("Buy it");
        newBuyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new SwingWorker<Integer, String>() {
                    //runs on a background thread.
                    @Override
                    protected Integer doInBackground() throws Exception {
                        try {
                            Item item1 = new Item(nameProduct.getText(), Integer.parseInt(priceProduct.getText()), null);
                            trader.getMarketObj().buy(trader.getName(), item1);

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }

                    //runs on EDT, allowed to update gui
                    protected void process(String fileName) {
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


                System.out.println("Sell clicked! after");
            }
        });
        smallPanelBuy.add(newBuyButton);
        panelBuy.add(smallPanelBuy);
    }

    public void createTitleWishProduct() {
        titleWishPanel = new JPanel();
        Font font;
        try {
            FileInputStream in = new FileInputStream(new File("Lobster.ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(40f);
        } catch (Exception e) {
            font = new Font("serif", Font.PLAIN, 20); //Default
        }
        JLabel title = new JLabel("=== Add a wished product ===");
        title.setFont(font);
        titleWishPanel.add(title);
    }

    public void createPanelWish() {
        panelWish = new JPanel();
        JPanel textFieldPanel = new JPanel(new GridLayout(4, 2));
        JLabel name = new JLabel("Name : ");
        nameProductWished = new JTextField("", 10);
        JLabel price = new JLabel("Price wished : ");
        priceProductWished = new JTextField("", 10);
        addWishedProduct = new JButton("Add wished product");
        addWishedProduct.setActionCommand("addWishedButton");
        addWishedProduct.addActionListener(this);
        textFieldPanel.add(name);
        textFieldPanel.add(nameProductWished);
        textFieldPanel.add(price);
        textFieldPanel.add(priceProductWished);
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(addWishedProduct);
        panelWish.add(textFieldPanel);
    }

    public void createPanelRight() {
        panelRight = new JPanel(new GridLayout(7, 1));

        panelRight.add(panelAccountBalance);
        panelRight.add(titleBuyPanel);
        panelRight.add(panelBuy);
        panelRight.add(titleWishPanel);
        panelRight.add(panelWish);
        panelRight.add(titleProductPanel);
        panelRight.add(sellProductPanel);
    }

    public Object[][] transformTable(List<Item> list) {
        Object[][] data = new Object[list.size()][2];
        int numberRow = 0;
        for (int i = 0; i < list.size(); i++) {
            Item cur = list.get(i);
            Object[] row = {cur.getName(), cur.getPrice()};
            data[numberRow] = row;
            numberRow++;
        }
        return data;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent arg) {
        if (arg.getActionCommand().equals("addWishedButton")) {
        	String nameWishProduct = nameProductWished.getText();
        	int priceWishProduct = Integer.parseInt(priceProductWished.getText());
        	Wish wish = new Wish(nameWishProduct, priceWishProduct, trader.getName());
        	try {
				this.trader.getMarketObj().wish(trader.getName(), wish);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
        }
    }
}
