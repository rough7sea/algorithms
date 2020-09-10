package com.roughsea.sorts;

import java.util.Arrays;
import java.util.Random;

public class MergeSort {

    static Random rand = new Random(33);
    public static void main(String[] args) {

        int[] a = new int[100];
        for (int i = 0; i < 100; i++) {
            a[i] = rand.nextInt(100);
        }

        System.out.println(Arrays.toString(sortArray(a)));

    }

    public static int[] sortArray(int[] array){
        if(array == null || array.length < 2)
            return array;

        int[] left = new int[array.length /2];
        System.arraycopy(array,0,left,0,array.length /2);

        int[] right = new int[array.length - array.length /2];
        System.arraycopy(array,array.length /2, right,0,array.length - array.length /2);

        left = sortArray(left);
        right = sortArray(right);

        return mergeSort(left, right);
    }

    public static int[] mergeSort(int[] left, int[] right){
        int l = 0, r = 0;

        int[] result = new int[left.length + right.length];

        int i = 0;
        while(l + r < left.length + right.length){
            if (l == left.length){
                while (r < right.length)
                    result[i++] = right[r++];
                return result;
            }
            if (r == right.length){
                while (l < left.length)
                    result[i++] = left[l++];
                return result;
            }

            if (left[l] < right[r])
                result[i++] = left[l++];
            else
                result[i++] = right[r++];
        }
        return result;
    }
}