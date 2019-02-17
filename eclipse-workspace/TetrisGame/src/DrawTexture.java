import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawTexture extends JPanel implements ActionListener, KeyListener {

	Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

	private int HEIGHT, WIDTH;

	private Timer timer;
	private int delay = 10;

	private int PosX = 10;
	private int PosY = 10;

	public DrawTexture(int width, int height) {
		this.HEIGHT = height;
		this.WIDTH = width;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}

	private Graphics g;

	public void paint(Graphics g) {
		this.g = g;
		// fill background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// fill borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, WIDTH, 4);
		g.fillRect(0, 0, 4, HEIGHT);
		g.fillRect(WIDTH - 20, 0, 4, HEIGHT);

		// fill figures
		figures.paintFigure(g, PosX, PosY);
		int b = 0;
		try {
			for (Integer key : hashmap.keySet()) {
				figures.paintFigure(g, key - b, hashmap.get(key));
				b++;
			}
		} catch (Exception EX) {

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_D) {
			moveRight();
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			moveLeft();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	private int a = 1;

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			for (Integer key : hashmap.keySet()) {
				if (new Rectangle(key, hashmap.get(key), 10, 10).intersects(new Rectangle(PosX, PosY, 20, 20))) {
					PosY = hashmap.get(key) - 21;

					hashmap.put(PosX + a, PosY);

					PosX = 10;
					PosY = 10;

					a++;
				}
			}
		} catch (Exception ex) {

		}
		if (PosY > 440) {
			PosY = 440;
			hashmap.put(PosX, PosY);
			PosX = 10;
			PosY = 10;
		} else {
			PosY += 1;
		}
		timer.start();
		repaint();

	}

	private void moveLeft() {
		PosX -= 20;
	}

	private void moveRight() {
		PosX += 20;
	}
}
