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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import marketplace.Item;

public class TraderGUI extends JFrame implements MouseListener, ActionListener, Runnable{
	private static final long serialVersionUID = 3786034680888498980L;
	
	private JPanel listProductsPanel, pendingSellProductsPanel, panelLeft,
			panelRight, sellProductPanel, titleProductPanel,
			panelAccountBalance, titleBuyPanel, panelBuy, titleWishPanel, panelWish;
	private JTextField nameProduct, priceProduct, balanceTrader, nameProductWished, priceProductWished;
	private JButton sellProduct, newAccountButton, newBuyButton, addWishedProduct;
	private JTable listItems, listPendingItems;

	private List<Item> productsMarket;
	private List<Item> productsPending;
	
	@Override
	public void run() {
		System.out.println("Run method for client");
		this.setVisible(true);
		addWindowListener (new WindowAdapter() {	// Close the window
			public void windowClosing (WindowEvent ev){System.exit(0);}
		});
	}
	
	public TraderGUI() {
		super();
		this.productsMarket = new ArrayList<Item>();
		this.productsPending = new ArrayList<Item>();

		this.setSize(1000, 800);
		this.setTitle("MarketPlace - Created by Theo and Thomas");
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
		
	}
	
	public void createListItems(Object[][] data) {
		listProductsPanel = new JPanel();
		
		String[] columns = {"Name", "Price"};
		listItems = new JTable(data, columns);
		listItems.setPreferredScrollableViewportSize(new Dimension(450,350));
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
		listPendingItems.setPreferredScrollableViewportSize(new Dimension(450,350));
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
		nameProduct = new JTextField("",10);
		JLabel price = new JLabel("Price : ");
		priceProduct = new JTextField("",10);
		sellProduct = new JButton("Sell Product");
		sellProduct.setActionCommand("sellProductButton");
		sellProduct.addActionListener(this);
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
		JPanel panelContent = new JPanel(new GridLayout(3,2));
		newAccountButton = new JButton("Create a new account");
		panelContent.add(newAccountButton);
		panelContent.add(new JPanel());
		panelContent.add(new JPanel());
		panelContent.add(new JPanel());
		panelContent.add(new JLabel("Current Balance : "));
		balanceTrader = new JTextField("",10);
		balanceTrader.setEditable(false);
		panelContent.add(balanceTrader);
		panelAccountBalance.add(panelContent);
	}

	
	public void createButtonBuy() {
		panelBuy = new JPanel();
		JPanel smallPanelBuy = new JPanel(new GridLayout(2,1));
		JLabel instructions = new JLabel("Click on an item in the left table to buy it !");
		smallPanelBuy.add(instructions);
		newBuyButton = new JButton("Buy it");
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
		nameProductWished = new JTextField("",10);
		JLabel price = new JLabel("Price wished : ");
		priceProductWished = new JTextField("",10);
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
		panelRight = new JPanel(new GridLayout(7,1));
		
		panelRight.add(panelAccountBalance);
		panelRight.add(titleBuyPanel);
		panelRight.add(panelBuy);
		panelRight.add(titleWishPanel);
		panelRight.add(panelWish);
		panelRight.add(titleProductPanel);
		panelRight.add(sellProductPanel);
	}

	
	public Object[][] transformTable (List<Item> list) {
		Object[][] data = new Object[list.size()][2];
		int numberRow=0;
		for (int i=0; i < list.size(); i++) {
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	

}
