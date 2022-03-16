package Chessboard;

import javafx.scene.image.ImageView;

public class DiceFace {

    private static int n;

    public static boolean testThrow = false;

    public static void setN(int in){
        n = in;
    }

    public static int getN() {
        return n;
    }

    public static ImageView getI() {
        if(n<1||n>6)
        {
            return new ImageView("dice/" + 6 +".0.png");
        }
        return new ImageView("dice/" + n +".0.png");
    }
}
