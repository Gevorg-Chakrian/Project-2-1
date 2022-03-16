package Pieces;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class Queen extends Piece {


    public static boolean isQueenMoves() {
        return queenMoves;
    }

    private static boolean queenMoves = false;

    public Queen(int color, int row, int column) {
        super(color, row, column, 9);
    }

    public String toString() {
        return "Queen";
    }

    @Override
    public boolean checkLegal(Square squareToBeMovedTo,Board board) {
        this.setBoard(board);
        queenMoves=true;
        if(((DiceFace.getN()!=5 && GlobalVariables.IGNOREDICE)&&!DiceFace.testThrow)&& !Board.isTest){return  false;}
        Rook tmpRook = new Rook(super.getColor(), super.getRow(), super.getColumn());
        Bishop tmpBishop = new Bishop(super.getColor(), super.getRow(), super.getColumn());

        if (tmpRook.checkLegal(squareToBeMovedTo,board) || tmpBishop.checkLegal(squareToBeMovedTo,board)) {
            queenMoves = false;
            return true;
        }
        queenMoves = false;
        return false;

    }

    @Override
    public Queen copy() {
        return new Queen(this.getColor(), this.getRow(), this.getColumn());
    }

    @Override
    public ImageView getIcon(){
        if(color == 1){
            return new ImageView("pieces_images/queen_white.png");
        }
        else{
            return  new ImageView("pieces_images/queen.png");
        }
    }

}
