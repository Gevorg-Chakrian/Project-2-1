package AI.Tree;

import AI.State;
import AI.StateSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node<T> {

    private T data = null;
    private int depth = 0;
    private double score = 0;
    private final List<Node<T>> children = new ArrayList<>();
    private Node<T> parent = null;



    /**
     *
     * @param data int first one size
     */
    public Node(T data, int depth) {
        this.data = data;
        this.depth = depth;
    }

    public Node getRoot() {
        if(getParent()== null) return this;

        Node<T> temp;
        do{
            temp = getParent();
        }while(temp.getParent() != null);
        return temp;
    }

    public Node<T> addChild(Node<T> child) {
        this.children.add(child);
        child.setParent(this);
        return child;
    }

    public void addChildren(List<Node<T>> children) {
        for (Node<T> child : children) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    public List<Node<T>> getChildren() {
        return this.children;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return this.parent;
    }

    //FIXME
    // is it really better, to do it with State Selector,less optimal?
    public Node<T> getRandomChildNode(){
       Random rand = new Random();
      int random_choice = rand.nextInt(children.size());


        return children.get(random_choice);

       // return new State().randomBiasSelect(this);
    }

    public boolean isFile(){
        return data != null;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public double getScore()
    {
        return this.score;
    }

    public int getDepth(){
        return this.depth;
    }


    public boolean hasChildren()
    {
        return children.size() > 0;
    }

    public boolean isOrphan()
    {
        return parent == null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", depth=" + depth +
                ", score=" + score +
                '}';
    }
}
