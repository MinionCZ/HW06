package alg;

import java.util.ArrayList;
import java.util.Collections;

public class Node {
    private static int nodeIndex = 0;
    private final ArrayList<Integer> keys;
    private Node leftChild, rightChild, parent;
    private int numberOfKeys;
    private final int index;
    private int leftHeight, rightHeight;

    public Node(int key, Node parent) {
        this.keys = new ArrayList<>(4);
        this.keys.add(key);
        this.numberOfKeys = 1;
        this.parent = parent;
        this.index = nodeIndex++;
        this.leftHeight = 0;
        this.rightHeight = 0;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    private int getHeightOfNode(Node node) {
        if (node == null) return 0;
        return Math.max(this.getHeightOfNode(node.getLeftChild()), this.getHeightOfNode(node.getRightChild())) + 1;
    }

    public void addKey(int key) {
        this.numberOfKeys++;
        this.keys.add(key);
        Collections.sort(this.keys);
    }

    public void removeKey(int key) {
        this.numberOfKeys--;
        this.keys.remove((Integer) key);
    }

    public boolean isLeaf() {
        return this.leftChild == null && this.rightChild == null;
    }

    public int getMinKey() {
        return this.keys.get(0);
    }

    public int getMaxKey() {
        return this.keys.get(this.numberOfKeys - 1);
    }

    public int getMostSimilarKey(int key) {
        return Math.abs(key - this.getMaxKey()) < Math.abs(key - this.getMinKey()) ? this.getMaxKey() : this.getMinKey();
    }

    public void removeAllKeys() {
        this.keys.clear();
        this.numberOfKeys = 0;
    }

    public int getLeftHeight() {
        return leftHeight;
    }

    public void setLeftHeight(int leftHeight) {
        this.leftHeight = leftHeight;
    }

    public int getRightHeight() {
        return rightHeight;
    }

    public void setRightHeight(int rightHeight) {
        this.rightHeight = rightHeight;
    }

    public void decrementLeftHeight() {
        this.leftHeight--;
    }

    public void decrementRightHeight() {
        this.rightHeight--;
    }

    public void incrementLeftHeight() {
        this.leftHeight++;
    }

    public void incrementRightSide() {
        this.rightHeight++;
    }
    public int getMaxHeight(){
        return Math.max(this.rightHeight, this.leftHeight);
    }
    public int getMinHeight(){
        return Math.min(this.rightHeight, this.leftHeight);
    }


    @Override
    public String toString() {
        return "Node{" +
                "keys=" + keys +
                ",index= " + index +
                ", parent=" + (parent != null ? parent.index : null) +
                ", numberOfKeys=" + numberOfKeys +
                ",leftHeight= " + this.getLeftHeight() +
                ",rightHeight= " + this.getRightHeight() +
                '}';
    }
}
