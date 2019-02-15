import javax.swing.JFrame;

public class Main {

	private static final int HEIGHT = 600;
	private static final int WIDTH = 700;
	
	
	public static void main(String[] args) {
		JFrame GameGUI = new JFrame();
		DrawDinamics DD = new DrawDinamics(WIDTH, HEIGHT);
		
		GameGUI.add(DD);
		GameGUI.setBounds(10, 10, WIDTH, HEIGHT);
		GameGUI.setVisible(true);
		GameGUI.setResizable(false);
		GameGUI.setTitle("Breakout Ball");
		GameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

}
