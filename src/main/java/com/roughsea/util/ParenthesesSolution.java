package com.roughsea.util;

import java.util.*;

public class ParenthesesSolution {
    public static void main(String[] args) {
        String s = "{}{()()[}]";
        System.out.println(isValid(s));
    }

    public static boolean isValid(String s) {

        char[] split = s.toCharArray();
        Deque<Character> closeStr = new LinkedList<>();
        int open = 0;
        for (char c : split){
            if(c == '['){
                closeStr.add(']');
                open++;
            }
            if(c == '{'){
                closeStr.add('}');
                open++;
            }
            if(c == '('){
                closeStr.add(')');
                open++;
            }

            if(c == ']' || c == '}' || c == ')'){
                if (closeStr.size() == 0)
                    return false;
                if(!closeStr.removeLast().equals(c))
                    return false;
                open--;
            }
        }
        return open <= 0;
    }
}