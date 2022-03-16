package AI;

import Chessboard.Board;
import Pieces.Piece;
import java.util.LinkedList;

import static Utilities.Normalization.normalizeScore;

public class Material {

    public static double getMaterialScore(Board board, int turn) {
        LinkedList<Piece> pieces = (turn == 1) ? board.getWhitePieces() : board.getBlackPieces(); //if turn is white get white pieces if turn is black get black pieces
        double score = 0;
        for (Piece piece : pieces) { // for every piece in pieces list add value to score
            score += piece.getValue();
        }
        return normalizeScore(0.1,score);
    }
}
