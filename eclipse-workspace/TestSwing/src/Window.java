import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class Window extends JFrame {

	private ListClass modelList;
	private JList<String> list;
	private JButton RemoveButton;

	public Window() {
		
		//
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 22, 249, 371);
		getContentPane().add(scrollPane);

		modelList = new ListClass();

		list = new JList<String>(modelList);
		scrollPane.setViewportView(list);

		RemoveButton = new JButton("Remove");

		RemoveButton.setBounds(295, 20, 89, 23);

		getContentPane().add(RemoveButton);
		RemoveButton.addActionListener(e -> {

			modelList.removeElement(list.getSelectedValue());
			

		});
	}

	public void addElements(String s) {
		modelList.addElement(s);
	}

	public void removeElement(int index) {
		modelList.removeElement(index);
	}
}
