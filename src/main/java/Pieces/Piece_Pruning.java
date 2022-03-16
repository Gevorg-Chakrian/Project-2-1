package Pieces;

import Chessboard.Board;
import Chessboard.Square;


public class Piece_Pruning {
    public static boolean move_pruning(Piece piece,Board board) {
        if (piece.toString().equals("Bishop")) {
            return !pruneBishop(piece, board);
        }
        if (piece.toString().equals("Rook")) {
            return !pruneRook(piece, board);
        }
        if (piece.toString().equals("Queen") || piece.toString().equals("King")) {
            return !pruneKingOrQueen(piece, board);
        }
        return false;
    }


    private static boolean pruneBishop(Piece b,Board board) {
        Square[][] s = board.getSquares();
        boolean move1 = false, move2 = false, move3 = false, move4 = false;

        if (b.getRow() <= 6 && b.getColumn() <= 6) {
            move1 = true;
        }
        if (b.getRow() >= 1 && b.getColumn() >= 1) {
            move2 = true;
        }
        if (b.getRow() <= 6 && b.getColumn() >= 1) {
            move3 = true;
        }
        if (b.getRow() >= 1 && b.getColumn() <= 6) {
            move4 = true;
        }
        int move1_row = b.getRow() + 1;
        int move1_col = b.getColumn() + 1;

        int move2_row = b.getRow() - 1;
        int move2_col = b.getColumn() - 1;

        int move3_row = b.getRow() + 1;
        int move3_col = b.getColumn() - 1;

        int move4_row = b.getRow() - 1;
        int move4_col = b.getColumn() + 1;

        if(checkAMove(b,move1,move1_row,move1_col,s,board)){return true;}
        if(checkAMove(b,move2,move2_row,move2_col,s,board)){return true;}
        if(checkAMove(b,move3,move3_row,move3_col,s,board)){return true;}
        return checkAMove(b, move4, move4_row, move4_col, s, board);
    }
    private static boolean pruneRook(Piece r,Board board) {
        Square[][] s = board.getSquares();
        boolean move1 = false, move2 = false, move3 = false, move4 = false;

        if (r.getRow()>= 1 ) {
            move1 = true;
        }
        if (r.getRow() <=6 ) {
            move2 = true;
        }
        if (r.getColumn() >= 1) {
            move3 = true;
        }
        if (r.getColumn() <= 6) {
            move4 = true;
        }
        int move1_row = r.getRow() - 1;

        int move2_row = r.getRow() + 1;

        int move3_col = r.getColumn() - 1;

        int move4_col = r.getColumn() + 1;

        if(checkAMove(r,move1,move1_row,r.getColumn(),s,board)){return true;}
        if(checkAMove(r,move2,move2_row,r.getColumn(),s,board)){return true;}
        if(checkAMove(r,move3,r.getRow(),move3_col,s,board)){return true;}
        return checkAMove(r, move4, r.getRow(), move4_col, s, board);
    }
    private static boolean pruneKingOrQueen(Piece p, Board board) {
        Square[][] s = board.getSquares();
        return (pruneRook(p,board) || pruneBishop(p,board));
    }




    private static boolean checkAMove(Piece p, boolean move, int s_row, int s_col, Square[][] s,Board board) {
        if (move) {
            return p.checkLegal(s[s_row][s_col],board);
        }
        return false;
    }

}
