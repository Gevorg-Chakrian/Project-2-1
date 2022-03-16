package AI;

import AI.Tree.Node;
import Chessboard.Board;
import Chessboard.DiceFace;
import Chessboard.Square;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Piece_Pruning;
import Utilities.DataFrame;
import Utilities.GlobalVariables;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class State {
    Board board;
    int visit_count;
    final boolean PRINT = false;
    int playerNo;
    double score;
    double comulativescore;
    String move_coords;
    int color;
    public double logistic_score = 0;

    String move_indexes;

    public String getMoveIndexes() {
        return move_indexes;
    }

    public Board getBoard() {
        return board;
    }

    public State(Board board, double score, String move_coords) {
        this.move_coords = move_coords;
        this.board = board;//.copy();
        this.score = score;
        this.comulativescore = score;
    }


    public State(Board board,int playerNo)
    {
        this.board = board;
        this.playerNo=playerNo;
    }
    public State()
    {

    }


    public List<State> getAllPossibleStates(int roll) {
        int real_turn = Board.turn;

        DiceFace.testThrow = true;
        Board.isTest = true;
        LinkedList<State> states = new LinkedList<>();

        if (playerNo == 1) {
            color = 1;
            if (GlobalVariables.PRINTLOG)
                //System.out.println("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            if (GlobalVariables.PRINTLOG) System.out.println(" WHITE MOVE");
            //  pieces = board.getWhitePieces();
        } else {
            if (GlobalVariables.PRINTLOG)
               // System.out.println("\n\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            if (GlobalVariables.PRINTLOG) System.out.println(" BLACK MOVE");
            color = -1;
        } // pieces =board.getBlackPieces();}


    /*      THIS IS FOR CONSIDERING MOVES FOR BOTH PLAYERS, PER getAllPossibleStates call
        LinkedList<Piece> pieces = new LinkedList<>(board.getWhitePieces());
        pieces.addAll(board.getBlackPieces());
    */
        LinkedList<Piece> pieces = new LinkedList<>();

        // THIS IS FOR CONSIDERING ALL STATES OF ONLY ONE PLAYER
        if (playerNo == 1) {
            pieces.addAll(board.getWhitePieces());
        } else pieces.addAll(board.getBlackPieces());


        Board.turn=playerNo;
        for (Piece piece : pieces) {

            if (Piece_Pruning.move_pruning(piece, board)) {
                continue;
            }

            if(roll != 0 && roll == numb(piece))
            {
                states.addAll(checkAllStates(piece));
                continue;
            }
            if(roll == 0){
                states.addAll(checkAllStates(piece));
            }
            /*if(piecNumber(piece) == roll && roll != 0)
            {
                states.addAll(checkAllStates(piece));
                continue;
            }
            if(piecNumber(piece) == roll && roll != 0)
            {
                states.addAll(checkAllStates(piece));
                continue;
            }
            states.addAll(checkAllStates(piece));*/

        }


        Board.isTest = false;
        //DiceFace.testThrow = false;
        if (GlobalVariables.PRINTLOG) System.out.println("\n\n\n\n");
        if (GlobalVariables.PRINTLOG) System.out.println("~~~~~~~~~~~~~ADDING ALL STATES~~~~~~~~~~~~~~~~~~~");
        if (GlobalVariables.PRINTLOG) System.out.println(states);
        if (GlobalVariables.PRINTLOG) System.out.println("\n\n\n\n");
        if (GlobalVariables.PRINTLOG) System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        Board.turn= real_turn;
        return states;
    }

    public static int numb(Piece target)
    {
        if(target.toString().equals("Pawn"))
            return 1;
        if(target.toString().equals("Knight"))
            return 2;
        if(target.toString().equals("Rook")) //rook=4 bishop=3
            return 4;
        if(target.toString().equals("Bishop"))
            return 3;
        if(target.toString().equals("Queen"))
            return 5;
        if(target.toString().equals("King"))
            return 6;

        return 0;
    }

    private LinkedList<State> checkAllStates(Piece piece) { //public instead of private
        LinkedList<State> states = new LinkedList<>();


        for (int i = 0; i < board.getSquares().length; i++) {
            for (int k = 0; k < board.getSquares().length; k++) {

                Board.turn = piece.getColor();

                if (piece.checkLegal(board.getSquares()[i][k], board)) {
                    if (Objects.equals(piece.toString(), "Pawn") && (piece.getRow() == 1 || piece.getRow() == 6)) {
                        Pawn p = (Pawn) piece;
                        p.hasMoved(false);
                    }
                    String takePiece = "";



                    Board tmp = board.copy();
                    Piece tmp_piece = piece.copy();

                    if(tmp.getSquares()[i][k].isOccupied()){
                        String s = tmp.getSquares()[i][k].getPiece().toString();
                        takePiece=" Took "+ s.substring(0,s.length()-1);
                    }


                    tmp_piece.move(tmp.getSquares()[i][k], tmp);
                    if (Objects.equals(piece.toString(), "Pawn") && (piece.getRow() == 1 || piece.getRow() == 6)) {
                        Pawn p = (Pawn) piece;
                        p.hasMoved(false);
                    }

                    String string_color;
                    if (piece.getColor() == 1) {
                        string_color = "White";
                    } else {
                        string_color = "Black";
                    }

                    String s = string_color + " " + piece + " from (row=" + piece.getRow() + ",column=" + piece.getColumn() + ") to " + tmp.getSquares()[i][k]+takePiece;
                    move_indexes = Integer.toString(piece.getRow())+ piece.getColumn() +i+k;
                    if (GlobalVariables.PRINTLOG) System.out.println("***********************************************");
                    if (GlobalVariables.PRINTLOG) System.out.println("BOARD: " + board.getSquares()[i][k]);
                    if (GlobalVariables.PRINTLOG) System.out.println("TMP: " + tmp.getSquares()[i][k]);
                    if (GlobalVariables.PRINTLOG) System.out.println("***********************************************");


                    if (GlobalVariables.PRINTLOG)
                      //  System.out.println("~~~~~~~~~~~~~~~~~~~NEW STATE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    if (GlobalVariables.PRINTLOG) System.out.println(s);
                    if (GlobalVariables.PRINTLOG) System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    State added_s = new State(tmp, playerNo);
                    added_s.move_coords = s;



                    added_s.setScore(new EvaluationFunction().getScore(added_s));
                    added_s.setMove_indexes(move_indexes);

                    states.add(added_s);
                }

            }
        }
        return states;
    }

    public int piecNumber(Piece target)
    {
        if(target.toString().equals("Pawn"))
            return 1;
        if(target.toString().equals("Knight"))
            return 2;
        if(target.toString().equals("Rook"))
            return 3;
        if(target.toString().equals("Bishop"))
            return 4;
        if(target.toString().equals("Queen"))
            return 5;
        if(target.toString().equals("King"))
            return 6;

        return 1;
    }

    public int getVisitCount() {
        return visit_count;
    }

    // new stuff
    // ~~~~~~~~~~~~~~~~~

    public int getOpponent() {
        return playerNo * -1;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public void setPlayerNo(int playerNo) {
        this.playerNo = playerNo;
    }

    // ~~~~~~~~~~~~~~~~~
    @Override
    public String toString() {
        return "State{" +
                "move=" + move_coords +
                ", score=" + score +
                '}';
    }

    public double getScore() {
        return score;
    }

    public void increaseComulativeScore(double comulativescore, double increase) {
        this.comulativescore = comulativescore + increase;
    }

    public double getComulativescore() {
        return comulativescore;
    }

    public void setComulativescore(double comulativescore) {
        this.comulativescore = comulativescore;
    }

    public String getMoveCoords() {
        return move_coords;
    }

    public void incrementVisit() {
        visit_count++;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore(double additional_score) {
        score += additional_score;
    }


    public void togglePlayer() {

        playerNo *= -1;
        Board.turn=playerNo;
    }

    public void setMove_indexes(String move_indexes) {
        this.move_indexes = move_indexes;
    }

    public void randomPlay() {
        List<State> states = getAllPossibleStates(0);
        if(GlobalVariables.PRINTLOG)System.out.println("RANDOM PLAY");
        List<Node<State>> nodeList = new LinkedList<>();
        //List<double[]> logistic_score_list = new LinkedList<>();
        for (State s : states) {
            Node<State> n = new Node<State>(s, -1);
            nodeList.add(n);
        }
        /*
        double max_score = -1000;
        int move_index = -1;
        Logistic l = new Logistic(896);

        for (int i =0 ; i <states.size();i++) {
            State s = states.get(i);
            Node<State> n = new Node<State>(s, -1);
            nodeList.add(n);
            double [] x =DataFrame.convert_to_vector(s,s.getMoveCoords().replaceAll("[^0-9]", "").substring(0, 4));
            double tmp_score = l.predict(x,true,s.getMoveCoords());
            if(tmp_score>max_score){
                max_score=tmp_score;
                move_index=i;
            }
        }

         */
        State tmp;
       // int val = new Random().nextInt(20);
    //    if(val<3){
     //       tmp = states.get(move_index);;
     //   }else {


            if (GlobalVariables.withBias) {
                //System.out.println("Using Bias");
                StateSelector selector = new StateSelector(nodeList, 1);
                tmp = selector.getOutput().getData();//states.get(rand.nextInt(states.size()));
            } else {
                //  System.out.println("Not Using Bias");
                Random rand = new Random();
                tmp = states.get(rand.nextInt(states.size()));
            }

            if (GlobalVariables.PRINTLOG) System.out.println(tmp.getMoveCoords());

       // }


       // System.out.println(tmp.getMoveCoords());
        board = tmp.getBoard();
        setScore(tmp.getScore());

    }

    public Node randomBiasSelect(Node parent){
        StateSelector selector = new StateSelector(parent.getChildren(),1);
        return selector.getOutput();

    }
}

