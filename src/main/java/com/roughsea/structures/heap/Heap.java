package com.roughsea.structures.heap;

public class Heap {

    private Node heapArray[];
    private int maxSize;
    private int currentSize;

    public Heap(){
        maxSize = 5;
        currentSize = 0;
        heapArray = new Node[maxSize];
    }

    public boolean isEmpty(){
        return currentSize == 0;
    }

    public boolean insert(int key){
        if (currentSize == maxSize)
            resize();

        heapArray[currentSize] = new Node(key);

        trickleUp(currentSize++);
        return true;
    }

    private void resize(){
        maxSize *= 1.5;
        Node[] newArray = new Node[maxSize];
        System.arraycopy(heapArray, 0, newArray, 0, heapArray.length);
        heapArray = newArray;
    }

    private void trickleUp(int index) {
        int parent = (index - 1) / 2;
        Node bottom = heapArray[index];
        while (index > 0 && heapArray[parent].getKey() < bottom.getKey()){
            heapArray[index] = heapArray[parent];
            index = parent;
            parent = (parent - 1)/2;
        }
        heapArray[index] = bottom;
    }

    public Node remove(){
        if (isEmpty())
            return null;

        Node root = heapArray[0];
        if (currentSize > 1)
            heapArray[0] = heapArray[--currentSize];

        trickleDown(0);
        return root;
    }

    private void trickleDown(int index) {
        int largeChild;
        Node top = heapArray[index];
        while (index < currentSize / 2){
            int leftChild = 2 * index + 1;
            int rightChild = leftChild + 1;
            if (rightChild < currentSize &&
                    heapArray[leftChild].getKey() < heapArray[rightChild].getKey())
                largeChild = rightChild;
            else
                largeChild  = leftChild;
            if (top.getKey() >= heapArray[largeChild].getKey())
                break;

            heapArray[index] = heapArray[largeChild];
            index = largeChild;
        }
        heapArray[index] = top;
    }

    public boolean change(int index, int newValue){
        if (index < 0 || index >= currentSize)
            return false;
        int oldValue = heapArray[index].getKey();
        heapArray[index].setKey(newValue);

        if (oldValue < newValue)
            trickleUp(index);
        else
            trickleDown(index);

        return true;
    }

    public void displayHeap(){
        System.out.println("Heap: ");
        int row = (int) (Math.log(currentSize) / Math.log(2)) + 1;
        int count = 0;
        int column = 1;
        int k = 1;
        int u = (int) Math.pow(2, row);

        while (count < currentSize){
            for (int i = 0; i < u + 1; i++) {
                System.out.print(" ");
            }
            while (true){
                if (count < column && count < currentSize) {
                    System.out.print(heapArray[count++].getKey());
                    for (int i = 0; i < u * 2 - (u/k); i++) {
                        System.out.print(" ");
                    }
                } else {
                    k++;
                    row --;
                    u /= 2;
                    break;
                }
            }
            column = column * 2 + 1;
            System.out.println();
        }

        String dots = "....................";
        System.out.println("\n" + dots + dots);
    }

    public static void main(String[] args) {
        Heap heap = new Heap();
        for (int i = 1; i < 44; i++) {
            heap.insert(i);
        }

        heap.displayHeap();
//        heap.remove();
//        heap.displayHeap();
//        heap.change(5, 15);
//        heap.displayHeap();
//        heap.insert(25);
//        heap.displayHeap();
//        for (int i = 1; i < 44; i++) {
//            heap.insert(i);
//        }
//        heap.displayHeap();
    }
}
class Node{
    private int iData;

    public Node(int iData) {
        this.iData = iData;
    }

    public int getKey(){
        return iData;
    }

    public void setKey(int iData) {
        this.iData = iData;
    }
}