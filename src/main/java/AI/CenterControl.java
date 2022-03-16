package AI;
import Chessboard.Board;
import Chessboard.Square;
import Pieces.Piece;
import static Utilities.Normalization.normalizeScore;

import java.util.LinkedList;


public class CenterControl {
    private final double maxScore = 23;
    private double currentScore = 0;


    public double CalcScore(Board board, int getturn){
        int turn = getturn;
        currentScore = 0;
        LinkedList<Piece> pieces = (turn == 1) ? board.getWhitePieces() : board.getBlackPieces();
        Square[][] squares = board.getSquares();

        for (Piece piece : pieces) {
            checkingLegal(piece, squares, board);
        }
        return normalizeScore(0.01, currentScore);
    }

    private void checkingLegal(Piece piece, Square[][] squares, Board board) {
        for(int j = 0; j < 4; j++){
            for(int l = 0; l < 4; l++ ){
                int row = j + 2;
                int column = l + 2;
                Square square = squares[row][column];
                if(piece.getValue() == 1){
                    piece.centerCheck = 1;
                }
                if(piece.checkLegal(square, board)){

                    if(piece.getValue() == 1){
                        currentScore += 2;
                        return;
                    } else {
                        currentScore++;
                        return;
                    }

                }
            }
        }
    }

}
