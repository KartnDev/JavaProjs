import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class figures {
	public static final int[][] figures = { { 1, 3, 5, 7 }, { 2, 4, 5, 7 }, { 3, 5, 4, 6 }, { 3, 5, 4, 7 },
			{ 2, 3, 5, 7 }, { 3, 5, 7, 6 }, { 2, 3, 4, 5 } };

	public static Color RandomColor() {
		Random rand = new Random();
		 int temp = rand.nextInt(6);
		switch (temp){
			case 0: return Color.red;
			case 1: return Color.green;
			case 2: return Color.cyan;
			case 3: return Color.yellow;
			case 4: return Color.orange;
			case 5: return Color.magenta;
		}
		return Color.pink;
	}
	
	
	public static void paintFigure(Graphics g, int PosX, int PosY) {
		// fill figures
				g.setColor(Color.cyan);
				g.fillRect(PosX, PosY, 20, 20);
	}
	
	
}
