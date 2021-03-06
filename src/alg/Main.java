package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        int operationCounter = 0;
        long runStart = System.currentTimeMillis();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        int numberOfLines = Integer.parseInt(bfr.readLine());
        AVLTree tree = new AVLTree();
        for (int i = 0; i < numberOfLines; i++) {
            String[] data = bfr.readLine().split(" ");
            int start = Integer.parseInt(data[1]);
            int end = Integer.parseInt(data[3]);
            int step = Integer.parseInt(data[2]);
            if (data[0].equals("I")){
                for (; start <= end; start += step){
                    tree.insert(start);
                    operationCounter++;
                }
            }else{
                //TODO delete
                for (; start<=end; start += step){
                    tree.delete(start);
                    operationCounter++;
                }
            }

        }
        tree.printTree();
        System.err.println(System.currentTimeMillis() - runStart);
        System.err.println("operations = " + operationCounter);
    }
}
