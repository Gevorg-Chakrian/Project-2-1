package AI.TreeVisualizator;

import AI.State;
import AI.Tree.Node;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class Controller extends JFrame {

    public Controller(Node<State> root)
    {
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode("Root");
        JTree tree = new JTree(rootTreeNode);

        rootTreeNode.setUserObject("Root CS:" + root.getData().getComulativescore());
        populateTree(root, rootTreeNode);

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        JScrollPane canvas = new JScrollPane(tree);
        this.add(canvas);
        this.setSize(200,900);
        this.setVisible(true);
    }


    public void populateTree(Node<State> node, DefaultMutableTreeNode treeNode)
    {
        for(Node<State> child : node.getChildren())
        {
            DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(child.toString());
            childTreeNode.setUserObject(child.getData().toString() + " CS:" + child.getData().getComulativescore());
            treeNode.add(childTreeNode);
            if(child.getChildren().size() != 0)
            {
                populateTree(child,childTreeNode);
            }
        }
    }



}
