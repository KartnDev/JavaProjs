import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class window extends JFrame {

	private JPanel pane1;
	private JPanel pane2;
	private JTabbedPane tabbedPane;
	private JTextArea txtrEnterUrlHere;
	private JButton btnNewButton;
	private JButton getFile;
	private Choice choice;
	private JLabel lblFormat;
	private JButton btnViewImage;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSaveImage;
	private JMenu mnOptions;
	private JMenuItem mntmExit;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JLabel imageLabel;

	public window(int width, int height) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(646, 480);

		setResizable(false);
		getContentPane().setLayout(null);

		pane2 = new JPanel(null);
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 604, 419);

		getContentPane().add(tabbedPane);
		tabbedPane.addTab("Tab 1", pane2);

		txtrEnterUrlHere = new JTextArea();
		txtrEnterUrlHere.setText("Enter url here....");
		txtrEnterUrlHere.setBounds(10, 53, 579, 327);
		pane2.add(txtrEnterUrlHere);

		btnNewButton = new JButton("Get Image");
		btnNewButton.addActionListener(e -> {
			try {
				Main.setImage(new URL(txtrEnterUrlHere.getText()));
			} catch (Exception exception) {
				exception.printStackTrace();
				JOptionPane.showMessageDialog(null, "Invalid URL / No internet connection");
			}

		});
		btnNewButton.setBounds(10, 11, 135, 31);
		pane2.add(btnNewButton);

		getFile = new JButton("Get file");
		getFile.setBounds(454, 11, 135, 31);
		getFile.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			int reply = chooser.showOpenDialog(null);
			if (reply == JFileChooser.APPROVE_OPTION) {
				Main.setImage(chooser.getSelectedFile());
			}
		});

		pane2.add(getFile);
		pane1 = new JPanel(null);
		tabbedPane.addTab("Tab 2", pane1);

		choice = new Choice();
		choice.setBounds(534, 10, 55, 20);
		choice.add("png");
		choice.add("jpg");
		pane1.add(choice);

		lblFormat = new JLabel("Format:");
		lblFormat.setBounds(487, 10, 61, 20);
		pane1.add(lblFormat);

		btnViewImage = new JButton("view");
		btnViewImage.addActionListener(e -> {
			if (Main.getImg() == null)
				return;
			imageLabel.setIcon(new ImageIcon(Main.getImg()));
			imageLabel.updateUI();
		});
		btnViewImage.setFont(new Font("Serif", Font.BOLD, 18));
		btnViewImage.setBounds(10, 10, 92, 41);
		pane1.add(btnViewImage);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 62, 589, 318);
		pane1.add(scrollPane);

		panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BorderLayout(0, 0));

		imageLabel = new JLabel("");
		panel.add(imageLabel, BorderLayout.CENTER);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSaveImage = new JMenuItem("save image");
		mnFile.add(mntmSaveImage);
		mntmSaveImage.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			int reply = chooser.showSaveDialog(null);
			if(reply == JFileChooser.APPROVE_OPTION) {
				Main.saveImage(chooser.getSelectedFile(), choice.getSelectedItem());
			}
		});
		
		
		mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		mntmExit = new JMenuItem("Exit");
		mnOptions.add(mntmExit);
		mntmExit.addActionListener(e -> {
			System.exit(0);
		});

		setVisible(true);
	}
}
