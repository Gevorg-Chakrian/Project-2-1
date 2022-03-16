package Pieces;

import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class Rook extends Piece {



    public Rook(int color, int row, int column) {
        super(color, row, column, 5);
    }

    @Override
    public String toString() {
        return "Rook";
    }



        @Override
        public boolean checkLegal(Square squareToBeMovedTo,Board board) {
            this.setBoard(board);
            if(((DiceFace.getN()!=4 && GlobalVariables.IGNOREDICE)&&!DiceFace.testThrow && !Queen.isQueenMoves())&& !Board.isTest){return  false;}
            boolean rowMatches = this.getRow() == squareToBeMovedTo.getRow();
            boolean columnMatches = this.getColumn() == squareToBeMovedTo.getColumn();

            if (!rowMatches && !columnMatches) {
                return false;
            }
            if (rowMatches) {
                return checkRowMatches(squareToBeMovedTo) && isSameColor(squareToBeMovedTo);

            }else return checkColumnMatches(squareToBeMovedTo) && isSameColor(squareToBeMovedTo);
        }

        private boolean checkRowMatches(Square squareToBeMovedTo) {

            if (this.getColumn() > squareToBeMovedTo.getColumn()) {
                for (int i = this.getColumn()-1; i > squareToBeMovedTo.getColumn(); i--) {
                    if (this.getBoard().getSquares()[this.getRow()][i].isOccupied()) {
                        return false;
                    }
                }
            } else {
                for (int i = this.getColumn()+1; i < squareToBeMovedTo.getColumn(); i++) {
                    if (this.getBoard().getSquares()[this.getRow()][i].isOccupied()) {
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean checkColumnMatches(Square squareToBeMovedTo) {
            if (this.getRow() > squareToBeMovedTo.getRow()) {
                for (int i = this.getRow()-1; i > squareToBeMovedTo.getRow(); i--) {
                    if (this.getBoard().getSquares()[i][this.getColumn()].isOccupied()) {
                        return false;
                    }
                }
            } else {
                for (int i = this.getRow()+1; i < squareToBeMovedTo.getRow(); i++) {
                    if (this.getBoard().getSquares()[i][this.getColumn()].isOccupied()) {
                        return false;
                    }
                }

            }
            return true;
        }
        @Override
        public boolean isSameColor(Square squareToBeMovedTo) {
            if(squareToBeMovedTo.isOccupied()){
                return this.getColor()!=squareToBeMovedTo.getPiece().getColor();
            }
            return true;
        }


    @Override
    public Rook copy() {
        return new Rook(this.getColor(), this.getRow(), this.getColumn());
    }

    @Override
    public ImageView getIcon(){
        if(color == 1){
            return  new ImageView("pieces_images/Rook_white.png");
        }
        else{
            return new ImageView("pieces_images/Rook.png");
        }
    }
}


