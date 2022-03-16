package Pieces;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class King extends Piece {


    private boolean legal = false;
    private Square checkSquare;

    public King(int color, int row, int column) {
        super(color, row, column, 50);
    } // TODO: see the value to give to king I think 0 should be fine

    public String toString() {
        return "King";
    }

    @Override
    public boolean checkLegal(Square squareToBeMovedTo, Board board) {
        this.setBoard(board);
        
        if((DiceFace.getN()!=6 && GlobalVariables.IGNOREDICE)&& !DiceFace.testThrow){return  false;}
        legal = false;
        checkSquare = squareToBeMovedTo;
        if (checkinglegal()){ //&& checkIfCheck(squareToBeMovedTo)) {
            // move(squareToBeMovedTo);
            legal = true;
        }

        return legal;
    }
/*

    private boolean checkIfCheck(Square squareToBeMovedTo) {
       // Board.isTest= true;
        LinkedList<Piece> OpposingPieces;

        if (this.getColor() == 1) {
            OpposingPieces = this.getBoard().getBlackPieces();
        } else OpposingPieces = this.getBoard().getWhitePieces();

        for (Piece opposingPiece : OpposingPieces) {
            if (opposingPiece.checkLegal(squareToBeMovedTo,this.getBoard())) {
                return false;
            }
        }
        return true;
    }

 */

    @Override
    public King copy() {
        return new King(this.getColor(), this.getRow(), this.getColumn());
    }

    private boolean checkinglegal() {
        if (checkSquare.getColumn() == this.getColumn()) {
            if (checkSquare.getRow() == this.getRow() + 1 || checkSquare.getRow() == this.getRow() - 1) {
                return checkColor();
            }
        } else if (checkSquare.getColumn() == this.getColumn() + 1) {
            if (Math.abs(checkSquare.getRow() - this.getRow()) < 2) { // changed 3 to 2
                return checkColor();
            }
        } else if (checkSquare.getColumn() == this.getColumn() - 1) {
            if (Math.abs(checkSquare.getRow() - this.getRow()) < 2) { // changed 3 to 2
                return checkColor();
            }
        }
        return false;
    }

    private boolean checkColor() {
        if (!checkSquare.isOccupied()) {
            return true;
        } else return checkSquare.getPiece().getColor() != this.getColor();
    }

    @Override
    public ImageView getIcon() {
        if (color == 1) {
            String name ="pieces_images/king_white.png";
            return new ImageView(name);

        } else {
            return new ImageView("pieces_images/king.png");
        }
    }
}
