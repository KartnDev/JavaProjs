import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Logics {

    private static Random random;

    public static void fillRandom(int cols, int rows, int interval,HashMap<Pair<Integer, Integer>, ObjectValues> hashMap){
        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                random = new Random();
                Pair<Integer, Integer> pair = new Pair<Integer, Integer>(i, j);
                Color nextColor = null;
                switch (random.nextInt(interval)){
                    case 0: nextColor = Color.CYAN; break;
                    case 1: nextColor = Color.ORANGE; break;
                    case 2: nextColor = Color.GREEN; break;
                    case 3: nextColor = Color.RED; break;
                    case 4: nextColor = Color.MAGENTA; break;
                }
                hashMap.put(new Pair<Integer, Integer>(i, j), new ObjectValues(i, j , nextColor));
            }
        }
    }
}
