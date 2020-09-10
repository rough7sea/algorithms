package com.roughsea.interpreter.parser;

public class ParserException extends Exception{
    String errStr;
    public ParserException(String str){
        errStr = str;
    }

    @Override
    public String toString() {
        return errStr;
    }
}
