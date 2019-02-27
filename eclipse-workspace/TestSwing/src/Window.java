import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.util.List;

import javax.swing.JButton;

public class Window extends JFrame {

	private ListClass modelList;
	private JList<String> list;
	private JButton RemoveButton;
	private JButton add_button;

	public Window() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		setResizable(false);

		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 22, 249, 371);
		getContentPane().add(scrollPane);

		modelList = new ListClass();

		list = new JList<String>(modelList);
		scrollPane.setViewportView(list);

		add_button = new JButton("Add");
		add_button.addActionListener(e -> {
			String str = JOptionPane.showInputDialog("Enter your element(str) here");
			modelList.addElement(str);
			UpdateUI();
		});
		add_button.setBounds(295, 50, 89, 23);
		getContentPane().add(add_button);

		RemoveButton = new JButton("Remove");
		RemoveButton.setBounds(295, 20, 89, 23);

		getContentPane().add(RemoveButton);
		RemoveButton.addActionListener(e -> {
			List<String> objects = list.getSelectedValuesList();
			
			for (String s : objects) {
				if (s == objects.get(objects.size() - 1)) {
					break;
				}
				modelList.removeElement(s);
			}
			list.setSelectedIndex(-1);
			modelList.removeElement(list.getSelectedValue());
			UpdateUI();

		});

		setVisible(true);
		UpdateUI();
	}

	public void addElements(String s) {
		modelList.addElement(s);

	}

	public void removeElement(int index) {
		modelList.removeElement(index);
	}

	public void UpdateUI() {
		list.updateUI();
	}
}
