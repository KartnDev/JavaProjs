import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import java.awt.SystemColor;

public class mainFrame extends JFrame {

	public mainFrame() {
		getContentPane().setBackground(SystemColor.inactiveCaptionBorder);
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(new Dimension(415, 319));
		System.out.println(123);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JRadioButton rdbtnReverse = new JRadioButton("reverse");
		rdbtnReverse.setBounds(6, 439, 109, 23);
		getContentPane().add(rdbtnReverse);

		JButton button = new JButton("1");
		button.setBounds(6, 11, 89, 23);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(123);

			}

		});
		getContentPane().add(button);

		JButton button_1 = new JButton("2");
		button_1.setBounds(6, 45, 89, 23);
		getContentPane().add(button_1);

		JButton button_2 = new JButton("3");
		button_2.setBounds(6, 79, 89, 23);
		getContentPane().add(button_2);

		JButton button_3 = new JButton("4");
		button_3.setBounds(105, 11, 89, 23);
		getContentPane().add(button_3);

		JButton button_4 = new JButton("5");
		button_4.setBounds(105, 45, 89, 23);
		getContentPane().add(button_4);

		JButton button_5 = new JButton("6");
		button_5.setBounds(105, 79, 89, 23);
		getContentPane().add(button_5);

		JButton button_6 = new JButton("7");
		button_6.setBounds(204, 11, 89, 23);
		getContentPane().add(button_6);

		JButton button_7 = new JButton("8");
		button_7.setBounds(204, 45, 89, 23);
		getContentPane().add(button_7);

		JButton button_8 = new JButton("9");
		button_8.setBounds(204, 79, 89, 23);
		getContentPane().add(button_8);

		JButton button_9 = new JButton("0");
		button_9.setBounds(105, 113, 89, 23);
		getContentPane().add(button_9);

		JButton button_10 = new JButton("+");
		button_10.setBounds(303, 11, 89, 23);
		getContentPane().add(button_10);

		JButton button_11 = new JButton("-");
		button_11.setBounds(303, 45, 89, 23);
		getContentPane().add(button_11);

		JButton button_12 = new JButton("*");
		button_12.setBounds(303, 79, 89, 23);
		getContentPane().add(button_12);

		JButton button_13 = new JButton("/");
		button_13.setBounds(303, 113, 89, 23);
		getContentPane().add(button_13);

		JButton button_14 = new JButton("=");
		button_14.setBounds(303, 223, 89, 46);
		getContentPane().add(button_14);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(303, 147, 89, 23);
		getContentPane().add(btnClear);
	}

}
