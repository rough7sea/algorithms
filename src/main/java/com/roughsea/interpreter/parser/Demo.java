package com.roughsea.interpreter.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Demo {
    public static void main(String[] args) throws IOException {
        String expr;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Parser p = new Parser();
        System.out.println("write down empty expression to exit.");
        System.out.println("Enter expressions");
        while (true){
            expr = br.readLine();
            if (expr.equals(" "))
                break;
            try {
                System.out.println("Result: " + p.evaluate(expr) + "\n");
            } catch (ParserException e) {
                System.out.println(e);
            }
        }
    }
}