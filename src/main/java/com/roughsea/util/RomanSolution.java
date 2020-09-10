package com.roughsea.util;

public class RomanSolution {

    public void main(String[] args) {
        String count = "XXII";
        System.out.println(decodeRoman(count));
        count = "MCMXCIV";
        System.out.println(decodeRoman(count));
    }



    public int decodeRoman(String rom){
        int result = 0;
        int next;

        String str = new StringBuilder(rom).reverse().toString();
        int prev = Roman.returnInt(str.substring(0,1));

        for (String s : str.split("")){

            next = Roman.returnInt(s);

            if (prev <= next){
                result += next;
                System.out.print("+" + Roman.returnInt(s));
            } else {
                result -= next;
                System.out.print("-" + Roman.returnInt(s));
            }
            prev = next;
        }
        return result;
    }

    static class Roman{
        static int returnInt(String num){
            switch (num.toUpperCase()){
                case "I":
                    return 1;
                case "V":
                    return 5;
                case "X":
                    return 10;
                case "L":
                    return 50;
                case "C":
                    return 100;
                case "D":
                    return 500;
                case "M":
                    return 1000;
                default: return -1;
            }
        }
    }
}
