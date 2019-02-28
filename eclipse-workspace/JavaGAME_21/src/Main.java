import javax.swing.*;

public class Main {
    private static RenderWindow gameWindow;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    public static void main(String[] args){
        gameWindow = new RenderWindow(WIDTH, HEIGHT);
        gameWindow.setBounds(100, 100, 600, 600);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setVisible(true);
    }

}
