package AI;

import Chessboard.Board;
import Chessboard.Square;
import Pieces.Piece_Pruning;
import Utilities.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Normalization.normalizeScore;

public class SuicideRate {
    private final Square[][] squares;
    private final List<Square> against;
    private final List<Square> army;
    private final int color;
    private final Board board;
    private final ArrayList<Double> score;

    private boolean holimpusHasAlmostFallen = false;

    public SuicideRate(Board board, int color)
    {
        this.board=board;
        this.squares= board.getSquares();
        this.color = color;
        army = calculateRace(color);
        if(color==1)
            against=calculateRace(-1);
        else
            against=calculateRace(1);

        score=calculateScore();

    }

    private ArrayList<Double> calculateScore()
    {
        ArrayList<Double> killersN = new ArrayList<>();
        for(Square s : army)
        {
            if(s.isOccupied() && s.getPiece().toString().equals("King") && allPossibleDeaths(s).size()!=0) {
                if(GlobalVariables.PRINTLOG) System.out.println("KING M8");
                holimpusHasAlmostFallen=true;
                killersN.add(-10000.0);
            }
            else if(s.isOccupied() && s.getPiece().toString().equals("Queen") && allPossibleDeaths(s).size()!=0)
                killersN.add(-100.0);
            else
                killersN.add(-(double) allPossibleDeaths(s).size());
        }

        return killersN;
    }

    private List<Square> calculateRace(int color)
    {
        List<Square> racists = new ArrayList<>();

        for(Square[] squareV : squares)
        {
            for (Square s :  squareV)
            {
                if(s.isOccupied() && s.getPiece().getColor()==color)
                {
                    racists.add(s);
                }
            }
        }
        return racists;
    }

    private List<Square> allPossibleDeaths(Square victim)
    {
        List<Square> hitmans = new ArrayList<>();

        for(Square square:against)
        {
            boolean isHitman = false;

            for(Square destinations : allPossibleMoves(square))
            {
                if(destinations.equals(victim))
                    isHitman=true;
            }

            if(isHitman)
                hitmans.add(square);
        }
        return hitmans;
    }

    protected List<Square> allPossibleMoves(Square startSquare)
    {
        List<Square> possibleSquare = new ArrayList<>();

        for(Square[] squareV: board.getSquares())
        {
            for(Square square: squareV)
            {
                if(Piece_Pruning.move_pruning(startSquare.getPiece(),board)){
                    continue;
                }
                if(startSquare.getPiece().checkLegal(square,board)){
                    possibleSquare.add(square);
                }
            }
        }

        return possibleSquare;
    }

    public static double normalize(ArrayList<Double> output ){
        ArrayList<Double> normalizeData = new ArrayList<>();
        for(int i = 0; i<output.size(); i++){
            double data = output.get(i);//(1 / (1+Math.exp(-0.1*output.get(i))));
            normalizeData.add(data);
        }
        int size = normalizeData.size();
        double total = 0;
        for(int j = 0; j<normalizeData.size(); j++){
            total = total +  normalizeData.get(j);
        }
        double result = total/size;
        result = normalizeScore(0.1, result*10);//(result-0.5)*2;
        if(GlobalVariables.PRINTLOG) System.out.println(result);
        return result;//result * 1000;

    }

    public double getScore() {
        if(holimpusHasAlmostFallen) return -5;
        return normalize(score);
    }
}
