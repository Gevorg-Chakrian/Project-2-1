package AI;

import Chessboard.Board;
import Chessboard.Square;
import Pieces.Piece_Pruning;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Normalization.normalizeScore;


// gonna combine mobility and Piece Eatable

public class PieceEatable {

    public Board board;
    public int color;
    public ArrayList<Double> score;

    public PieceEatable(Board board, int color)
    {
        this.board = board;
        this.color = color;

        score = calculateScore();
    }

    private ArrayList<Double> calculateScore()
    {

        ArrayList<Double> tempScores = new ArrayList<>();

        for(Square[] squareV: board.getSquares())
        {
            for(Square square: squareV)
            {
                if(square.isOccupied())
                {
                    if(square.getPiece().getColor() == color)
                    {
                        double tempScore = 0.0;
                        List<Square> possibleSq = allPossibleMoves(square);
                        for(Square s: possibleSq)
                        {

                            if(!s.isOccupied())
                            {
                                tempScore+=0;
                            }
                            else if(s.getPiece().toString().equals("Pawn"))
                            {
                                tempScore += 1.0;
                            }
                            else if (s.getPiece().toString().equals("Knight") || s.getPiece().toString().equals("Rook") || s.getPiece().toString().equals("Bishop"))
                            {
                                tempScore += 1.5;
                            }
                            else if(s.getPiece().toString().equals("Queen"))
                            {
                                tempScore += 2.0;
                            }
                            else if(s.getPiece().toString().equals("King"))
                                tempScore += 1000.0;
                        }
                        tempScores.add(tempScore);
                    }
                }
            }
        }
        return tempScores;
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
            if(output.get(i)>=100)
                return 2;
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
        return result;//result * 1000;

    }

    public double getScore() {
        return normalize(score);
    }
}
