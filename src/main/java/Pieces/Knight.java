package Pieces;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class Knight extends Piece {


    public Knight(int color, int row, int column) {
        super(color, row, column, 3);
    }

    public String toString() {
        return "Knight";
    }

    @Override
    public boolean checkLegal(Square squareToBeMovedTo,Board board) {
        this.setBoard(board);
        int n = DiceFace.getN();
        if((((DiceFace.getN() != 2 && GlobalVariables.IGNOREDICE) && !DiceFace.testThrow) && !Board.isTest)){return  false;}
        return checkMoveSet(squareToBeMovedTo) && checkColor(squareToBeMovedTo);
    }

    public boolean checkMoveSet(Square squareToBeMovedTo) {
        return checkLegalMove(super.getColumn(), super.getRow(), squareToBeMovedTo.getColumn(), squareToBeMovedTo.getRow())
                || checkLegalMove(super.getRow(), super.getColumn(), squareToBeMovedTo.getRow(), squareToBeMovedTo.getColumn());
    }


    public boolean checkLegalMove(int pieceCoord1, int pieceCoord2, int squareCoord1, int squareCoord2) {
        return Math.abs(pieceCoord1 - squareCoord1) == 1 && Math.abs(pieceCoord2 - squareCoord2) == 2;
    }


    public boolean checkColor(Square squareToBeMovedTo) {
        if (!squareToBeMovedTo.isOccupied()) {
            return true;
        } else
            return squareToBeMovedTo.isOccupied() && squareToBeMovedTo.getPiece().getColor() != super.getColor();
    }

    @Override
    public Knight copy() {
        return new Knight(this.getColor(), this.getRow(), this.getColumn());
    }

    @Override
    public ImageView getIcon() {
        if (color == 1) {
            return new ImageView("pieces_images/knight_white.png");
        } else {
            return new ImageView("pieces_images/knight.png");
        }
    }


}
