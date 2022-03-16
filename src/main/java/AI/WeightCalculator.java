package AI;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Pieces.*;
import Utilities.GlobalVariables;

public class WeightCalculator {
    private final Square square;
    private final Board board;
    private final int player;
    private final Piece piece;
    private final String[][] weights = new String[8][8];

    public WeightCalculator(Square square, Board board, int player)
    {


        this.square = square;
        this.board=board;
        this.player=player;
        piece = checkLegalPiece(square);
        if(piece == null)
            return;


        long startTime = System.nanoTime();
        for(int i=0; i<board.getSquares().length; i++){
            for (int j=0; j<board.getSquares()[i].length; j++)
            {
                if(board.getSquares()[i][j].equals(square)){
                    weights[i][j]="H\t";
                    continue;
                }
                else{
                    if(square.getPiece().checkLegal(board.getSquares()[i][j],board)) {
                        if (board.getSquares()[i][j].isOccupied()) {
                            int n = 2 * calcWeightMultiplicator(board.getSquares()[i][j]);
                            weights[i][j]=n +"\t";
                        }
                        else
                            weights[i][j]="1\t";
                    }
                    else
                        weights[i][j]="0\t";
                }
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 100;
        printB(duration);
    }

    private Piece checkLegalPiece(Square square)
    {
        Piece occupying = square.getPiece();
        if(GlobalVariables.PRINTLOG) System.out.println("OUTPUT OF INSERTED PIECE = " + occupying.toString());
        switch(DiceFace.getN()){
            case 1:
                if(occupying.toString().equals("Pawn") && occupying.getColor() == player)
                    return occupying;
            case 2:
                if(occupying.toString().equals("Knight") && occupying.getColor() == player)
                    return occupying;
            case 3:
                if(occupying.toString().equals("Rook") && occupying.getColor() == player)
                    return occupying;
            case 4:
                if(occupying.toString().equals("Bishop") && occupying.getColor() == player)
                    return occupying;
            case 5:
                if(occupying.toString().equals("Queen") && occupying.getColor() == player)
                    return occupying;
            case 6:
                if(occupying.toString().equals("King") && occupying.getColor() == player)
                    return occupying;
            default:
                if(occupying.getColor() != player)
                    if(GlobalVariables.PRINTLOG) System.out.println("Not the same color.");
                if(GlobalVariables.PRINTLOG) System.out.println("NO match found for this color.");
                return null;
        }
    }

    public static int calcWeightMultiplicator(Square square)
    {
        if(square.getPiece().toString().equals("Pawn"))
            return 1;
        if(square.getPiece().toString().equals("Knight"))
            return 2;
        if(square.getPiece().toString().equals("Rook"))
            return 3;
        if(square.getPiece().toString().equals("Bishop"))
            return 4;
        if(square.getPiece().toString().equals("Queen"))
            return 20;
        if(square.getPiece().toString().equals("King"))
            return 100;

        return 1;
    }




    public void printB(long duration)
    {
        for (String[] sv : weights){
            for (String s : sv)
                if(GlobalVariables.PRINTLOG) System.out.print(s);

            if(GlobalVariables.PRINTLOG) System.out.println();
        }
        if(GlobalVariables.PRINTLOG) System.out.println("Time of execution: " + duration + " * 10^-4 ms");
    }
}
