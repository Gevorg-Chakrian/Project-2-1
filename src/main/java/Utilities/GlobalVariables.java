package Utilities;

public class GlobalVariables {

    public static boolean PRINTLOG = false; //False not print - True print
    public static boolean IGNOREDICE = false;//True ignore = False Not Ignore
    public static long MCTSTIME = 10000L;
    public static double EXPLORAITONCOEFFICIENT=1.41;
    public static double WINSCORE = 0.4;
    public static int BLACKWON=0;
    public static int WHITEWON=0;
    public static boolean FIRSTGAME = true;
    public static int PLAYGAMES = 1;
    public static boolean isExperiment = false;

    public static int EXPECTIDEPTH = 5;

    public static long game_start_time;
    public static long game_end_time;
    public static int number_of_moves =0 ;


    public static boolean isGameDone = false;


    public static boolean mcts_turn = true;
    public static boolean mcts_noeval = false;


    public static String stupid_string ="";

    public static boolean isBlackKing = true;
    public static boolean isWhiteKing = true;


    public static boolean withProgressiveBias = true;
    public static boolean withBias = true;

    public static int GOALSIDE = -1;
    public static boolean ISGOALOREXPECTI = false; //false for expectiminimax and true for goal

}
