package com.roughsea.structures.trees;

import java.util.Random;

class DataItem{
    public long dData;

    public DataItem(long dData) { this.dData = dData; }

    @Override
    public String toString() {
        return "_" + dData;
    }
}
class Node{
    private static final int ORDER = 4;
    private int numItems;
    private Node parent;
    private Node[] childArray = new Node[ORDER];
    private DataItem[] items = new DataItem[ORDER-1];

    public void connect(int childNum, Node child){
        childArray[childNum] = child;
        if (child != null)
            child.parent = this;
    }
    public Node disconnect(int childNum){
        Node temp = childArray[childNum];
        childArray[childNum] = null;
        return temp;
    }
    public Node getChild(int childNum){
        return childArray[childNum];
    }
    public Node getParent(){
        return parent;
    }
    public boolean isLeaf(){
        return childArray[0] == null;
    }
    public int getNumItems() {
        return numItems;
    }
    public DataItem getItems(int index) {
        return items[index];
    }
    public boolean isFull(){
        return numItems == ORDER-1;
    }
    public int findKey(long key){
        for (int i = 0; i < ORDER-1; i++) {
            if (items[i] == null)
                break;
            if (items[i].dData == key)
                return i;
        }
        return -1;
    }

    public int insertItem(DataItem newItem){
        numItems++;
        long newKey = newItem.dData;

        for (int i = ORDER-2; i >=0 ; i--) {
            if (items[i] == null)
                continue;
            long itsKey = items[i].dData;
            if (newKey < itsKey)
                items[i+1] = items[i];
            else {
                items[i+1] = newItem;
                return i+1;
            }

        }
        items[0] = newItem;
        return 0;
    }
    public DataItem removeItem(){
        DataItem temp = items[numItems-1];
        items[numItems-1] = null;
        numItems--;
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numItems ; i++)
            sb.append(items[i].toString());
        return String.valueOf(sb);
    }
}

public class Tree234 {

    private Node root = new Node();

    public int find(long key){

        Node curNode = root;
        int childNumber;

        while (true){
            if ((childNumber = curNode.findKey(key)) != -1)
                return childNumber;
            else if (curNode.isLeaf())
                return -1;
            else curNode = getNextChild(curNode, key);
        }
    }

    public void insert(long dValue){

        Node curNode = root;
        DataItem tempItem = new DataItem(dValue);

        while (true){
            if (curNode.isFull()){
                split(curNode);
                curNode = curNode.getParent();

                curNode = getNextChild(curNode, dValue);
            }
            else if (curNode.isLeaf())
                break;
            else
                curNode = getNextChild(curNode, dValue);
        }
        curNode.insertItem(tempItem);
    }

    private void split(Node thisNode) {
        DataItem itemB, itemC;
        Node parent, child2, child3;
        int itemIndex;

        itemC = thisNode.removeItem();
        itemB = thisNode.removeItem();
        child2 = thisNode.disconnect(2);
        child3 = thisNode.disconnect(3);
        Node newRight = new Node();

        if (thisNode == root){
            root = new Node();
            parent = root;
            root.connect(0, thisNode);
        }
        else
            parent = thisNode.getParent();

        itemIndex = parent.insertItem(itemB);
        int n = parent.getNumItems();

        for (int i = n-1; i >itemIndex; i--) {
            Node temp = parent.disconnect(i);
            parent.connect(i+1, temp);
        }

        parent.connect(itemIndex+1, newRight);

        newRight.insertItem(itemC);
        newRight.connect(0,child2);
        newRight.connect(1,child3);
    }

    private Node getNextChild(Node theNode, long theValue) {
        int i;

        int numItems = theNode.getNumItems();
        for (i = 0; i < numItems; i++)
            if (theValue < theNode.getItems(i).dData)
                return theNode.getChild(i);
        return theNode.getChild(i);
    }

    public void displayTree(){
        recDisplayTree(root,0,0);
    }

    private void recDisplayTree(Node thisNode, int level, int child) {
        System.out.println("level=" + level + " child=" + child + " " + thisNode.toString());

        int numItems = thisNode.getNumItems();
        for (int i = 0; i < numItems+1; i++) {
            Node nextNode = thisNode.getChild(i);
            if (nextNode != null)
                recDisplayTree(nextNode, level+1,i);
            else
                return;
        }
    }

    public boolean contains(long key){
        return recSearch(root, key) != null;
    }

    private Node recSearch(Node node, long key){
        while(node != null){
            int n = node.getNumItems();
            for (int i = 0; i < n; i++) {
                if (key == node.getItems(i).dData)
                    return node;
            }

            Node old = node;

            for (int i = n - 1; i >= 0; i--) {
                if (key > node.getItems(i).dData) {
                    node = node.getChild(i + 1);
                    break;
                }
            }
            if (old == node)
                node = node.getChild(0);

        }
        return null;
    }

    public static void main(String[] args) {
        Tree234 tree234 = new Tree234();
        Random rand = new Random(33);
        for (int i = 0; i < 30; i++) {
            tree234.insert(rand.nextInt(50));
        }
        tree234.displayTree();
        System.out.println(tree234.contains(28));
        System.out.println(tree234.contains(4));
        System.out.println(tree234.contains(47));
        System.out.println(tree234.contains(55));
    }
}