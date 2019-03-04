import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class Map2D {

   static HashMap<Pair<Integer, Integer>, ObjectValues> map;



    public Map2D(){
        map = new HashMap<Pair<Integer, Integer>,ObjectValues>();
        Logics.fillRandom(7,7, 5, map);
    }

    public void RenderMap(Graphics graphics){
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++) {
                graphics.setColor(map.get(new Pair<Integer, Integer>(i, j)).color);
                map.get(new Pair<Integer, Integer>(i, j)).PosX =i*80+30;
                map.get(new Pair<Integer, Integer>(i, j)).PosY =j*80+40;
                graphics.fillOval(map.get(new Pair<Integer, Integer>(i, j)).PosX, map.get(new Pair<Integer, Integer>(i, j)).PosY, 50, 50);
            }
        }
    }
}


