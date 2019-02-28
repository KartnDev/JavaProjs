import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class Map2D {

   static HashMap<Pair<Integer, Integer>, Integer> map;



    public Map2D(){
        map = new HashMap<Pair<Integer, Integer>, Integer>();
        Logics.fillRandom(7,7, 5, map);
    }

    public void RenderMap(Graphics graphics){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++) {
                switch (map.get(new Pair<Integer, Integer>(i, j))){
                    case 0:
                        graphics.setColor(Color.CYAN);
                        break;
                    case 1:
                        graphics.setColor(Color.YELLOW);
                        break;
                    case 2:
                        graphics.setColor(Color.RED);
                        break;
                    case 3:
                        graphics.setColor(Color.ORANGE);
                        break;
                    case 4:
                        graphics.setColor(Color.GREEN);
                        break;
                }
                graphics.fillOval(i*80+20, j*80+50, 50, 50);
            }
        }
    }
}


