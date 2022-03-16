package MCTSAttempt;

import AI.State;
import AI.Tree.Node;

public class Tree {
    Node<State> root;


    public Node<State> getRoot() {
        return root;
    }

    public void setRoot(Node<State> root) {
        this.root = root;
    }
}
