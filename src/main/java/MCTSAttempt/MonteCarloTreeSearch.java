package MCTSAttempt;

import AI.EvaluationFunction;
import AI.State;
import AI.Tree.Node;
import Chessboard.Board;
import Chessboard.DiceFace;
import Utilities.GlobalVariables;

import java.util.List;

public class MonteCarloTreeSearch  {
    static final double WIN_SCORE = GlobalVariables.WINSCORE; // probably for us not 10
    static String mcts_move;
    int opponent;

    public String findNextMove(Board board, int playerNo) {
        int real_turn = Board.turn;
        opponent = playerNo*-1; //should work?
        Tree tree = new Tree();

        State new_state = new State(board,opponent);
        new_state.setScore(new EvaluationFunction().getScore(new_state));

        Node<State> rootNode = new Node<State>(new_state,-1);
        long time = System.currentTimeMillis();
        while (System.currentTimeMillis() <  time + GlobalVariables.MCTSTIME) {

            Node<State> promisingNode = selectPromisingNode(rootNode);

            if (promisingNode.getData().getBoard().checkStatus(false)
                    == Board.IN_PROGRESS) { // no winner
                expandNode(promisingNode);
            }
            Node<State> nodeToExplore = promisingNode; //maybe should put a copy() here?
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            GlobalVariables.number_of_moves=0;
            GlobalVariables.game_start_time=System.currentTimeMillis();
            double playoutResult = simulateRandomPlayout(nodeToExplore);
            GlobalVariables.game_end_time=System.currentTimeMillis();
            if(GlobalVariables.PRINTLOG) System.out.println("PLAYOUT RESULT OF PROMISING NODE:" + promisingNode.getData().getMoveCoords());
          if(GlobalVariables.PRINTLOG)  System.out.println("PLAYOUT RESULT FROM NODE TO EXPLORE : "+ nodeToExplore.getData().getMoveCoords());
          if (GlobalVariables.PRINTLOG) System.out.println("RESULT : "+ playoutResult);

            if(GlobalVariables.PRINTLOG)System.out.println("Time of Game : " + (GlobalVariables.game_end_time-GlobalVariables.game_start_time));
            if(GlobalVariables.PRINTLOG)System.out.println("Number of moves : " + GlobalVariables.number_of_moves);
            backPropogation(nodeToExplore, (int) playoutResult);
        }

        if(GlobalVariables.PRINTLOG) System.out.println("Found winner node!");
        Node<State> winnerNode =rootNode.getChildren().get(getChildWithMaxScore(rootNode));
        tree.setRoot(winnerNode);
        if(GlobalVariables.PRINTLOG) System.out.println(winnerNode.getData().getMoveCoords()+" move coords");
        if(GlobalVariables.PRINTLOG) System.out.println(winnerNode.getData().getMoveIndexes() + " MOVE STRING");

        Board.turn=real_turn;
        mcts_move = winnerNode.getData().getMoveIndexes();
       // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ONE MCTS MOVE WAS MADE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return winnerNode.getData().getMoveCoords().replaceAll("[^0-9]", "").substring(0,4);
    }



    private double simulateRandomPlayout(Node<State> node) {
        Node<State> tempNode = new Node<State>(node.getData(),node.getDepth());
        tempNode.setParent(node.getParent());// maybe can just use node param, instead of tempNode?
        State tempState = tempNode.getData();
        int boardStatus = tempState.getBoard().checkStatus(false);
        if (boardStatus == opponent) {
            if(!tempNode.isOrphan()) {
                tempNode.getParent().getData().setScore(Integer.MIN_VALUE);
            }
            return boardStatus;
        }
        while (boardStatus == Board.IN_PROGRESS) { // no winner
            tempState.togglePlayer();
            tempState.randomPlay();
            GlobalVariables.number_of_moves++;
            boardStatus = tempState.getBoard().checkStatus(false);
            //System.out.println("STATUS : " + boardStatus);
        }

        return boardStatus;
    }


    private Node<State> selectPromisingNode(Node<State> rootNode) {
        Node<State> node = rootNode;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(Node<State> node) {
        List<State> possibleStates = node.getData().getAllPossibleStates(0);
        possibleStates.forEach(state -> {
            Node<State> newNode = new Node<State>(state,0);
            newNode.setParent(node);
            newNode.getData().setPlayerNo(node.getData().getOpponent());
            node.getChildren().add(newNode);
        });
    }

    private void backPropogation(Node<State> nodeToExplore, int playOutResult) {
        Node<State> tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getData().incrementVisit();

            if (tempNode.getData().getPlayerNo() == playOutResult) {
                tempNode.getData().addScore(WIN_SCORE);
            }else if (tempNode.getData().getOpponent()==playOutResult ){
                tempNode.getData().addScore(-WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    public int getChildWithMaxScore(Node<State>parent) {
        Board board = parent.getData().getBoard();
        int king_row = -1;
        int king_column = 1;
        for(int i = 0; i < board.getSquares().length; i++){
            for(int j = 0; j < board.getSquares().length;j++){
                if(board.getSquares()[i][j].isOccupied()){
                    if(board.getSquares()[i][j].getPiece().toString().equals("King") && board.getSquares()[i][j].getPiece().getColor()!=parent.getData().getPlayerNo()){
                        king_row=i;
                        king_column=j;
                        break;
                    }
                }
            }
        }

        double max_score = -10000;
        int max_index = -1;
        List<Node<State>> children = parent.getChildren();





        //if(true)System.out.println("Dice Number: " + DiceFace.getN());
        for (int i = 0; i < children.size();i++) {
            if(GlobalVariables.PRINTLOG) System.out.println(children.get(i));
         //   System.out.println(children.get(i).getData().getMoveCoords());
            if(!checkDiceMatch(children.get(i).getData().getMoveCoords())) {
                continue;
            }
            // if there is a possible move to get king, return that move
            if(children.get(i).getData().getMoveCoords().contains("Square{, row="+king_row+", column="+king_column+",")){

                if(GlobalVariables.PRINTLOG) System.out.println("is present");
                return i;
            }


            if(children.get(i).getData().getScore()>=max_score){
              //  System.out.println(children.get(i).getData().getMoveCoords().replaceAll("[^0-9]", "").substring(0,4));
                max_score = children.get(i).getData().getScore();
                max_index = i;
            }
        }
        /*

        // MACHINE LEARNING AGENT
        Logistic logistic = new Logistic(896);
        int max_log_index=-1;
        double max_log_score = -1000;
        for(int i =0;i<parent.getChildren().size();i++){
            double[] move1 = DataFrame.convert_to_vector(new State(board,-1),parent.getChildren().get(i).getData().getMoveCoords().replaceAll("[^0-9]", "").substring(0,4));
            double move_score =logistic.predict(move1,true);
            System.out.println("Move_score=" + move_score + "   ||The move: "+ children.get(i).getData().getMoveCoords());
            if(move_score>max_log_score){
                max_log_score=move_score;
                max_log_index=i;
            }
        }

         */


        if(max_index==-1){
            for(int i =0; i < children.size();i++){
                Node<State> child = children.get(i);
                child.getData().addScore(new EvaluationFunction().getScore(child.getData()));
                if(child.getData().getScore()>max_score){
                    max_index=i;
                }
            }
        }
       if (GlobalVariables.PRINTLOG)System.out.println("MAX CHILD : " + children.get(max_index));
       //return max_log_index; return for pure LR!
        return max_index;
    }
    public static boolean checkDiceMatch(String move_coords){

        if(move_coords.contains("Pawn") && DiceFace.getN()==1){
            return true;
        }
        else if(move_coords.contains("Knight") && DiceFace.getN()==2){
            return true;
        }
        else if(move_coords.contains("Bishop") && DiceFace.getN()==3){
            return true;
        }
        else if(move_coords.contains("Rook") && DiceFace.getN()==4){
            return true;
        }
        else if(move_coords.contains("Queen") && DiceFace.getN()==5){
            return true;
        }
        else return move_coords.contains("King") && DiceFace.getN() == 6;






    }



}
