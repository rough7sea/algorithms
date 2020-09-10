package com.roughsea.structures.grafs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

class Graph implements IGraph{
    private Node[] nodeList; // list of nodes
    private int[][] adjMat; // adjacency matrix
    private int nNode; // current number of nodes
    private Stack stack;
    private Queue queue;
    private PriorityQueue<Node> priorityQueue;
    private Node[] sortedArray;
    private boolean isOriented = false;


    public void setOriented(boolean oriented) {
        isOriented = oriented;
    }

    public Graph(int count){
        nodeList = new Node[count];
        sortedArray = new Node[count];
        adjMat = new int[count][count];
        nNode = 0;
        stack = new Stack(count);
        queue = new Queue(count);
        priorityQueue = new PriorityQueue<>(
                Comparator.comparingInt(o -> o.distance)
        );
    }


    public void addNode(char label){
        nodeList[nNode++] = new Node(label);
    }

    public void addEdge(int start, int end){
        adjMat[start][end] = 1;
        if (!isOriented)
            adjMat[end][start] = 1;
    }

    public void displayNode(int n){
        System.out.print(nodeList[n].label);
    }

    public void dfs(){
        nodeList[0].wasVisited = true;
        displayNode(0);
        stack.push(0);
        while(!stack.isEmpty()){
            int v = getUnvisitedNode(stack.peek());
            if (v == -1)
                stack.poop();
            else {
                nodeList[v].wasVisited = true;
                displayNode(v);
                stack.push(v);
            }
        }
        for (int i = 0; i < nNode; i++) {
            nodeList[i].wasVisited = false;
        }
    }

    public int getUnvisitedNode(int v){
        for (int i = 0; i < nNode; i++)
            if (adjMat[v][i] == 1 && !nodeList[i].wasVisited)
                return i;
        return -1;
    }

    public void bfs(){
        nodeList[0].wasVisited = true;
        displayNode(0);
        queue.insert(0);
        int v2;

        while (!queue.isEmpty()){
            int v1 = queue.remove();

            while ((v2 = getUnvisitedNode(v1)) != -1){
                nodeList[v2].wasVisited = true;
                displayNode(v2);
                queue.insert(v2);
            }
        }
        for (int i = 0; i < nNode; i++) {
            nodeList[i].wasVisited = false;
        }
    }

    public void mst(){
        nodeList[0].wasVisited = true;
        stack.push(0);
        while (!stack.isEmpty()){
            int currentNode = stack.peek();

            int v = getUnvisitedNode(currentNode);
            if (v == -1)
                stack.poop();
            else {
                nodeList[v].wasVisited = true;
                stack.push(v);

                displayNode(currentNode);
                displayNode(v);
            }
        }
        for (int i = 0; i < nNode; i++) {
            nodeList[i].wasVisited = false;
        }
    }

    public void topo(){
        int origin_nNodes = nNode;
         while (nNode > 0){

             Node currentNode = noSuccessors();

             if (currentNode == null){
                 System.err.println("ERROR: Graph has cycles");
                 return;
             }

//             System.out.println(currentNode);
             sortedArray[nNode -1] = currentNode;

             deleteNode(currentNode);
         }

        System.out.println("\nTopologically sorted order: ");
        for (int i = 0; i <origin_nNodes ; i++)
            System.out.print(sortedArray[i] + " ");
        System.out.println();
    }

    private void deleteNode(Node currentNode) {
        int currentInt = findNode(currentNode);
        if (currentInt != nNode - 1){

            if (nNode - 1 - currentInt >= 0)
                System.arraycopy(nodeList, currentInt + 1, nodeList, currentInt, nNode - 1 - currentInt);

            for (int row = currentInt; row < nNode - 1; row++) {
                moveRowUp(row, nNode);
            }

            for (int i = currentInt; i < nNode - 1; i++){
                moveColLeft(i, nNode -1);
            }
        }
        nNode--;
    }

    public int findNode(Node node){
        for (int i = 0; i < nNode; i++) {
            if (nodeList[i].equals(node))
                return i;
        }
        return -1;
    }

    private void moveColLeft(int col, int length) {
        for (int row = 0; row < length; row++) {
            adjMat[row][col] = adjMat[row][col + 1];
        }
    }

    private void moveRowUp(int row, int length) {
        if (length >= 0)
            System.arraycopy(adjMat[row + 1], 0, adjMat[row], 0, length);
    }

    private Node noSuccessors() {
        boolean isEdge;

        for (int row = 0; row < nNode; row++) {
            isEdge = false;
            for (int col = 0; col < nNode; col++) {
                if (adjMat[row][col] > 0){
                    isEdge = true;
                    break;
                }
            }
            if (!isEdge)
                return nodeList[row];
        }
        return null;
    }

    private class Node implements Comparable<Node>{
        public char label;
        public boolean wasVisited;
        public int distance;

        public Node(char label){
            this.label = label;
            wasVisited = false;
        }

        public void setDistance(int distance){
            this.distance = distance;
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }

        @Override
        public int compareTo(Node o) {
            return Character.compare(this.label, o.label);
        }
    }

    class Stack{
        private int[] stack;
        private int top;

        public Stack(int count){
            stack = new int[count];
            top = -1;
        }
        public void  push(int j){
            stack[++top] = j;
        }
        public int poop(){
            return stack[top--];
        }
        public int peek() {
            return stack[top];
        }
        public boolean isEmpty(){
            return top == -1;
        }
    }

    class Queue{
        private int size;
        private int[] queue;
        private int front;
        private int rear;
        public Queue(int size){
            this.size = size;
            queue = new int[size];
            front = 0;
            rear = -1;
        }
        public void insert(int i){
            if (rear == size - 1)
                rear = -1;
            queue[++rear] = i;
        }
        public int remove(){
            int temp = queue[front++];
            if (front == size)
                front = 0;
            return temp;
        }
        public boolean isEmpty(){
            return rear+1==front || front+size-1==rear;
        }

    }

    public static void main(String[] args) {
        Graph graph = new Graph(20);
        test1Graph(graph);

        graph = new Graph(10);
        test2Graph(graph);

        graph = new Graph(10);
        testTopoGraph(graph);
    }

    public static void test1Graph(IGraph graph){
        graph.addNode('a');
        graph.addNode('b');
        graph.addNode('c');
        graph.addNode('d');
        graph.addNode('e');
        graph.addNode('f');
        graph.addNode('g');
        graph.addNode('h');
        graph.addNode('i');
        graph.setOriented(false);

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(0,4);
        graph.addEdge(1,5);
        graph.addEdge(5,7);
        graph.addEdge(3,6);
        graph.addEdge(6,8);

        System.out.println("\ndfs");
        graph.dfs();
        System.out.println("\nbfs");
        graph.bfs();
        System.out.println("\nmst");
        graph.mst();
    }

    public static void test2Graph(IGraph graph){
        graph.addNode('a');
        graph.addNode('b');
        graph.addNode('c');
        graph.addNode('d');
        graph.addNode('e');

        graph.setOriented(false);

        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(0,4);
        graph.addEdge(1,2);
        graph.addEdge(1,3);
        graph.addEdge(1,4);
        graph.addEdge(2,3);
        graph.addEdge(2,4);
        graph.addEdge(3,4);

        System.out.println("\ndfs");
        graph.dfs();
        System.out.println("\nbfs");
        graph.bfs();
        System.out.println("\nmst");
        graph.mst();
    }

    public static void testTopoGraph(IGraph graph){
        graph.addNode('a');
        graph.addNode('b');
        graph.addNode('c');
        graph.addNode('d');
        graph.addNode('e');
        graph.addNode('f');
        graph.addNode('g');
        graph.addNode('h');

        graph.setOriented(true);

        graph.addEdge(0,3);
        graph.addEdge(0,4);
        graph.addEdge(1,4);
        graph.addEdge(2,5);
        graph.addEdge(2,4);
        graph.addEdge(3,6);
        graph.addEdge(4,6);
        graph.addEdge(5,7);
        graph.addEdge(6,7);
        System.out.println();
        graph.topo();
    }
}