import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawDinamics extends JPanel implements KeyListener, ActionListener {

	private int widthScreen, heightScreen;
	private int playerX = 340;

	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;

	private Timer timer;
	private int delay = 8;

	private int CountBricksX = 7;
	private int CountBricksY = 3;

	private boolean Matrix[][] = new boolean[CountBricksX][CountBricksY];

	public DrawDinamics(int Width, int Height) {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.heightScreen = Height;
		this.widthScreen = Width;
		timer = new Timer(delay, this);
		timer.start();

		for (int i = 0; i < CountBricksX; i++) {
			for (int j = 0; j < CountBricksY; j++) {
				Matrix[i][j] = true;
			}
		}
	}

	public void paint(Graphics g) {
		// background set
		g.setColor(Color.black);
		g.fillRect(0, 0, widthScreen, heightScreen);

		// set limits
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 4, heightScreen);
		g.fillRect(0, 0, widthScreen, 4);
		g.fillRect(widthScreen - 20, 0, 4, heightScreen);
		g.fillRect(0, heightScreen - 43, widthScreen, 4);

		// fill the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 546, 100, 8);

		// fill ball
		g.setColor(Color.CYAN);
		g.fillOval(ballposX, ballposY, 20, 20);

		for (int i = 0; i < CountBricksX; i++) {
			for (int j = 0; j < CountBricksY; j++) {
				if (Matrix[i][j] == true) {
					g.setColor(Color.white);
					g.fillRect(50 + i * 85, 50 + j * 25, 80, 20);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		ballposX += ballXdir;
		ballposY += ballYdir;

		if (ballposX < 0) {
			ballXdir = -ballXdir;
		}
		if (ballposY < 0) {
			ballYdir = -ballYdir;
		}
		if (ballposX > 670) {
			ballXdir = -ballXdir;
		}
		if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
			ballYdir = -ballYdir;
		}
		for (int i = 0; i < CountBricksX; i++) {
			for (int j = 0; j < CountBricksY; j++) {

				Rectangle brickRect = new Rectangle(50 + i * 85, 50 + j * 25, 80, 20);
				if ((new Rectangle(ballposX, ballposY, 20, 20).intersects(brickRect))&& Matrix[i][j]==true) {
					if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
						ballXdir = -ballXdir;

					} else {
						ballYdir = -ballYdir;
					}
					Matrix[i][j] = false;
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (playerX < 12) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	

	}

	private void moveRight() {
		playerX += 20;
	}

	private void moveLeft() {
		playerX -= 20;
	}
}
