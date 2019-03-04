import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

public class RenderWindow extends JFrame implements MouseMotionListener, ActionListener, KeyListener, MouseInputListener {

    private final Map2D map;
    private Timer timer;
    private static int HEIGHT, WIDTH;
    private int MousePosX, MousePosY;



    public RenderWindow(int Height, int Width){

        map = new Map2D();

        this.HEIGHT = Height;
        this.WIDTH = Width;
        setLayout(null);

        addKeyListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        setFocusable(true);
        timer = new Timer(8, null);
        timer.start();


    }

    public void paint(Graphics graphics){
        //fill background
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0,0,WIDTH, HEIGHT);

        //fill Elements (by RenderMap)
        graphics.setColor(Color.YELLOW);
        map.RenderMap(graphics);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //why action performed not works

        System.out.println(123);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        MousePosX = e.getX();
        MousePosY = e.getY();



        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++) {
                int x = Map2D.map.get(new Pair<Integer, Integer>(i, j)).PosX;
                int y = Map2D.map.get(new Pair<Integer, Integer>(i, j)).PosY;
                Rectangle rect = new Rectangle(x,    y, 30, 30);
                if(new Rectangle(MousePosX, MousePosY, 30,30).intersects(rect)){
                    Map2D.map.get(new Pair<Integer, Integer>(i, j)).PosX +=MousePosX;
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getMousePosX() {
        return MousePosX;
    }

    public int getMousePosY() {
        return MousePosY;
    }
    public void setMousePosX(int value) {
        this.MousePosX = value;
    }

    public void setMousePosY(int value) {
        this.MousePosY = value;
    }
}
