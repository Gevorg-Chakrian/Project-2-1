package Pieces;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class Bishop extends Piece {




    public Bishop(int color, int row, int column) {
        super(color, row, column, 3);

    }



    public String toString(){
        return "Bishop";
    }

    @Override
    public boolean checkLegal(Square squareToBeMovedTo,Board board) {
        this.setBoard(board);
        if(((DiceFace.getN()!=3 && GlobalVariables.IGNOREDICE) && !DiceFace.testThrow&&!Queen.isQueenMoves())&& !Board.isTest){return  false;}
        if (isSameColor(squareToBeMovedTo)) {
            return false;
        }
        if (super.getRow() > squareToBeMovedTo.getRow() && super.getColumn() > squareToBeMovedTo.getColumn()) {
            return checkDiagonal(squareToBeMovedTo, 1, 1); //chagend from 1s to -1s

        } else if (super.getRow() < squareToBeMovedTo.getRow() && super.getColumn() < squareToBeMovedTo.getColumn()) {
            return checkDiagonal(squareToBeMovedTo, -1, -1);//changed from -1s to 1s

        } else if (super.getRow() > squareToBeMovedTo.getRow() && super.getColumn() < squareToBeMovedTo.getColumn()) {
            return checkDiagonal(squareToBeMovedTo, 1, -1);

        } else if (super.getRow() < squareToBeMovedTo.getRow() && super.getColumn() > squareToBeMovedTo.getColumn()) {
            return checkDiagonal(squareToBeMovedTo, -1, 1);

        } else return false;


    }


    private boolean checkDiagonal(Square squareToBeMovedTo, int horizontalCoordinate, int diagonalCoordinate) {

        int s_row = squareToBeMovedTo.getRow();
        int s_column = squareToBeMovedTo.getColumn();

        while (true) {
            if (s_row == super.getRow() && s_column == super.getColumn()) {
                return true;
            } else if (s_row == super.getRow() && s_column != super.getColumn()) {
                return false;
            } else if (s_row != super.getRow() && s_column == super.getColumn()) {
                return false;
            }
            s_row += horizontalCoordinate;
            s_column += diagonalCoordinate;

            if (this.getBoard().getSquares()[s_row][s_column].isOccupied() && !(s_row == super.getRow() && s_column == super.getColumn())) {
                return false;
            }

        }
    }

    public boolean isSameColor(Square squareToBeMovedTo) {
        if (squareToBeMovedTo.isOccupied()) {
            return this.getColor() == squareToBeMovedTo.getPiece().getColor();
        } else return false;

    }
    @Override
    public Bishop copy() {
        return new Bishop(this.getColor(), this.getRow(), this.getColumn());
    }
    @Override
    public ImageView getIcon(){
    if(color == 1){
        return new ImageView("pieces_images/bishop_white.png");
    }
    else{
        return new ImageView("pieces_images/bishop.png");
    }
    }
}
