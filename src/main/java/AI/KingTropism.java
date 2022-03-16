package AI;

import Chessboard.Board;
import Chessboard.Square;
import Utilities.DistanceCalc;
import Utilities.GlobalVariables;

import java.util.ArrayList;

import static Utilities.Normalization.normalizeScore;

public class KingTropism {

    private final Board board;
    private final int kingColor;
    private final boolean allowDiagonals;
    private final Square[][] squares;
    private final Square king;

    private final ArrayList<Double> output = new ArrayList();

    public KingTropism(Board board, int kingColor, boolean allowDiagonals)
    {
        this.board=board;
        this.kingColor=kingColor;
        this.allowDiagonals=allowDiagonals;

        this.squares=this.board.getSquares();
        this.king=getKing();

        for (Square[] squareV : squares) {
            for (Square s : squareV) {
                if(s.isOccupied())
                {
                    if(s.getPiece().getColor() != kingColor && s.getPiece().toString().equals("Pawn"))
                    {
                        double distance = new DistanceCalc(this.king,s,allowDiagonals).getOutput();
                        if(GlobalVariables.PRINTLOG) System.out.println("Distance between KING " + this.king + " and square " + s + " is "+ distance);
                        output.add(distance);
                    }
                }
            }
        }

    }

    private Square getKing()
    {
        for (Square[] squareV : squares) {
            for (Square s : squareV) {
                if(s.isOccupied())
                {
                    if(s.getPiece().getColor() == kingColor && s.getPiece().toString().equals("King"))
                    {
                        return s;
                    }
                }
            }
        }
        return null;
    }

   public double getOutput() {
//        if (output.isEmpty())
//            System.out.println("output "+output);
       if(output.isEmpty()){
           return 0;
       }
        return normalize(output);}


  public static double normalize(ArrayList<Double> output ){
        ArrayList<Double> normalizeData = new ArrayList<>();
        for(int i = 0; i<output.size(); i++){
            double data = output.get(i);
            normalizeData.add(data);
        }
        int size = normalizeData.size();
        double total = 0;
        for(int j = 0; j<normalizeData.size(); j++){
            total = total +  normalizeData.get(j);
        }
        double result = total/size; //*5
        return 1-normalizeScore(0.35, result);

    }
     public double average(ArrayList<Double> output){
        double total = 0;
        int size = output.size();
        for(int i = 0; i<size; i++){
            total = total + output.get(i);
        }
        double avg = total/size;
        return avg;
    }

}
