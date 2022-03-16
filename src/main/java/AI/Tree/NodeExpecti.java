package AI.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NodeExpecti<T> {

    private T data = null;
    private int depth = 0;
    private double score = 0;
    private List<NodeExpecti<T>> children = new ArrayList<>();
    private NodeExpecti<T> parent = null;



    /**
     *
     * @param data int first one size
     */
    public NodeExpecti(T data, int depth) {
        this.data = data;
        this.depth = depth;
    }

    public NodeExpecti getRoot() {
        if(getParent()== null) return this;

        NodeExpecti<T> temp;
        do{
            temp = getParent();
        }while(temp.getParent() != null);
        return temp;
    }

    public NodeExpecti<T> addChild(NodeExpecti<T> child) {
        this.children.add(child);
        child.setParent(this);
        return child;
    }

    public void addChildren(List<NodeExpecti<T>> children) {
        for (NodeExpecti<T> child : children) {
            child.setParent(this);
            this.children.add(child);
        }
    }

    public List<NodeExpecti<T>> getChildren() {
        return this.children;
    }

    public T getState() {
        return this.data;
    }

    public void setState(T data) {
        this.data = data;
    }

    public void setParent(NodeExpecti<T> parent) {
        this.parent = parent;
    }

    public NodeExpecti<T> getParent() {
        return this.parent;
    }

    //FIXME
    // is it really better, to do it with State Selector,less optimal?
    public NodeExpecti<T> getRandomChildNode(){
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
