package com.roughsea.sorts;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class QuickSort {

    static Random rand = new Random(44);
    public static void main(String[] args) throws IOException {

        int[] a = new int[100];
//        System.out.println(partition(a, 0, a.length - 1));
        for (int i = 0; i < 100; i++) {
            a[i] = rand.nextInt(100);
        }
        quickSort(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));

    }


    static void quickSort(int[] a, int l, int r){
        if (l >= r)
            return;
        int m = partition(a, l, r);
        quickSort(a, l, m - 1);
        quickSort(a, m + 1, r);
    }

    static int partition(int[] a, int l, int r){

//        to optimize median element
        swap(a, r, l + rand.nextInt(r - l));
        int i = l - 1;
        for (int j = l; j < r; j++) {
            if (a[j] <= a[r]){
                swap(a, j, i + 1);
                i++;
            }
        }
        i++;
        swap(a, i, r);
        return i;
    }
    static void swap(int[] a, int j, int i){
        int b = a[j];
        a[j] = a[i];
        a[i] = b;
    }
}
