package com.roughsea.structures.grafs;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

    }

    static void testPack(){
        int[] c = {0, 3, 2, 10, 1};
        int[] w = {0, 5, 7, 4, 2};
        int[][] arr = packCost(10, w, c);

        for (int[] a : arr)
            System.out.println(Arrays.toString(a));
    }
    static int[][] packCost(int W, int[] w, int[] c){
        int[][] a = new int[W][w.length];

        for (int i = 1; i < w.length; i++) {
            for (int wi = 1; wi < W; wi++) {
                if (wi - w[i] > -1)
                    a[wi][i] = Math.max(a[wi - w[i]] [i-1] + c[i], a[wi] [i-1]);
                else
                    a[wi][i] =  a[wi][i - 1];
            }
        }
        return a;
    }

    static void test1(){
        int[] arr = {0, 4, 5, 7, 2, 1, 6, 6, 6, 8, 13};
        int max = Arrays.stream(arr).max().getAsInt();
        System.out.println(max);
        System.out.println(findBest(arr));
    }
    static int findBest(int[] arr){
        int[] d = new int[arr.length];
        d[1] = 1;
        for (int i = 2; i < arr.length; i++) {
            int maxI = 0;
            for (int j = 1; j <= i; j++) {
                if (arr[i] > arr[j] && arr[j] > arr[maxI])
                    maxI = j;
            }
            d[i] = 1 + d[maxI];
        }
        return Arrays.stream(d).max().getAsInt();
    }

    static int[][] findCost(String str1, String str2){
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        char[] a = new char[str1.length() + 1];
        System.arraycopy(s1, 0, a, 1, a.length - 1);
        char[] b = new char[str2.length() + 1];
        System.arraycopy(s2, 0, b, 1, b.length - 1);

        int[][] d = new int[a.length][b.length];
        for (int i = 0; i < a.length; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j < b.length; j++) {
            d[0][j] = j;
        }

        for (int i = 1; i < a.length; i++) {
            for (int j = 1; j < b.length; j++) {
                int min = Math.min(d[i-1][j]+1,d[i][j-1]+1);
                d[i][j] = Math.min(min, d[i-1][j-1] + (a[i]==b[j]?0:1));
            }
        }
        return d;
    }
}