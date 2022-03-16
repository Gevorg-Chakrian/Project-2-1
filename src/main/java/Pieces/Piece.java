
package Pieces;

import AI.KingSafety;
import Chessboard.Board;
import Chessboard.Dice;
import Chessboard.Square;
import Pieces.Promotion.Promotion;
import Pieces.Promotion.PromotionStage;
import Utilities.GlobalVariables;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;


public class Piece {

    private String notation;
    private Board board;
    protected int row;
    protected int column;
    private boolean isCaptured;
    protected int color;
    protected int value;
    public boolean print;

    private String currentLog;

    private static Rectangle displayTurnA;
    private static Rectangle displayTurnB;

    protected boolean promoting = false;
    private Promotion promotion = new Promotion();
    public int centerCheck = 0;

    public static void setTurnLabel(Rectangle displayTurnAin, Rectangle displayTurnBin) {
        displayTurnA = displayTurnAin;
        displayTurnB = displayTurnBin;

    }

    public Piece(int color, int row, int column, int value) {
        this.color = color;
        this.row = row;
        this.column = column;
        this.value = value;

    }

    public Piece(int color, String notation) {
        this.color = color;
        char char_column = notation.charAt(0);

        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == char_column) {
                column = i;
            }
        }
        row = notation.charAt(1) - 1;
    }

    public boolean checkLegal(Square squareToBeMovedTo,Board board) {
        return false;
    }

    public boolean move(Square squareToBeMovedTo, Board board) {

        if (Dice.wasThrown || Board.isTest) {

            if (checkLegal(squareToBeMovedTo,board)) {
                if (checkTurn()) {
                    Dice.wasThrown = false;
                    movePiece(squareToBeMovedTo, board);
                    // Piece.printKingScores(board);

                    //System.out.println("NEW " + this + " COORDS : " + this.getRow() + ", " + this.getColumn()
                    return true;
                }
            }
            //System.out.println(incorrectMove(squareToBeMovedTo) + "\n************");

            return false;
        } else
            return false;
    }

    public static void printKingScores(Board board) {
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n");
        if(GlobalVariables.PRINTLOG) System.out.println("******** WHITE KING SCORE ********");
        if(GlobalVariables.PRINTLOG) System.out.println(KingSafety.getEnemySafetyScore(board, -1));
        if(GlobalVariables.PRINTLOG) System.out.println("*******************************************");
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n");
        if(GlobalVariables.PRINTLOG) System.out.println("******** BLACK KING SCORE ********");
        if(GlobalVariables.PRINTLOG) System.out.println(KingSafety.getEnemySafetyScore(board, 1));
        if(GlobalVariables.PRINTLOG) System.out.println("*******************************************");
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n");
    }

    boolean checkTurn() {
        if (Board.turn == this.getColor()) {
            if (this.getColor() == 1) {
                Board.turn = -1;
                if (!Board.isTest) {
                    displayTurnB.setStyle("-fx-opacity: 100%");
                    displayTurnA.setStyle("-fx-opacity: 0%");
                }
            } else {
                Board.turn = 1;
                if (!Board.isTest) {
                    displayTurnA.setStyle("-fx-opacity: 100%");
                    displayTurnB.setStyle("-fx-opacity: 0%");
                }
            }
            return true;
        }
        return false;
    }

    public String logMove(Square squareToBeMovedTo) {
        String color;
        if (this.getColor() == 1)
            color = "White";
        else
            color = "Black";
        String move =color + " " + this + " : " + this.getNotation() + " to " + squareToBeMovedTo.getNotation();
      if(GlobalVariables.PRINTLOG)  System.out.println(" MOVE :" + move);
        return move;
    }

    public String incorrectMove(Square squareToBeMovedTo) {

        return color + " " + this + " : " + this.getNotation() + " cannot move to " + squareToBeMovedTo.getNotation();
    }

    void movePiece(Square squareToBeMovedTo, Board board) {

        board.addToLog(logMove(squareToBeMovedTo));
        if (squareToBeMovedTo.isOccupied()) {
            capturePiece(squareToBeMovedTo, board);
        } else {
            if(GlobalVariables.PRINTLOG) System.out.println(logMove(squareToBeMovedTo) + "\n************");

            currentLog = logMove(squareToBeMovedTo) + "\n************";
        }
        changePosition(squareToBeMovedTo, board);
    }

    private void capturePiece(Square squareToBeMovedTo, Board board) {

        int remove_index=-1;
        LinkedList<Piece> opp_pieces;
        if(this.getColor()==1) {
            opp_pieces = board.getBlackPieces();
        }else opp_pieces=board.getWhitePieces();



        for( Piece piece : opp_pieces){
            if(piece.getRow()==squareToBeMovedTo.getRow() && piece.getColumn()==squareToBeMovedTo.getColumn()){
                remove_index = opp_pieces.indexOf(piece);
            }
        }
        currentLog = logMove(squareToBeMovedTo) + "\n" + this + " takes " + opp_pieces.get(remove_index).toString()
                + "\n************";
        //    System.out.println(currentLog);


        Piece p=opp_pieces.remove(remove_index);
        //System.out.println(p+ " -WAS REMOVED REMOVED");
        }


    public String getLog() {
        return currentLog;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    //TODO
    // figure out how to set the proper coords for the pieces, in the copy of board
    // not by reference -> have to change manually?

    private void changePosition(Square squareToBeMovedTo, Board board) {
        int rowTobeMovedTo = squareToBeMovedTo.getRow();
        int columnToBeMovedTo = squareToBeMovedTo.getColumn();

        int previous_row = this.getRow();
        int previous_col = this.getColumn();


        int remove_index = -1;
       // if(Board.turn==1){board.getWhitePieces}
        setSquareAfterMove(squareToBeMovedTo, board);



        this.setRow(rowTobeMovedTo);
        this.setColumn(columnToBeMovedTo);
        LinkedList<Piece> pieces;
        Piece p = board.getSquares()[this.getRow()][this.getColumn()].getPiece();

        if(this.getColor()==1) {
            pieces = board.getWhitePieces();
        }else pieces=board.getBlackPieces();

            for( Piece piece : pieces){
                if(piece.getRow()==previous_row && piece.getColumn()==previous_col){
                     remove_index = pieces.indexOf(piece);
                }
            }
                if(remove_index!=-1) {
                    pieces.remove(remove_index);
                    pieces.add(p);
                }


        }


    private void setSquareAfterMove(Square squareToBeMovedTo, Board board) {
        int rowTobeMovedTo = squareToBeMovedTo.getRow();
        int columnToBeMovedTo = squareToBeMovedTo.getColumn();
        Square current_square = board.getSquares()[this.getRow()][this.getColumn()];
        current_square.unOccupy();
        if (false) { // was promotion, changed for testing purposes, CHANGE BACK IN TIME
            PromotionStage st = new PromotionStage();
            promotion.setter(board);
            Piece newpiece = promotion.createpiece();
            squareToBeMovedTo.setOccupyingPiece(newpiece);
            newpiece.setRow(rowTobeMovedTo);
            newpiece.setColumn(columnToBeMovedTo);
            promotion.changelist(this, newpiece);
            promoting = false;
            if(GlobalVariables.PRINTLOG) System.out.println("promote");//
        } else {
            squareToBeMovedTo.setOccupyingPiece(this);
        }



        }

        public boolean isSameColor (Square squareToBeMovedTo){
            return false;
        }

        @Override
        public String toString () {
            return "Piece{" + "row=" + row + ", column=" + column + ", isCaptured=" + isCaptured + ", color=" + color + "}";
        }

        public String getNotation () {
            char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
            return letters[this.getColumn()] + String.valueOf(this.getRow() + 1);
        }

        public Piece copy () {
            return new Piece(this.getColor(), this.getRow(), this.getColumn(), this.getValue());
        }

        public int getValue () {
            return this.value;
        }

        public int getRow () {
            return row;
        }

        public void setRow ( int row){
            this.row = row;
        }

        public int getColumn () {
            return column;
        }

        public void setColumn(int column){
            this.column = column;
        }

        public boolean isCaptured () {
            return isCaptured;
        }

        public int getColor () {
            return color;
        }

        public void setCaptured ( boolean captured){
            isCaptured = captured;
        }

        public ImageView getIcon () {
            if (color == 1) {
                return null;
            } else {
                return null;
            }
        }

        public void togglePrint()
        {
            print= !print;
        }
    }

