import javax.swing.JFrame;

public class Main {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	
	public static void main(String[] args) {
		DrawTexture element = new DrawTexture(WIDTH, HEIGHT);
		
		JFrame GUI = new JFrame();
		GUI.setVisible(true);
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI.setResizable(false);
		GUI.setBounds(110, 110, WIDTH, HEIGHT);
		GUI.add(element);
	}

}
