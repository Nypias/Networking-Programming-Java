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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import marketplace.*;
import bankjpa.Account;

public class TraderGUI extends JFrame implements MouseListener, ActionListener {

    private static final long serialVersionUID = 3786034680888498980L;
    private JPanel listProductsPanel, panelLeft,
            panelRight, sellProductPanel, titleProductPanel,
            panelAccountBalance, titleBuyPanel, panelBuy, titleWishPanel, panelWish;
    private JTextField nameProduct, priceProduct, balanceTrader, nameProductWished, priceProductWished;
    private JButton sellProduct, newAccountButton, newBuyButton, addWishedProduct, refreshListItemButton;
    private JTable listItems;
    private JScrollPane scrPaneTextArea;
    private JTextArea textArea;
    private TableModel listItemsModel;
    private List<Item> productsMarket;
    private TraderImpl trader;
    private Account account;
    private Item itemClicked = null;

    public TraderGUI(final TraderImpl trader) {
        super();
        this.trader = trader;
        this.productsMarket = new ArrayList<Item>();
        this.account = null;
        this.setSize(1000, 800);
        this.setTitle("MarketPlace - Created by Theo and Thomas --- Trader:" + trader.getName());
        //
        this.setLocationRelativeTo(this);
        this.setLayout(new BorderLayout());
        this.createListItems();
        this.createListPendingItem();
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
                try {
                    trader.getBankObj().deleteAccount(trader.getName());
                    trader.getMarketObj().unregister(trader.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

    }

    public void createListItems() {
        listProductsPanel = new JPanel();
        listItems = new JTable();
        listItems.setPreferredScrollableViewportSize(new Dimension(450, 350));
        listItems.setFillsViewportHeight(true);
        listItems.addMouseListener(this);

        listItemsModel = new TableModel(this.productsMarket);
        listItems.setModel(listItemsModel);

        JScrollPane scrollPane = new JScrollPane(listItems);
        scrollPane.addMouseListener(this);
        listProductsPanel.removeAll();
        listProductsPanel.add(scrollPane);
        this.revalidate();
        this.repaint();
    }

    public TableModel getListItemsModel() {
        return listItemsModel;
    }

    public void setListItemsModel(TableModel listItemsModel) {
        this.listItemsModel = listItemsModel;
    }

    public void createListPendingItem() {
        textArea = new JTextArea();
        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(20);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setText("");

        // We create a scroll and we add the JTextArea into the scroll
        scrPaneTextArea = new JScrollPane(textArea);
        scrPaneTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrPaneTextArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.revalidate();
        this.repaint();
    }

    public void createPanelLeft() {
        panelLeft = new JPanel(new BorderLayout());
        panelLeft.add(listProductsPanel, BorderLayout.NORTH);
        panelLeft.add(scrPaneTextArea, BorderLayout.CENTER);
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
                            Item item1 = new Item(nameProduct.getText(), Integer.parseInt(priceProduct.getText()), trader.getName());
                            trader.getMarketObj().sell(trader.getName(), item1);
                            //trader.getMarketObj().listItems(trader.getName(), true);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }

                    //runs on EDT, allowed to update gui
                    protected void done() {
                        try {
                            // We reset the textfield
                            nameProduct.setText("");
                            priceProduct.setText("");
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
        JLabel title = new JLabel("Sell a new product");
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
        JLabel title = new JLabel("Buy a product");
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
                            balanceTrader.setText(account.getBalance() + " €");
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
            }
        });


        // Refresh the list
        refreshListItemButton = new JButton("Refresh the list of products");
        refreshListItemButton.setActionCommand("refreshListItemButton");
        refreshListItemButton.addActionListener(this);

        panelContent.add(refreshListItemButton);
        panelContent.add(newAccountButton);
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
        this.newBuyButton.setEnabled(false);
        newBuyButton.setActionCommand("newBuyButton");

        newBuyButton.addActionListener(this);
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
        JLabel title = new JLabel("Add a wished product");
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

    public void addLog(String text) {
        textArea.append(text + "\n");
    }

    public void setBalanceTrader(String price) {
        this.balanceTrader.setText(price + " €");
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
        JTable jtable = (JTable) e.getSource();
        int selection = jtable.getSelectedRow();

        this.itemClicked = listItemsModel.getRow(selection);
        this.newBuyButton.setEnabled(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent arg) {
        if (arg.getActionCommand().equals("addWishedButton")) {
            final String nameWishProduct = nameProductWished.getText();
            final int priceWishProduct = Integer.parseInt(priceProductWished.getText());

            new SwingWorker<Integer, String>() {
                protected Integer doInBackground() throws Exception {
                    try {
                        Wish wish = new Wish(nameWishProduct, priceWishProduct, trader.getName());
                        trader.getMarketObj().wish(trader.getName(), wish);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return 1;
                }

                protected void done() {
                    try {
                    } catch (Exception e) {
                        // We reset the textfields
                        nameProductWished.setText("");
                        priceProductWished.setText("");
                    }
                }
            }.execute();
        } else if (arg.getActionCommand().equals("refreshListItemButton")) {
            new SwingWorker<Integer, String>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    try {
                        trader.getMarketObj().listItems(trader.getName(), true);	// We want all the items
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return 1;
                }
            }.execute();
        } else if (arg.getActionCommand().equals("newBuyButton")) {
            new SwingWorker<Integer, String>() {
                @Override
                protected Integer doInBackground() throws Exception {
                    try {
                        Item item1 = new Item(itemClicked.getName(), itemClicked.getPrice(), null);
                        trader.getMarketObj().buy(trader.getName(), item1);
                        //trader.getMarketObj().listItems(trader.getName(), true);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return 1;
                }

                protected void done() {
                    try {
                        newBuyButton.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
}
