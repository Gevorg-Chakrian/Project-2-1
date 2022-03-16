package AI.Tree;

import AI.State;
import AI.StateSelector;
import AI.TreeVisualizator.Controller;
import Chessboard.Board;
import Chessboard.Square;
import Utilities.GlobalVariables;

import java.util.LinkedList;

import java.util.ArrayList;
import java.util.List;
 // Alessandro's MCTS ohoh and a bootle of rum
public class MCTS {

    private final Node<State> rootOfEverything;
    private int nodeCounter = 1;
    private int real_board_turn;

    private int counter=0;


    public MCTS(State basestate) {
        real_board_turn = Board.turn;

        this.rootOfEverything = new Node<State>(basestate, 0);

        if(GlobalVariables.PRINTLOG) System.out.println("UNLEASHING HELL:");

        long startTime = System.nanoTime();
        selection(rootOfEverything);
        long endTime = System.nanoTime();

        expansion(3,selectHighestScoreState(this.rootOfEverything));
        //expansion(5,this.rootOfEverything);
        long duration = (endTime - startTime) / 1000000;
        if(GlobalVariables.PRINTLOG) System.out.println("Duration: " + duration + "ms for total depth of 2 and for a total of " + nodeCounter + " nodes:)");

        comulativeAssignator();

        if(GlobalVariables.PRINTLOG) System.out.println("Backtracking touched nodes are: " + counter);

        Controller controller = new Controller(rootOfEverything);
        if(GlobalVariables.PRINTLOG) System.out.println("Total nodes = " + getAllNodes(rootOfEverything).size() );
    }

    private void selection(Node<State> parent)
    {

        List<State> stateList = parent.getData().getAllPossibleStates(0);
        //System.out.println("GetAllPossibleState size: " + stateList.size());
        int counter = 0 ;
        for(State s : stateList){
            //  if(s.toString().contains("Pawn")){
            if(GlobalVariables.PRINTLOG) System.out.println("COUNTER = "+ counter);
            if(GlobalVariables.PRINTLOG) System.out.println(s);
            counter+=1;
            if(GlobalVariables.PRINTLOG) System.out.println("\n************************************************************************************");
            //    }
        }
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n\n");
        List<Node<State>> nodeList = new ArrayList<>();
        for (State s : stateList) {
            nodeCounter++;
            Node<State> n = new Node<State>(s, parent.getDepth() + 1);
            nodeList.add(n);
        }
        LinkedList<Node<State>> node_choice_list = new LinkedList<>();

        for (State state : stateList){
            if(state.getMoveCoords().contains("White") && real_board_turn==1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }else if (state.getMoveCoords().contains("Black") && real_board_turn==-1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }
        }

        //System.out.println("node_choice_list size: " + nodeList.size());
        StateSelector stateSelector = new StateSelector(nodeList,2);
        if(GlobalVariables.PRINTLOG) System.out.println("THE MOVE CHOSEN IS : "+ stateSelector.getOutput());
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n\\\n");
        if(real_board_turn==1){
            real_board_turn=-1;
        }else real_board_turn=1;

        parent.addChildren(nodeList);

        double turnMaxScore = 0;

        for(Node<State> stateNode : nodeList)
        {
            double score = stateNode.getData().getScore();
            if(score >= turnMaxScore)
                turnMaxScore=score;

        }
        if(GlobalVariables.PRINTLOG) System.out.println("Max score of round is: " + turnMaxScore);
        if (turnMaxScore <= 40) {
            if(stateSelector.getOutputSize()==1)
            {
                if(stateSelector.getOutput().getDepth()< 10)
                    selection(stateSelector.getOutput());
            }
            else
            {
                //System.out.println("Dupling ");
                if(stateSelector.getOutputArray().get(0).getDepth()< 10) {
                    for (Node<State> st : stateSelector.getOutputArray())
                        selection(st);
                }
            }
        }

    }

    private void expansion(int iterations, Node<State> parent)
    {
        List<State> stateList = parent.getData().getAllPossibleStates(0);
        int counter = 0 ;
        for(State s : stateList){
            //  if(s.toString().contains("Pawn")){
            if(GlobalVariables.PRINTLOG) System.out.println("COUNTER = "+ counter);
            if(GlobalVariables.PRINTLOG) System.out.println(s);
            counter+=1;
            if(GlobalVariables.PRINTLOG) System.out.println("\n************************************************************************************");
            //    }
        }
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n\n");
        List<Node<State>> nodeList = new ArrayList<>();
        for (State s : stateList) {
            nodeCounter++;
            Node<State> n = new Node<State>(s, parent.getDepth() + 1);
            nodeList.add(n);
        }
        LinkedList<Node<State>> node_choice_list = new LinkedList<>();

        for (State state : stateList){
            if(state.getMoveCoords().contains("White") && real_board_turn==1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }else if (state.getMoveCoords().contains("Black") && real_board_turn==-1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }
        }


        StateSelector stateSelector = new StateSelector(node_choice_list,2);
        if(GlobalVariables.PRINTLOG) System.out.println("THE MOVE CHOSEN IS : "+ stateSelector.getOutput());
        if(GlobalVariables.PRINTLOG) System.out.println("\n\n\\\n");
        if(real_board_turn==1){
            real_board_turn=-1;
        }else real_board_turn=1;

        parent.addChildren(node_choice_list);

        if (iterations != 0) {
            for(Node<State> st : stateSelector.getOutputArray())
            {
                expansion(iterations-1,st);
            }
        }
    }


    private Node<State> selectHighestScoreState(Node<State> rootOfEverything)
    {
        double maxScore =0;
        Node<State> maxState = null;

        for(Node<State> stateNode : getAllNodes(rootOfEverything))
        {
            if(stateNode.getData().getScore() >= maxScore)
            {
                maxState = stateNode;
            }
        }

        return maxState;
    }


    private List<Node<State>> getChildlessBoys(List<Node<State>> allNodes)
    {
        List<Node<State>> nodes = new LinkedList<>();
        for(Node<State> child : allNodes)
        {
            if(!child.hasChildren())
            {
                nodes.add(child);
            }

        }
        return nodes;
    }

    private void comulativeAssignator()
    {
        List<Node<State>> states = getChildlessBoys(getAllNodes(rootOfEverything));
        counter++;
        for(Node<State> child : states)
        {
            recursiveFatherhood(child);
        }
    }

    private void recursiveFatherhood(Node<State> child)
    {

        if(!child.isOrphan())
        {
            counter++;
            child.getParent().getData().increaseComulativeScore(child.getData().getComulativescore(),child.getData().getScore());
            recursiveFatherhood(child.getParent());
            //System.out.println("Current state Child Score " + child.getData().getComulativescore() + " Father score " + child.getParent().getData().getComulativescore());
        }

    }

    private List<Node<State>> getAllNodes(Node<State> root)
    {
        List<Node<State>> nodes = new LinkedList<>();
        nodes.addAll(root.getChildren());
        for(Node<State> child : root.getChildren())
        {
            nodes.addAll(getAllNodes(child));

        }
        return nodes;
    }

    private boolean checkKings(Node<State> nodeState)
    {
        State state = nodeState.getData();
        Square[][] squares = state.getBoard().getSquares();

        boolean isBlackKing = false;
        boolean isWhiteKing = false;

        for(Square[] squareV : squares)
        {
            for(Square s : squareV)
            {
                if(s.isOccupied() && s.getPiece().getColor()==1&& s.getPiece().toString().equals("King"))
                {
                    isWhiteKing=true;
                }
                else if(s.isOccupied() && s.getPiece().getColor()==-1&& s.getPiece().toString().equals("King"))
                {
                    isBlackKing=true;
                }
            }
        }

        if(!isBlackKing || !isWhiteKing)
            if(GlobalVariables.PRINTLOG) System.out.println("One KinG MISSING!!");



        return isWhiteKing && isBlackKing;
    }

    public Node<State> getRootOfEverything() {
        return rootOfEverything;
    }
}

/*  DAN's OLD MCTS
package AI.Tree;

import AI.State;
import Chessboard.Board;

import java.util.LinkedList;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

public class MCTS {

    private Node<State> rootOfEverything;
    private int nodeCounter = 1;
    private int real_board_turn;

    public MCTS(State basestate) {
        real_board_turn = Board.turn;

        this.rootOfEverything = new Node<State>(basestate, 0);

        System.out.println("UNLEASHING HELL:");

        long startTime = System.nanoTime();
        feedTheTree(rootOfEverything);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;
        System.out.println("Duration: " + duration + "ms for total depth of 2 and for a total of " + nodeCounter + " nodes:)");
    }

    private void feedTheTree(Node<State> parent) {
        List<State> stateList = parent.getData().getAllPossibleStates();

        System.out.println("\n**************************************");
        List<Node<State>> nodeList = new ArrayList<>();
        for (State s : stateList) {
            nodeCounter++;
            Node<State> n = new Node<State>(s, parent.getDepth() + 1);
            nodeList.add(n);
        }
        Random rand =new Random();
        LinkedList<Node> node_choice_list = new LinkedList<>();

        for (State state : stateList){
            if(state.getMove_coords().contains("White") && real_board_turn==1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }else if (state.getMove_coords().contains("Black") && real_board_turn==-1){
                Node<State> n = new Node<State>(state, parent.getDepth() + 1);
                node_choice_list.add(n);
            }
        }


        int random =rand.nextInt(node_choice_list.size());
        //System.out.println("THE MOVE  CHOSEN IS : "+ node_choice_list.get(random));
        System.out.println("\n");
        if(real_board_turn==1){
            real_board_turn=-1;
        }else real_board_turn=1;




        parent.addChild(node_choice_list.get(random));

        System.out.println(parent.getChildren());

        if (parent.getDepth() + 1 < 60) {
            for (Node<State> ns : parent.getChildren()) {
                feedTheTree(ns);
            }
        }

    }

    public Node<State> getRootOfEverything() {
        return rootOfEverything;
    }
}
 
 */