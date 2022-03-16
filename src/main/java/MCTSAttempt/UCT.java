package MCTSAttempt;

import AI.State;
import AI.Tree.Node;
import Utilities.DataFrame;
import Utilities.GlobalVariables;
import Utilities.Logistic;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class UCT {
    public static double uctValue(
            int totalVisit, double nodeWinScore, int nodeVisit,State s,Logistic logistic) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        double move_score;
        if(GlobalVariables.withProgressiveBias) {
            double[] move1 = DataFrame.convert_to_vector(s, s.getMoveCoords().replaceAll("[^0-9]", "").substring(0, 4));
            move_score = logistic.predict(move1, true, s.getMoveCoords());
        }else move_score=0;
       if (GlobalVariables.PRINTLOG) System.out.println("Move_score=" + move_score + "   ||The move: "+ s.getMoveCoords());
       if(GlobalVariables.PRINTLOG) System.out.println("NORMAL UCT : " + ((double) nodeWinScore / (double) nodeVisit)
                + GlobalVariables.EXPLORAITONCOEFFICIENT * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit));

        return ((double) nodeWinScore / (double) nodeVisit)
                + GlobalVariables.EXPLORAITONCOEFFICIENT * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit) +(move_score*1.4/(nodeVisit+1));
    }

    public static Node<State> findBestNodeWithUCT(Node<State> node) {
        if(GlobalVariables.PRINTLOG) System.out.println("~~~~~~~~~~~~~~~~~~UCT~~~~~~~~~~~~~~~~~~");
        LinkedList<Node<State>> legalStates = new LinkedList<>();
        //System.out.println("LEGAL STATES !!");
        for( Node<State> child : node.getChildren()){
            State s =child.getData();
            if(MonteCarloTreeSearch.checkDiceMatch(s.getMoveCoords())){
                legalStates.add(new Node<>(s,-1));

                //System.out.println(s.getMoveCoords());
            }

        }
        int parentVisit = node.getData().getVisitCount();
        /*
        return Collections.max(
                node.getChildren(),
                Comparator.comparing(c -> uctValue(parentVisit,
                        c.getData().getScore(), c.getData().getVisitCount())));

         */
        Logistic logistic = new Logistic(896);
        return Collections.max(
                legalStates,
                Comparator.comparing(c -> uctValue(parentVisit,
                        c.getData().getScore(), c.getData().getVisitCount(),c.getData(),logistic)));
    }




}
