import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class PersonModel implements TableModel {

	private ArrayList<TableModelListener> listeners;

	public PersonModel() {
		listeners = new ArrayList<TableModelListener>();
	}

	@Override
	public int getRowCount() {
		return Main.persons.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {

		switch (columnIndex) {
		case 0:
			return "Name";
		case 1:
			return "Surname";
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Main.persons.get(rowIndex).getName();
		case 1:
			return Main.persons.get(rowIndex).getSurname();
		}
		return Main.persons.get(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			Main.persons.get(rowIndex).setName((String) aValue);
			break;
		case 1:
			Main.persons.get(rowIndex).setSurname((String) aValue);
			break;
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

}
