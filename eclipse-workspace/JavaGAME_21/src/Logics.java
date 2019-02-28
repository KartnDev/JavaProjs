import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Logics {

    private static Random random;

    public static void fillRandom(int cols, int rows, int interval,HashMap<Pair<Integer, Integer>, Integer> hashMap){
        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                random = new Random();
                Pair<Integer, Integer> pair = new Pair<Integer, Integer>(i, j);
                hashMap.put(pair, random.nextInt(interval));
            }
        }
    }
}
