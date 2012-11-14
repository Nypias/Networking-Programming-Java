package trader;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import marketplace.Item;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 6649973596519689389L;
	
	private List<Item> list;
	private String headerList[] = new String[] {"Name", "Price"};
	
	public TableModel(List<Item> list) {
		this.list = list;
	}
	
	public void addItem (Item item) {
		this.list.add(item);
		fireTableDataChanged();
	}
	
	public void addAllItems (List<Item> list) {
		this.list.clear();
		this.list.addAll(list);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}
	
	public Item getRow(int row) {
		return this.list.get(row);
	}
	
	public String getColumnName(int col) {
		return headerList[col];
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Item myItem = list.get(row);
		switch(column) {
		case 0:
			return myItem.getName();
		case 1:
			return myItem.getPrice();
		default: 
			return null;
		}
	}
	
	public void removeItem(Item item) {
		String name = item.getName();
		int price = item.getPrice();
		for (Item i : list) {
			if (i.getName().equals(name) && i.getPrice() == price) {
				this.list.remove(i);
			}
		}
	}

}
