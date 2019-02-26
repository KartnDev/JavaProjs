import java.util.ArrayList;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class ListClass implements ListModel<String> {

	private ArrayList<String> data;

	public ListClass() {
		data = new ArrayList<String>();
	}

	@Override
	public int getSize() {
		return data.size();
	}

	@Override
	public String getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {

	}

	@Override
	public void removeListDataListener(ListDataListener l) {

	}

	public void addElement(String s) {
		data.add(s);
	}

	public void removeElement(int index) {
		data.remove(index);
	}

	public void removeElement(String s) {
		data.remove(s);
	}
}
