package com.roughsea.structures.grafs;

import java.util.ArrayList;
import java.util.List;

import static com.roughsea.structures.grafs.Graph.*;

class Graph2 implements IGraph{
    private Node[] nodeList; // list of nodes
    private int nNode; // current number of nodes
    private Stack stack;
    private Queue queue;
    private Node[] sortedArray;
    private boolean isOriented = false;

    @Override
    public void setOriented(boolean oriented) {
        isOriented = oriented;
    }

    public Graph2(int count){
        nodeList = new Node[count];
        sortedArray = new Node[count];
        nNode = 0;
        stack = new Stack(count);
        queue = new Queue(count);
    }

    public void addNode(char label){
        nodeList[nNode++] = new Node(label);
    }

    public void addEdge(int start, int end){
        nodeList[start].addBound(nodeList[end]);
        if (!isOriented)
            nodeList[end].addBound(nodeList[start]);
    }

    public void displayNode(Node node){
        System.out.print(node.label + " ");
    }

    public void dfs(){
        nodeList[0].wasVisited = true;
        displayNode(nodeList[0]);
        stack.push(nodeList[0]);
        while(!stack.isEmpty()){
            Node node = getUnvisitedNode(stack.peek());
            if (node == null)
                stack.poop();
            else {
                node.wasVisited = true;
                displayNode(node);
                stack.push(node);
            }
        }
        for (int i = 0; i < nNode; i++) {
            nodeList[i].wasVisited = false;
        }
    }

    public void bfs(){
        nodeList[0].wasVisited = true;
        displayNode(nodeList[0]);
        queue.insert(nodeList[0]);
        Node v2;

        while (!queue.isEmpty()){
            Node v1 = queue.remove();

            while ((v2 = getUnvisitedNode(v1)) != null){
                v2.wasVisited = true;
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
        stack.push(nodeList[0]);
        while (!stack.isEmpty()){
            Node currentNode = stack.peek();

            Node v = getUnvisitedNode(currentNode);
            if (v == null)
                stack.poop();
            else {
                v.wasVisited = true;
                stack.push(v);

                displayNode(currentNode);
                displayNode(v);
            }
        }
        for (int i = 0; i < nNode; i++) {
            nodeList[i].wasVisited = false;
        }
    }

    public Node getUnvisitedNode(Node node){
        for (int i = 0; i < node.size(); i++)
            if (!node.getBound(i).wasVisited)
                return node.getBound(i);
        return null;
    }

    public void topo(){
        int origin_nNodes = nNode;
        while (nNode > 0){

            Node currentNode = noSuccessors();

            if (currentNode == null){
                System.err.println("ERROR: Graph has cycles");
                return;
            }

             System.out.print(currentNode + " ");
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

        for (int i = 0; i < nNode; i++) {
            nodeList[i].removeBound(currentNode);
        }

        if (currentInt != nNode - 1){
            nodeList[currentInt] = nodeList[nNode - 1];
        }

        nNode--;
    }

    public int findNode(Node node){
        for (int i = 0; i < nNode + 1; i++) {
            if (nodeList[i].equals(node))
                return i;
        }
        return -1;
    }


    // return non-Edge Node
    private Node noSuccessors() {
        boolean isEdge;

        for (int i = 0; i < nNode; i++) {
            isEdge = nodeList[i].bounds.size() != 0;

            if (!isEdge)
                return nodeList[i];
        }

        return null;
    }


    private class Node{
        private char label;
        private List<Node> bounds;
        private boolean wasVisited;

        public Node(char label){
            this.label = label;
            bounds = new ArrayList<>();
            wasVisited = false;
        }
        public void addBound(Node node){
            bounds.add(node);
        }
        public boolean removeBound(Node node){
            return bounds.remove(node);
        }
        public Node getBound(int i){
            return bounds.get(i);
        }
        public int size(){
            return bounds.size();
        }

        @Override
        public String toString() {
            return String.valueOf(label);
        }
    }

    class Stack{
        private Node[] stack;
        private int top;

        public Stack(int count){
            stack = new Node[count];
            top = -1;
        }
        public void push(Node node){
            stack[++top] = node;
        }
        public Node poop(){
            return stack[top--];
        }
        public Node peek() {
            return stack[top];
        }
        public boolean isEmpty(){
            return top == -1;
        }
    }

    class Queue{
        private int size;
        private Node[] queue;
        private int front;
        private int rear;
        public Queue(int size){
            this.size = size;
            queue = new Node[size];
            front = 0;
            rear = -1;
        }
        public void insert(Node node){
            if (rear == size - 1)
                rear = -1;
            queue[++rear] = node;
        }
        public Node remove(){
            Node temp = queue[front++];
            if (front == size)
                front = 0;
            return temp;
        }
        public boolean isEmpty(){
            return rear+1==front || front+size-1==rear;
        }

    }


    public static void main(String[] args) {
        Graph2 graph = new Graph2(20);
        test1Graph(graph);

        graph = new Graph2(10);
        test2Graph(graph);

        graph = new Graph2(10);
        testTopoGraph(graph);

    }
}