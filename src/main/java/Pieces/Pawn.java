package Pieces;

import Chessboard.Board;
import Chessboard.Dice;
import Chessboard.DiceFace;
import Chessboard.Square;
import MCTSAttempt.MonteCarloTreeSearch;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;

public class Pawn extends Piece {


    private boolean hasmoved = false;
    private Square checkingLegalSquare;
    private boolean legal;
    private int steps = 0;

    public boolean print =false;

    public Pawn(int color, int row, int column) {
        super(color, row, column, 1);
    }

    @Override
    public String toString() {
        return "Pawn";
    }

    @Override
    public boolean move(Square squareToBeMovedTo,Board board) {
        this.setBoard(board);

        if(print)
        {
            System.out.println("MOVING CONDITION:");
            System.out.println("Dice.wasThrown "+Dice.wasThrown);
            System.out.println("Bard.istest "+Board.isTest);
            System.out.println("CheckLegal "+checkLegal(squareToBeMovedTo,this.getBoard()));
            //System.out.println("CheckTurn " + checkTurn()+"\n\n\n");

        }

        if (Dice.wasThrown || Board.isTest) {
            if (checkLegal(squareToBeMovedTo,this.getBoard())) {
                if (checkTurn()) {
                    Dice.wasThrown = false;
                    movePiece(squareToBeMovedTo,board);
                    if(print) System.out.println("MOVED");
                  //  Piece.printKingScores(board);Z

                    return true;
                }else
                    if(this.getRow()==1 || this.getRow()==6) {this.hasMoved(false);}
                return false;
            }
            if(GlobalVariables.PRINTLOG) System.out.println(incorrectMove(squareToBeMovedTo) + "\n************");

            return false;
        } else return false;
    }



    @Override
    public boolean checkLegal(Square squareToBeMovedTo,Board board) {
        this.setBoard(board);
        if (this.getRow()*this.getColor() == 6 || this.getRow()*this.getColor() == -1 ) { //6 and -1
            super.promoting = true;
        } else
        if (((DiceFace.getN() != 1 && GlobalVariables.IGNOREDICE) && !DiceFace.testThrow)&& !Board.isTest) {return false;}
        legal = false;
        checkingLegalSquare = squareToBeMovedTo;
        if(print){
            System.out.println(squareToBeMovedTo);
        }
        checkPawnMove();
        return legal;
    }

    public boolean isHasmoved() {
        return hasmoved;
    }

    @Override
    public Pawn copy() {
        return new Pawn(this.getColor(), this.getRow(), this.getColumn());
    }

    private void checkPawnMove() {
        if (checkingLegalSquare.getColumn() == this.getColumn()) {
            moving();
        } else if (checkingLegalSquare.getColumn() == this.getColumn() + 1 || checkingLegalSquare.getColumn() == this.getColumn() - 1) {
            capturing();
        }
        promoting();//
    }

    private void moving() {
        if (!hasmoved) {
            firstMove();
            if(print) System.out.println("FirstMove");
        } else {
            notFirstMove();
            if(print) System.out.println("NotFirstMove");
        }
    }

    private void firstMove() {
        if (checkingLegalSquare.getRow() == super.getRow() + super.getColor()) {
            if (!checkingLegalSquare.isOccupied()) {
                legal = true;
                steps++;
            }
        } else if (checkingLegalSquare.getRow() == this.getRow() + 2 * super.getColor()) {
            if (!checkingLegalSquare.isOccupied() && !this.getBoard().getSquares()[this.getRow() + super.getColor()][this.getColumn()].isOccupied()) {
                legal = true;
                steps += 2;
            }
        }
        //hasmoved = true;
    }

    private void notFirstMove() {
        if (checkingLegalSquare.getRow() == super.getRow() + super.getColor()) {
            if (!checkingLegalSquare.isOccupied()) {
                legal = true;
                steps++;
            }
        }
    }

    private void capturing() {
        if (checkingLegalSquare.isOccupied() && checkingLegalSquare.getPiece().getColor() != this.getColor()) {
            if (checkingLegalSquare.getRow() == this.getRow() + super.getColor()) {
                legal = true;
                steps++;
            }
        }
    }

    public void hasMoved(boolean hasmoved) {
        this.hasmoved = hasmoved;
    }

    private void promoting() {
        if (steps == 6) {
            //checkingLegalSquare.setOccupyingPiece() = "dicethrow";

        }
    }

    public void togglePrint()
    {
        print= !print;
    }

    @Override
    public ImageView getIcon() {
        if (color == 1) {
            return new ImageView("pieces_images/pawn_white.png");
        } else {
            return new ImageView("pieces_images/pawn.png");
        }
    }


}
