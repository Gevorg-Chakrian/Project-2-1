package Pieces.Promotion;


import Chessboard.Board;
import Pieces.*;

import java.util.LinkedList;


public class Promotion {
    //PromotionGUI promo = new PromotionGUI(Board.turn);
    private int roll = 2;
    private Piece piece = null;
    private final boolean isSIMULATION = true;
    public Board board;
    LinkedList<Piece> white;
    LinkedList<Piece> black;

    public void setter(Board board){
        this.board = board;
        if(board != null){
            white = board.getWhitePieces();
            black = board.getBlackPieces();
        }
    }




    public Piece createpiece() {
        if(isSIMULATION)
        {
            roll = (int) (Math.random()*4 + 2);
        }
        else
        {
            roll = PromotionGUI.promotionChoice;
        }
        switch (roll) {
            case 2:
                piece = new Knight(board.getTurn()* -1, 0, 0);
                break;

            case 3:
                piece = new Bishop(board.getTurn() * -1, 0, 0);
                break;

            case 4:
                piece = new Rook(board.getTurn() * -1, 0, 0);
                break;

            case 5:
                piece = new Queen(board.getTurn() * -1, 0, 0);
                break;

        }
        return piece;
    }

    private int findInList(Piece pawn) {

        int ans = -1;
        int row = pawn.getRow();
        int column = pawn.getColumn();
        for (int i = 0; i < white.size(); i++) {
            if (white.get(i).getRow() == row && white.get(i).getColumn() == column) {
                ans = i;
            }
        }
        for (int i = 0; i < black.size(); i++) {
            if (black.get(i).getRow() == row && black.get(i).getColumn() == column) {
                ans = i;
            }

        }
        return ans;
    }

    public void changelist(Piece pawn, Piece newPiece) {
        int number = findInList(pawn);
        if (pawn.getColor() == 1) {
            white.set(number, newPiece);
        } else{
            black.set(number, newPiece);
        }
    }
}
