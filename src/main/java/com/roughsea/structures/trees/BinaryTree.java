package com.roughsea.structures.trees;

import java.util.LinkedList;
import java.util.Queue;

//check logic! It can be broken
public class BinaryTree<T> {

    private Node root;
    class Node{
        T value;
        Node left;
        Node right;
        Node(T value){
            this.value = value;
            right = null;
            left = null;
        }
    }

    private Node addRecursive(Node current, T value){
        if(current == null)
            return new Node(value);

        if(((Comparable<T>)value).compareTo(current.value) > 0)
            current.left = addRecursive(current.left,value);
        else if(((Comparable<T>)value).compareTo(current.value) < 0)
            current.right = addRecursive(current.right,value);
        else return current;
        return current;
    }

    public void add(T value){
        root = addRecursive(root,value);
    }
    private BinaryTree<Integer> createBinaryTree(){
        BinaryTree<Integer> bt = new BinaryTree<>();
        bt.add(4);
        bt.add(7);
        bt.add(1);
        bt.add(19);
        bt.add(4);
        bt.add(2);
        bt.add(22);
        bt.add(6);
        bt.add(5);
        return bt;
    }

    private boolean containsNodeRecursive(Node current, T value){
        if(current == null) return false;
        if (current.value.equals(value)) return true;
        return ((Comparable<T>)value).compareTo(current.value) < 0
                ? containsNodeRecursive(current.left,value)
                : containsNodeRecursive(current.right,value);
    }

    public boolean containsNode(T value){
//        return containsNodeRecursive(root,value);
        return containsNodeNoNRecursive(value);
    }

    private boolean containsNodeNoNRecursive(T key){
        Node current = root;

        while (!key.equals(current.value)){
            if (((Comparable<T>)key).compareTo(current.value) > 0)
                current = current.left;
            else
                current = current.right;

            if (current == null)
                return false;
        }
        return true;
    }


    private T findSmallestValue(Node current){
        return current.left == null ? current.value : findSmallestValue(current.left);
    }

//    private Node deleteRecursive(Node current,T value){
//        if(current == null) return null;
//        if(value == current.value){
//            if(current.left == null && current.right == null){
//                return  null;
//            }
//            if (current.left == null)
//                return current.right;
//            if (current.right == null)
//                return current.left;
//
//            T smallestValue = findSmallestValue(current.right);
//            current.value = smallestValue;
//            current.right = deleteRecursive(current.right, smallestValue);
//            return current;
//        }
//        if(((Comparable<T>)value).compareTo(current.value) < 0){
//            current.left = deleteRecursive(current.left,value);
//            return current;
//        }
//        current.right = deleteRecursive(current.right,value);
//        return current;
//    }
//
//    public void delete(T value){
//        root = deleteRecursive(root,value);
//    }

    public boolean delete(T key){

        Node current = root;
        Node parent = root;
        boolean isLeftChild = true;

        while (!current.value.equals(key)){
            parent = current;
            if (((Comparable<T>)key).compareTo(current.value) > 0){
                isLeftChild = true;
                current = current.left;
            }else {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null)
                return  false;
        }

        if (current.left == null && current.right == null){
            if (current == root)
                root = null;
            else if (isLeftChild)
                parent.left = null;
            else
                parent.right = null;
        }
        else if (current.right == null)
            if (current == root)
                root = current.left;
            else if (isLeftChild)
                parent.left = current.left;
            else
                parent.right = current.left;
        else if (current.left == null)
            if (current == root)
                root = current.right;
            else if (isLeftChild)
                parent.left = current.right;
            else
                parent.right = current.right;
        else {
            Node successor = getSuccessor(current);

                if (current == root)
                    root = successor;
                else if (isLeftChild)
                    parent.left = successor;
                else
                    parent.right = successor;
        }
        return true;
    }

    private Node getSuccessor(Node delNode) {

        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.right;

        while(current != null){
            successorParent = successor;
            successor = current;
            current = successor.left;
        }

        if (successor != delNode.right) {
            successorParent.left = successor.right;
            successor.right = delNode.right;
        }

        return successor;
    }

    private void traverseInOrder(Node current){
        if(current!= null){
            traverseInOrder(current.left);
            System.out.print(current.value + " ");
            traverseInOrder(current.right);
        }
    }
    private void traversePreOrder(Node current){
        if(current!= null){
            System.out.print(current.value + " ");
            traverseInOrder(current.left);
            traverseInOrder(current.right);
        }
    }
    private void traversePostOrder(Node current){
        if(current!= null){
            traverseInOrder(current.left);
            traverseInOrder(current.right);
            System.out.print(current.value + " ");
        }
    }
    public void traverseLevelOrder(){
        if(root == null) return;
        //FIFO
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);
        while(!nodes.isEmpty()){
            Node node = nodes.remove();
            System.out.print(" " + node.value);
            if(node.left != null)
                nodes.add(node.left);
            if(node.right != null)
                nodes.add(node.right);
        }
    }


    private void toPrint(){
        System.out.print("In :");
        traverseInOrder(root);
        System.out.print("\nPre :");
        traversePreOrder(root);
        System.out.print("\nPost :");
        traversePostOrder(root);
        System.out.print("\nLevel Order :");
        traverseLevelOrder();
        System.out.println();
    }

    public static void main(String[] args) {
        BinaryTree<Integer> bt = new BinaryTree<>();
        bt = bt.createBinaryTree();
        bt.toPrint();
        System.out.println(bt.delete(7));
        for (int i = 0; i < 6; i++) {
            bt.add(i);
        }
        bt.toPrint();
        System.out.println(bt.containsNode(-1));
    }
}