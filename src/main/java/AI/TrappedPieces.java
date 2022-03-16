package AI;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Pieces.Piece;
import Pieces.Piece_Pruning;

import java.util.LinkedList;
import java.util.List;

import static Utilities.Normalization.normalizeScore;

public class TrappedPieces {

    private static int turn;
    private static Board board;

    public static double getTrappedPiecesScore(Board board1, int setTurn){
        board = board1;
        turn = setTurn;
        DiceFace.testThrow = true;
        LinkedList<Piece> pieces;
        LinkedList<Piece> opp_pieces;

        if (turn == 1) {
            pieces = board.getWhitePieces();
            opp_pieces = board.getBlackPieces();
        }
        else {
            pieces = board.getBlackPieces();
            opp_pieces = board.getWhitePieces();
        }

        DiceFace.testThrow = false;
        return getTrappedScore(pieces, opp_pieces);
    }

    public static double getTrappedScore(LinkedList<Piece> pieces, LinkedList<Piece> opp_pieces){
        double score = 0;
        double mobility_score=0;
     //   PieceEatable pe = new PieceEatable(board, turn);

        for(Piece piece : pieces) {
        //    List<Square> sqlist = pe.allPossibleMoves(board.getSquares()[piece.getColumn()][piece.getRow()]);
         //   mobility_score = mobility_score + sqlist.size();

            if (!piece.toString().equals("Pawn") && !piece.toString().equals("King")) {
                if (!(isTrapped(piece, board.getSquares(), opp_pieces))){
                    score+=piece.getValue();
                }
            }
        }
        Mobility.setMobilityScore(mobility_score);
        return normalizeScore(0.1, score);
    }

     public static boolean isTrapped(Piece piece, Square[][] squares, LinkedList<Piece> opp_pieces){
        for (Square[] square : squares){
             for (int i = 0; i < squares.length; i++) {
                 if(Piece_Pruning.move_pruning(piece,board)){
                     continue;
                 }


                 if (piece.checkLegal(square[i],board) && !inDanger(square[i], opp_pieces)) {
                     return false;
                 }
             }
         }
         return true;
     }

     public  static boolean inDanger(Square square, LinkedList<Piece> opp_pieces){
         for(Piece piece : opp_pieces){
             if(Piece_Pruning.move_pruning(piece,board)){
                 continue;
             }
             if(piece.checkLegal(square,board)){
                 return true;
             }
         }
         return false;
     }

    
    private static double  normalize(double score){
        double newValue = (1 / (1+Math.exp(-0.001*score)));
        newValue = (newValue-0.5)*2;
        return newValue;
    }
}
