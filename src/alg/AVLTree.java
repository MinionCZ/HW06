package alg;

import java.util.ArrayList;
import java.util.Collections;

public class AVLTree {

    private Node root;
    private boolean[] keys;
    private int numberOfKeys;
    private static final int LEFT_ROTATION = 1;
    private static final int RIGHT_ROTATION = 2;
    private static final int LEFT_STEP = 1;
    private static final int RIGHT_STEP = 2;

    public AVLTree() {
        keys = new boolean[1000001];
    }

    public void insert(int key) {
        System.out.println("inserting = " + key);
        if (this.keys[key]) return;
        if (this.numberOfKeys == 0) {
            root = new Node(key, null);
        } else if (this.numberOfKeys < 3) {
            root.addKey(key);
        } else {
            insertKeyIntoTree(root, key);
        }
        this.keys[key] = true;
        this.numberOfKeys++;
    }

    private void insertKeyIntoTree(Node actualNode, int key) {
        if (actualNode.isLeaf()) {

            //insert into leaf
            actualNode.addKey(key);
            if (actualNode.getNumberOfKeys() > 3) {
                //leaf split
                actualNode.setRightChild(new Node(actualNode.getMaxKey(), actualNode));
                actualNode.setLeftChild(new Node(actualNode.getMinKey(), actualNode));
                actualNode.removeKey(actualNode.getMaxKey());
                actualNode.removeKey(actualNode.getMinKey());
                this.balanceTreeAfterInsert(actualNode);
            }
        } else {
            //search nodes and insert
            if (actualNode.getMinKey() > key) {
                this.insertKeyIntoTree(actualNode.getLeftChild(), key);
            } else if (actualNode.getMaxKey() < key) {
                this.insertKeyIntoTree(actualNode.getRightChild(), key);
            } else {
                //insert into current node
                if (actualNode.getLeftHeight() <= actualNode.getRightHeight()) {
                    int min = actualNode.getMinKey();
                    actualNode.removeKey(min);
                    actualNode.addKey(key);
                    this.insertKeyIntoTree(actualNode.getLeftChild(), min);
                } else {
                    int max = actualNode.getMaxKey();
                    actualNode.removeKey(max);
                    actualNode.addKey(key);
                    this.insertKeyIntoTree(actualNode.getRightChild(), max);
                }
            }
        }
    }

    private void balanceTreeAfterInsert(Node actualNode) {
        int firstPathStep, secondPathStep = 0;
        Node previous = actualNode;
        while (actualNode != null) {
            int leftHeight = actualNode.getLeftHeight();
            int rightHeight = actualNode.getRightHeight();
            firstPathStep = secondPathStep;
            secondPathStep = previous == actualNode.getLeftChild() ? RIGHT_STEP : LEFT_STEP;

            if (Math.abs(leftHeight - rightHeight) > 1) {
                //rotate
                if (firstPathStep == LEFT_STEP && secondPathStep == LEFT_STEP) {
                    rotate(actualNode, previous, LEFT_ROTATION);
                } else if (firstPathStep == RIGHT_STEP && secondPathStep == RIGHT_STEP) {
                    rotate(actualNode, previous, RIGHT_ROTATION);
                } else if (firstPathStep == RIGHT_STEP && secondPathStep == LEFT_STEP) {
                    //RL rotation
                    Node rightRotationNode = previous.getLeftChild();
                    rotate(previous, rightRotationNode, RIGHT_ROTATION);
                    rotate(actualNode, rightRotationNode, LEFT_ROTATION);
                } else {
                    //LR rotation
                    Node leftRotationNode = previous.getRightChild();
                    rotate(previous, leftRotationNode, LEFT_ROTATION);
                    rotate(actualNode, leftRotationNode, RIGHT_ROTATION);
                }
            }
            previous = actualNode;
            actualNode = actualNode.getParent();
        }
    }

    private void rotate(Node actualNode, Node previousNode, int rotationType) {
        Node parent = actualNode.getParent();
        boolean isActualNodeLeftChild = (parent != null) && actualNode == parent.getLeftChild();
        switch (rotationType) {
            case LEFT_ROTATION:
                actualNode.setRightChild(previousNode.getLeftChild());
                actualNode.getRightChild().setParent(actualNode);
                previousNode.setParent(actualNode.getParent());
                actualNode.setParent(previousNode);
                previousNode.setLeftChild(actualNode);
                break;
            case RIGHT_ROTATION:
                actualNode.setLeftChild(previousNode.getRightChild());
                actualNode.getLeftChild().setParent(actualNode);
                previousNode.setParent(actualNode.getParent());
                actualNode.setParent(previousNode);
                previousNode.setRightChild(actualNode);
                break;
        }
        if (previousNode.getParent() == null) {
            this.root = previousNode;
        } else if (isActualNodeLeftChild) {
            parent.setLeftChild(previousNode);
        } else {
            parent.setRightChild(previousNode);
        }
    }

    public void delete(int key) {
        if (!this.keys[key]) return;
        if (this.numberOfKeys <= 3) {
            this.root.removeKey(key);
        } else {
            this.deleteKey(this.root, key);
        }
        this.keys[key] = false;
        this.numberOfKeys--;
    }

    private void deleteKey(Node actualNode, int key) {
        if (actualNode.getMaxKey() >= key && key >= actualNode.getMinKey()) {
            //key is in this node
            if (actualNode.isLeaf()) {
                this.deleteKeyFromLeaf(actualNode, key);
            } else {
                this.deleteKeyInInnerNode(actualNode, key);
            }


        } else if (actualNode.getMaxKey() < key) {
            this.deleteKey(actualNode.getRightChild(), key);
        } else {
            this.deleteKey(actualNode.getLeftChild(), key);
        }
    }

    private void deleteKeyInInnerNode(Node innerNode, int key) {
        int rightHeight = innerNode.getRightHeight();
        int leftHeight = innerNode.getLeftHeight();
        innerNode.removeKey(key);
        int subKey;
        if (leftHeight <= rightHeight) {
            Node subNode = innerNode.getRightChild();
            while (!subNode.isLeaf()) {
                subNode = subNode.getLeftChild();
            }
            subKey = subNode.getMinKey();
            innerNode.addKey(subKey);
            this.deleteKey(innerNode.getRightChild(), subKey);
        } else {
            Node subNode = innerNode.getLeftChild();
            while (!subNode.isLeaf()) {
                subNode = subNode.getRightChild();
            }
            subKey = subNode.getMinKey();
            innerNode.addKey(subKey);
            this.deleteKey(innerNode.getLeftChild(), subKey);
        }
    }

    private void deleteKeyFromLeaf(Node leaf, int key) {
        leaf.removeKey(key);
        if (leaf.getNumberOfKeys() < 1) {
            Node parent = leaf.getParent();
            boolean isLeafSideLeft = parent.getLeftChild() == leaf;
            Node sibling = isLeafSideLeft ? parent.getRightChild() : parent.getLeftChild();
            if (sibling.isLeaf()) {
                if (sibling.getNumberOfKeys() > 1) {
                    //key redistribution
                    ArrayList<Integer> keys = new ArrayList<>();
                    keys.addAll(parent.getKeys());
                    keys.addAll(sibling.getKeys());
                    Collections.sort(keys);
                    parent.removeAllKeys();
                    sibling.removeAllKeys();
                    if (isLeafSideLeft) {
                        leaf.addKey(keys.get(0));
                        parent.addKey(keys.get(1));
                        parent.addKey(keys.get(2));
                        for (int i = 3; i < keys.size(); i++) {
                            sibling.addKey(keys.get(i));
                        }
                    } else {
                        int size = keys.size();
                        leaf.addKey(keys.get(size - 1));
                        parent.addKey(keys.get(size - 2));
                        parent.addKey(keys.get(size - 3));
                        for (int i = size - 4; i >= 0; i--) {
                            sibling.addKey(keys.get(i));
                        }
                    }
                } else {
                    //node contraction
                    parent.addKey(sibling.getMinKey());
                    parent.setRightChild(null);
                    parent.setLeftChild(null);
                    //Todo add balancing
                }
            } else {
                int mostSimilarKey = parent.getMostSimilarKey(key);
                leaf.addKey(mostSimilarKey);
                this.deleteKey(parent, mostSimilarKey);
            }

        }
    }


    public void printTree() {
        this.travelTree(this.root);
    }

    private void travelTree(Node actualNode) {
        System.out.println(actualNode);
        if (!actualNode.isLeaf()) {
            travelTree(actualNode.getLeftChild());
            travelTree(actualNode.getRightChild());
        }
    }
}
