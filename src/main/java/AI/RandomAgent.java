package AI;

import Chessboard.Board;
import Chessboard.Square;
import Utilities.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

//TODO
// make a version that chooses move that captures
public class RandomAgent {
    private Square squaree=null;
    private Square[][] squaress=null;
    private final ArrayList<Square> possibleSquares = new ArrayList();
    private int diceOutput;
    private final boolean PRINT = false;
    private Board board;
    private int color;
    public RandomAgent() {
    }

    public void initialize(Square[][] squares, int diceOutputs,Board board, int color)
    {
        this.color=color;
        this.board = board;
        if(GlobalVariables.PRINTLOG) System.out.println("Printing squares of size- " + squares.length + "x" + squares[0].length);
        diceOutput = diceOutputs;
        if(GlobalVariables.PRINTLOG) System.out.println("Dice result: " + this.diceOutput);
        squaress = squares.clone();
        printB();
        squaree = chooseStartingSquare();
        calculatePossibleSquares();
        if(GlobalVariables.PRINTLOG) System.out.println("INITIALIZATION FINISHED");
    }

    public void extPrint(Square[][] squares)
    {
        squaress = squares.clone();
        if(GlobalVariables.PRINTLOG) System.out.println("Matrix after move:");
        printB();
    }

    private void printB()
    {
        if(GlobalVariables.PRINTLOG) System.out.println("Printing squares of size- " + squaress.length + "x" + squaress[0].length);
        for (Square[] sv : squaress){
            for (Square s : sv)
                if(GlobalVariables.PRINTLOG) System.out.print("|"+ getSquareValue(s)+"|");

            if(GlobalVariables.PRINTLOG) System.out.println();
        }
    }

    private int getSquareValue(Square s)
    {
        if(!s.isOccupied())
            return 0;

        if(s.getPiece().toString().equals("Pawn"))
            return 1;
        if(s.getPiece().toString().equals("Knight"))
            return 2;
        if(s.getPiece().toString().equals("Rook"))
            return 4;
        if(s.getPiece().toString().equals("Bishop"))
            return 3;
        if(s.getPiece().toString().equals("Queen"))
            return 5;
        if(s.getPiece().toString().equals("King"))
            return 6;

        return 0;
    }

    public Square getRandomSquare()
    {
        Square output = null;
        double maxScore = 0;
        for(Square s : possibleSquares)
        {
            double sc = funcScore(s);
            if(sc>=maxScore)
            {
                maxScore=sc;
                output=s;
            }
        }
        return output;
    }

    private Square chooseStartingSquare()
    {
        Square output =null;
        do{
            int x = (int) (Math.random()*8);
            int y = (int) (Math.random()*8);
            output = squaress[x][y];
            if(!output.isOccupied() || dicePiece(output,false) != diceOutput || output.getPiece().getColor() != color){
                if(GlobalVariables.PRINTLOG) System.out.println("Skipping for square " + output);
                output = null;
            }

            if(GlobalVariables.PRINTLOG) System.out.println("Testing starting Square: " + output.toString());
        }while (output == null);

        return output;
    }

    private int dicePiece(Square toBeChecked,boolean multipliers){
        if(!toBeChecked.isOccupied())
            return 0;

        if(toBeChecked.getPiece().toString().equals("Pawn"))
            return 1;
        if(toBeChecked.getPiece().toString().equals("Knight"))
            return 2;
        if(toBeChecked.getPiece().toString().equals("Rook"))
            return 4;
        if(toBeChecked.getPiece().toString().equals("Bishop"))
            return 3;
        if(toBeChecked.getPiece().toString().equals("Queen"))
            if(multipliers)
                return 100;
            else
                return 5;
        if(toBeChecked.getPiece().toString().equals("King"))
            if(multipliers)
                return 1000;
            else
                return 6;

        return 0;
    }

    private boolean isLegal(Square toBeChecked)
    {
        if(GlobalVariables.PRINTLOG) System.out.println("Result of Movin " + squaree.getPiece().checkLegal(toBeChecked,board));
        return squaree.getPiece().checkLegal(toBeChecked,board);
    }

    private double funcScore(Square possible)
    {
        double score = 0;
        if(possible.isOccupied())
        {
            score+=dicePiece(possible, true);
        }

        for(Square[] sv : squaress)
        {
            for(Square s : sv)
            {
                if(!s.isOccupied())
                    continue;
                for(Square victims : allPossibleMoves(s))
                {
                    if(victims.equals(possible))
                        score = score - (score/10);
                    if(score<=0)
                        score=0;


                }
            }
        }


        return score;
    }

    private void calculatePossibleSquares()
    {
        possibleSquares.clear();
        if(GlobalVariables.PRINTLOG) System.out.println("Calculating possible squares input square matrix of size: " + squaress.length + "x" + squaress[0].length + " from sqaure " + squaree.toString());
        for (Square[] squares : squaress) {
            for (Square square : squares) {
                if (squaree.getPiece().checkLegal(square,board)) {

                    if(square.isOccupied()){
                        possibleSquares.clear();
                        possibleSquares.add(square);
                        break;
                    }
                    possibleSquares.add(square);
                }
            }
        }
        if(GlobalVariables.PRINTLOG) System.out.println("Possible squares found: " + possibleSquares.size() + " for piece " + squaree.getPiece().toString());
        if(possibleSquares.size()==0)
        {
            if(GlobalVariables.PRINTLOG) System.out.println("_______________________________________________________________________________________________");
            if(GlobalVariables.PRINTLOG) System.out.println("__________________________________ERROR_NO_POSSIBLE_OUTCOMES___________________________________");
            if(GlobalVariables.PRINTLOG) System.out.println("_______________________________________________________________________________________________");
            initialize(squaress,diceOutput,board,color);
        }
    }

    protected List<Square> allPossibleMoves(Square startSquare)
    {
        List<Square> possibleSquare = new ArrayList<>();

        for(Square[] squareV: board.getSquares())
        {
            for(Square square: squareV)
            {
                if(startSquare.getPiece().checkLegal(square,board)){
                    possibleSquare.add(square);
                }
            }
        }

        return possibleSquare;
    }

    public Square getStartingSquare() {
        return squaree;
    }
}
