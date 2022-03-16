package Chessboard;

import AI.EvaluationFunction;
import AI.State;
import AI.Tree.MCTS;
import MCTSAttempt.MonteCarloTreeSearch;
import Pieces.*;

import java.util.Arrays;
import java.util.LinkedList;

import static Chessboard.Board.scale;

public class Game {
    private final LinkedList<Piece> WhitePieces = new LinkedList<>();
    private final LinkedList<Piece> BlackPieces = new LinkedList<>();
    private final Square[][] squares = new Square[8][8];




    public void start_game() {

        resetBoard(squares);

        Board.getInstance();


        init_squares();
        init_board();
        store_pieces();

        Board.getInstance().setBlackPieces(BlackPieces);
        Board.getInstance().setWhitePieces(WhitePieces);
        Board.getInstance().setSquares(squares);


        //MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();

       // int[] move_index = mcts.findNextMove(Board.getInstance(),1);
    //    System.out.println(Arrays.deepToString(board.getSquares()));

     //   System.exit(0);
      //  MCTS mcts = new MCTS(new State(Board.getInstance(), EvaluationFunction.getScore(Board.getInstance(),"Initial State"),"Initial State"));
       //new State(Board.getInstance()).getAllPossibleStates();
    }

    private void init_board() {
        for (int i = 2; i < 6; i++) {
            for (int k = 0; k < 8; k++) {
                squares[i][k] = new Square(false, i, k, scale, Board.getInstance());
            }
        }

    }



   // public B

    private void store_pieces() {
        store_whites();
        store_blacks();
    }

    private void init_squares() { //board
        init_pawns();
        init_knights();
        init_bishops();
        init_rooks();
        init_queens();
        init_kings();

    }

    private void store_whites() {  //board
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 8; j++) {
                WhitePieces.add(squares[i][j].getPiece());
            }


    }

    private void store_blacks() { //board
        for (int i = 6; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                BlackPieces.add(squares[i][j].getPiece());
            }


    }

    private void init_pawns() { //board


        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Square(new Pawn(1, 1, i), Board.getInstance());
            squares[6][i] = new Square(new Pawn(-1, 6, i), Board.getInstance());
        }


    }

    private void init_knights() {

        squares[0][1] = new Square(new Knight(1, 0, 1), Board.getInstance());
        squares[0][6] = new Square(new Knight(1, 0, 6), Board.getInstance());

        squares[7][1] = new Square(new Knight(-1, 7, 1), Board.getInstance());
        squares[7][6] = new Square(new Knight(-1, 7, 6), Board.getInstance());


    }

    private void init_bishops() {
        squares[0][2] = new Square(new Bishop(1, 0, 2), Board.getInstance());
        squares[0][5] = new Square(new Bishop(1, 0, 5), Board.getInstance());

        squares[7][2] = new Square(new Bishop(-1, 7, 2), Board.getInstance());
        squares[7][5] = new Square(new Bishop(-1, 7, 5), Board.getInstance());


    }

    private void init_rooks() {
        squares[0][0] = new Square(new Rook(1, 0, 0), Board.getInstance());
        squares[0][7] = new Square(new Rook(1, 0, 7), Board.getInstance());

        squares[7][0] = new Square(new Rook(-1, 7, 0), Board.getInstance());
        squares[7][7] = new Square(new Rook(-1, 7, 7), Board.getInstance());


    }

    private void init_kings() {
        squares[0][4] = new Square(new King(1, 0, 4), Board.getInstance());
        squares[7][4] = new Square(new King(-1, 7, 4), Board.getInstance());


    }

    private void init_queens() {
        squares[0][3] = new Square(new Queen(1, 0, 3), Board.getInstance());
        squares[7][3] = new Square(new Queen(-1, 7, 3), Board.getInstance());


    }

    public Square[][] getSquares() {
        return squares;
    }

    private void resetBoard(Square[][] target)
    {
        for (Square[] sv:
             target) {
            for (Square s:
                 sv) {
                s = null;
            }
        }
    }
}
