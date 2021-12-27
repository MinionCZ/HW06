package alg;

import java.util.ArrayList;
import java.util.Collections;

public class AVLTree {

    private Node root;
    private final boolean[] keys;
    private int numberOfKeys;
    private static final int LEFT_ROTATION = 1;
    private static final int RIGHT_ROTATION = 2;
    private static final int LEFT_STEP = 1;
    private static final int RIGHT_STEP = 2;
    private int numberOfNodes = 0;
    private final int[] numberOfLeafs = new int[3];

    public AVLTree() {
        keys = new boolean[1000001];
    }

    public void insert(int key) {
//        System.out.println("inserting = " + key);
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
            firstPathStep = secondPathStep;
            secondPathStep = previous == actualNode.getLeftChild() ? RIGHT_STEP : LEFT_STEP;
            actualNode.setLeftHeight(actualNode.getLeftChild().getMaxHeight() + 1);
            actualNode.setRightHeight(actualNode.getRightChild().getMaxHeight() + 1);
            if (Math.abs(actualNode.getLeftHeight() - actualNode.getRightHeight()) > 1) {
                //rotate
                int minHeight = actualNode.getMinHeight();
                if (firstPathStep == LEFT_STEP && secondPathStep == LEFT_STEP) {
                    rotate(actualNode, previous, LEFT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    previous.setLeftHeight(minHeight + 1);
                    previous.setRightHeight(minHeight + 1);
                } else if (firstPathStep == RIGHT_STEP && secondPathStep == RIGHT_STEP) {
                    rotate(actualNode, previous, RIGHT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    previous.setLeftHeight(minHeight + 1);
                    previous.setRightHeight(minHeight + 1);
                } else if (firstPathStep == RIGHT_STEP && secondPathStep == LEFT_STEP) {
                    //RL rotation
                    Node rightRotationNode = previous.getLeftChild();
                    rotate(previous, rightRotationNode, RIGHT_ROTATION);
                    rotate(actualNode, rightRotationNode, LEFT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    previous.setLeftHeight(minHeight);
                    previous.setRightHeight(minHeight);
                    rightRotationNode.setRightHeight(minHeight + 1);
                    rightRotationNode.setLeftHeight(minHeight + 1);
                } else {
                    //LR rotation
                    Node leftRotationNode = previous.getRightChild();
                    rotate(previous, leftRotationNode, LEFT_ROTATION);
                    rotate(actualNode, leftRotationNode, RIGHT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    previous.setLeftHeight(minHeight);
                    previous.setRightHeight(minHeight);
                    leftRotationNode.setRightHeight(minHeight + 1);
                    leftRotationNode.setLeftHeight(minHeight + 1);
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
        System.out.println("delete " + key);
        printTree();
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
            subKey = subNode.getMaxKey();
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
                    this.balanceTreeAfterDelete(parent);
                }
            } else {
                int mostSimilarKey = parent.getMostSimilarKey(key);
                leaf.addKey(mostSimilarKey);
                this.deleteKey(parent, mostSimilarKey);
            }

        }
    }

    private void balanceTreeAfterDelete(Node actualNode) {
        while (actualNode != null) {
            int leftHeight = actualNode.isLeaf() ? 0 : actualNode.getLeftChild().getMaxHeight() + 1;
            int rightHeight = actualNode.isLeaf() ? 0 : actualNode.getRightChild().getMaxHeight() + 1;
            actualNode.setLeftHeight(leftHeight);
            actualNode.setRightHeight(rightHeight);
            if (Math.abs(actualNode.getLeftHeight() - actualNode.getRightHeight()) > 1) {
                //rotation must be done
                int secondStep = actualNode.getRightHeight() < actualNode.getLeftHeight() ? RIGHT_STEP : LEFT_STEP;
                Node child = secondStep == RIGHT_STEP ? actualNode.getLeftChild() : actualNode.getRightChild();
                int firstStep;
                if (secondStep == RIGHT_STEP) {
                    firstStep = child.getRightHeight() <= child.getLeftHeight() ? RIGHT_STEP : LEFT_STEP;
                } else {
                    firstStep = child.getRightHeight() < child.getLeftHeight() ? RIGHT_STEP : LEFT_STEP;
                }
                int minHeight = actualNode.getMinHeight();
                if (firstStep == LEFT_STEP && secondStep == LEFT_STEP) {
//                    System.out.println("LEFT");
                    rotate(actualNode, child, LEFT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    child.setLeftHeight(minHeight + 1);
                    child.setRightHeight(minHeight + 1);
                } else if (firstStep == RIGHT_STEP && secondStep == RIGHT_STEP) {
//                    System.out.println("RIGHT");
                    rotate(actualNode, child, RIGHT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    child.setLeftHeight(minHeight + 1);
                    child.setRightHeight(minHeight + 1);
                } else if (firstStep == RIGHT_STEP && secondStep == LEFT_STEP) {
                    //RL rotation
//                    System.out.println("RIGHT LEFT");
                    Node rightRotationNode = child.getLeftChild();
                    rotate(child, rightRotationNode, RIGHT_ROTATION);
                    rotate(actualNode, rightRotationNode, LEFT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    child.setLeftHeight(minHeight);
                    child.setRightHeight(minHeight);
                    rightRotationNode.setRightHeight(minHeight + 1);
                    rightRotationNode.setLeftHeight(minHeight + 1);
                    actualNode = rightRotationNode;
                } else {
                    //LR rotation
//                    System.out.println("LEFT RIGHT");
                    Node leftRotationNode = child.getRightChild();
                    rotate(child, leftRotationNode, LEFT_ROTATION);
                    rotate(actualNode, leftRotationNode, RIGHT_ROTATION);
                    actualNode.setRightHeight(minHeight);
                    actualNode.setLeftHeight(minHeight);
                    child.setLeftHeight(minHeight);
                    child.setRightHeight(minHeight);
                    leftRotationNode.setRightHeight(minHeight + 1);
                    leftRotationNode.setLeftHeight(minHeight + 1);
                    actualNode = leftRotationNode;
                }
            }
            actualNode = actualNode.getParent();
        }
    }


    public void printTree() {
        this.travelTree(this.root);
        System.out.println(this.numberOfNodes + " " + this.numberOfLeafs[0] + " " + this.numberOfLeafs[1] + " " + this.numberOfLeafs[2]);
    }

    private void travelTree(Node actualNode) {
        System.out.println(actualNode);
        this.numberOfNodes++;
        if (!actualNode.isLeaf()) {
            travelTree(actualNode.getLeftChild());
            travelTree(actualNode.getRightChild());
        } else {
            this.numberOfLeafs[actualNode.getNumberOfKeys() - 1]++;
        }
    }
}
