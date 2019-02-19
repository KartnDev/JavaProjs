import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Choice;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class window extends JFrame {

	public window(int width, int height) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(646, 480);

		setResizable(false);
		getContentPane().setLayout(null);

		JPanel pane2 = new JPanel(null);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 604, 419);

		getContentPane().add(tabbedPane);
		tabbedPane.addTab("Tab 1", pane2);

		JTextArea txtrEnterUrlHere = new JTextArea();
		txtrEnterUrlHere.setText("Enter url here....");
		txtrEnterUrlHere.setBounds(10, 53, 579, 327);
		pane2.add(txtrEnterUrlHere);

		JButton btnNewButton = new JButton("Get Image");
		btnNewButton.addActionListener(e -> System.out.println(1));
		btnNewButton.setBounds(10, 11, 135, 31);
		pane2.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(454, 11, 135, 31);
		pane2.add(btnNewButton_1);
		JPanel pane1 = new JPanel(null);
		tabbedPane.addTab("Tab 2", pane1);

		Choice choice = new Choice();
		choice.setBounds(534, 10, 55, 20);
		choice.add("png");
		choice.add("jpg");
		pane1.add(choice);

		JLabel lblFormat = new JLabel("Format:");
		lblFormat.setBounds(487, 10, 61, 20);
		pane1.add(lblFormat);

		JButton btnViewImage = new JButton("view");
		btnViewImage.setFont(new Font("Serif", Font.BOLD, 18));
		btnViewImage.setBounds(10, 10, 92, 41);
		pane1.add(btnViewImage);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmSaveImage = new JMenuItem("save image");
		mnFile.add(mntmSaveImage);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnOptions.add(mntmExit);
		setVisible(true);
	}
}
