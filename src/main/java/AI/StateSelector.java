package AI;

import AI.Tree.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StateSelector {

    private Node<State> output=null;
    private List<Node<State>> outputArray=null;
    private int outputSize;

    public StateSelector(List<Node<State>> nodeList, int outputSize){
        this.outputSize=outputSize;
        outputArray = new LinkedList<>();
        List<Integer> expandedList = new LinkedList<>();


        int index =0;
        for(Node<State> node : nodeList)
        {
            double nodeScore = node.getData().getScore();
            //System.out.println("SCORE= " + nodeScore);

            //useless but you never know
            if(nodeScore <= 0)
            {
                nodeScore = 1.0;
            }
            else if( nodeScore >0 && nodeScore <=0.1)
            {
                nodeScore = nodeScore * 2;
            }
            else if(nodeScore>0.1 ){
                nodeScore=nodeScore*3;
            }


            for (int i = 0; i < nodeScore; i++) {
                expandedList.add(index);
            }

            index++;
        }

        //System.out.println("ExpandedList size: " + expandedList.size());
        if(outputSize==1)
        {
            Random rand = new Random();
            int randomIndex = rand.nextInt(expandedList.size()); //changed this a bit..
            output = nodeList.get(expandedList.get(randomIndex));
        }
        else {
            for (int i = 0; i < outputSize; i++) {
                int randomIndex = (int) (Math.random()*expandedList.size());
                outputArray.add(nodeList.get(expandedList.get(randomIndex)));
            }
        }


    }

    public Node<State> getOutput() {
        return output;
    }

    public List<Node<State>> getOutputArray() {
        return outputArray;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public int getOutputSize() {
        return outputSize;
    }
}
