package trader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import marketplace.Item;

public class TraderGUI extends JFrame implements MouseListener, Runnable{
	private static final long serialVersionUID = 3786034680888498980L;
	
	private JPanel listProducts, pendingSellProducts;
	private JTable listItems;

	private List<Item> productsMarket;
	
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

		this.setSize(1000, 800);
		this.setTitle("MarketPlace - Created by Theo and Thomas");
		this.setLocationRelativeTo(this);
		this.setLayout(new BorderLayout());
		this.createListItems(transformTable(productsMarket));
		this.add(listProducts, BorderLayout.WEST);
	}
	
	public void createListItems(Object[][] data) {
		listProducts = new JPanel();
		
		String[] columns = {"Name", "Price", "Seller"};
		listItems = new JTable(data, columns);
		listItems.setPreferredScrollableViewportSize(new Dimension(450,350));
		listItems.setFillsViewportHeight(true);
		listItems.addMouseListener(this);
		
		JScrollPane scrollPane = new JScrollPane(listItems);
		scrollPane.addMouseListener(this);
		listProducts.removeAll();
		listProducts.add(scrollPane);
		this.revalidate();
		this.repaint();
	}
	
	public Object[][] transformTable (List<Item> list) {
		Object[][] data = new Object[list.size()][4];
		int numberRow=0;
		for (int i=0; i < list.size(); i++) {
			Item cur = list.get(i);
			Object[] row = {cur.getName(), cur.getPrice(), cur.getSeller()};
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

	
	

}
