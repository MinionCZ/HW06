package alg;

public class AVLTree {

    private Node root;
    private boolean[] keys;
    private int numberOfKeys;

    public AVLTree() {
        keys = new boolean[1000001];
    }

    public void insert(int key) {
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
            actualNode.addKey(key);
            if (actualNode.getNumberOfKeys() > 3) {
                actualNode.setRightChild(new Node(actualNode.getMaxKey(), actualNode));
                actualNode.setLeftChild(new Node(actualNode.getMinKey(), actualNode));
                actualNode.removeKey(actualNode.getMaxKey());
                actualNode.removeKey(actualNode.getMinKey());
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
    public void printTree(){
        this.travelTree(this.root);
    }

    private void travelTree(Node actualNode){
        System.out.println(actualNode);
        if (!actualNode.isLeaf()){
            travelTree(actualNode.getLeftChild());
            travelTree(actualNode.getRightChild());
        }


    }
}
