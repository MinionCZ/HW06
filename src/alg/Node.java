package alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Node {
    private static int nodeIndex = 0;
    private final int[] keys;
    private Node leftChild, rightChild, parent;
    private int numberOfKeys;
    private final int index;
    private int leftHeight, rightHeight;

    public Node(int key, Node parent) {
        this.keys = new int[4];
        Arrays.fill(this.keys, Integer.MAX_VALUE);
        this.keys[0] = key;
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

    public int[] getKeys() {
        return keys;
    }

    public int getNumberOfKeys() {
        return numberOfKeys;
    }


    public void addKey(int key) {
        this.keys[numberOfKeys] = key;
        this.numberOfKeys++;
        Arrays.sort(keys);
    }

    public void removeKey(int key) {
        this.numberOfKeys--;
        for (int i = 0; i < this.keys.length; i++) {
            if (this.keys[i] == key) {
                this.keys[i] = Integer.MAX_VALUE;
                break;
            }
        }
        Arrays.sort(keys);
    }

    public boolean isLeaf() {
        return this.leftChild == null && this.rightChild == null;
    }

    public int getMinKey() {
        return this.keys[0];
    }

    public int getMaxKey() {
        return this.keys[numberOfKeys - 1];
    }

    public int getMostSimilarKey(int key) {
        return Math.abs(key - this.getMaxKey()) < Math.abs(key - this.getMinKey()) ? this.getMaxKey() : this.getMinKey();
    }

    public void removeAllKeys() {
        Arrays.fill(this.keys, Integer.MAX_VALUE);
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

    public int getMaxHeight() {
        return Math.max(this.rightHeight, this.leftHeight);
    }

    public int getMinHeight() {
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
                ",minKey= " + this.getMinKey() +
                ",maxKey= " + this.getMaxKey() +
                '}';
    }
}
