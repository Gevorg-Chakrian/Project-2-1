package AI;

import Chessboard.Board;
import Chessboard.Square;
import Pieces.Piece;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;

import static Utilities.Normalization.normalizeScore;


public class Mobility {

    public static double global_score = 0;
    public static boolean wasCalculated = false;

    public static double getMobilityScore(Board board, int turn) {
        if(wasCalculated){
            //System.out.println("WAS CALCULATED");
            return global_score;
        }

        LinkedList<Piece> pieces = new LinkedList<Piece>((turn == 1) ? board.getWhitePieces() : board.getBlackPieces());
        int score = 0;
        PieceEatable pe = new PieceEatable(board, turn);
        Square[][] sq = board.getSquares();
        for(Piece piece: pieces){
            Square startSquare = sq[piece.getRow()][piece.getColumn()];
            List<Square> sqlist = pe.allPossibleMoves(startSquare);
            score = score + sqlist.size();
        }
        return normalizeScore(0.01, score);
        /*Board.turn = turn;
        State state = new State(board);
        List<State> states = state.getAllPossibleStates();
        return ((1 / (1+Math.exp(-0.1* states.size())))-0.5)*2; //return normalized data*/
    }


    public static void setMobilityScore(double score){
        wasCalculated=true;
        global_score=normalizeScore(0.01, score);
    }

}

