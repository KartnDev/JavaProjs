import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

	private JTable table;

	private PersonModel model;

	public Window() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setBounds(100, 100, 640, 480);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 496, 407);
		getContentPane().add(scrollPane);

		model = new PersonModel();
		table = new JTable(model);
		scrollPane.setViewportView(table);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Person person = new Person();
				person.setName(JOptionPane.showInputDialog("Enter the Name"));
				person.setSurname(JOptionPane.showInputDialog("Enter the Surame"));
				Main.persons.add(person);
				table.updateUI();
			}
		});
		addButton.setBounds(517, 11, 89, 23);
		getContentPane().add(addButton);

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1 || Main.persons.size() <= 0)
					return;
				Main.persons.remove(table.getSelectedRow());
				table.updateUI();
			}
		});
		removeButton.setBounds(517, 45, 89, 23);
		getContentPane().add(removeButton);
		setVisible(true);
	}
}
