package com.roughsea.interpreter.parser;


import java.util.HashMap;
import java.util.Map;

public class Parser {

//    private double[] vars = new double[26];
    private Map<String, Double> varsMap = new HashMap<>();

    final int NONE = 0;
    final int DELIMITER = 1;
    final int VARIABLE = 2;
    final int NUMBER = 3;
    final int STRING = 4;
    final int STRING_DELIMITER = 5;

    //     errors
    final int SYNTAX = 0;
    final int UNBALPARENS = 1;
    final int NO_EXP = 2;
    final int DIV_BY_ZERO = 3;
    final int VAR_IS_ABS = 4;

    final String EOE = "\0";
    private String exp;
    private int expIdx;
    private String token;
    private int tokType;



    public double evaluate(String expstr) throws ParserException {
        double result;
        exp = expstr;
        expIdx = 0;

        getToken();
        if(token.equals(EOE))
            handleErr(NO_EXP); // no expression present

// Parse and evaluate the expression.
        result = evalExp1();
        if(!token.equals(EOE)) // last token must be EOE
            handleErr(SYNTAX);
        return result;
    }
// assigment
    private double evalExp1() throws ParserException{
        double result;
//        int varIdx;
        int tTokType;
        String varName;
        String tempToken;

        if (tokType == VARIABLE){
//            save previous lexeme
            tempToken = token;
            tTokType = tokType;

//            varIdx = Character.toUpperCase(token.charAt(0)) - 'A';
            varName = token;

            getToken();
            if (!token.equals("=")){
                putBack(); // put back previous lexeme
                token = tempToken;
                tokType = tTokType;
            }
            else {
                getToken();
                result = evalExp2();
//                vars[varIdx] = result;
                varsMap.put(varName, result);
                return result;
            }
        }

        return evalExp2();
    }


    //    + or -
    private double evalExp2() throws ParserException {
        char op;
        double result;
        double partialResult;

        result = evalExp3();

        while((op = token.charAt(0)) == '+' || op == '-') {
            getToken();
            partialResult = evalExp3();
            switch(op) {
                case '-':
                    result = result - partialResult;
                    break;
                case '+':
                    result = result + partialResult;
                    break;
            }
        }
        return result;
    }


    //    * or /
    private double evalExp3() throws ParserException {
        char op;
        double result;
        double partialResult;

        result = evalExp4();

        while((op = token.charAt(0)) == '*' || op == '/' || op == '%') {
            getToken();
            partialResult = evalExp4();
            switch(op) {
                case '*':
                    result = result * partialResult;
                    break;
                case '/':
                    if(partialResult == 0.0)
                        handleErr(DIV_BY_ZERO);
                    result = result / partialResult;
                    break;
                case '%':
                    if(partialResult == 0.0)
                        handleErr(DIV_BY_ZERO);
                    result = result % partialResult;
                    break;
            }
        }
        return result;
    }

    // Process an exponent.
    private double evalExp4() throws ParserException {
        double result;
        double partialResult;
        double ex;
        int t;

        result = evalExp5();

        if(token.equals("^")) {
            getToken();
            partialResult = evalExp4();
            ex = result;
            if(partialResult == 0.0) {
                result = 1.0;
            } else
                for(t=(int)partialResult-1; t > 0; t--)
                    result = result * ex;
        }
        return result;
    }

    // Evaluate a unary + or -.
    private double evalExp5() throws ParserException {
        double result;
        String op = "";

        if((tokType == DELIMITER) &&
                token.equals("+") || token.equals("-")) {
            op = token;
            getToken();
        }
        result = evalExp6();

        if(op.equals("-")) result = -result;
        return result;
    }

    //    parentheses
    private double evalExp6() throws ParserException {
        double result;
        if(token.equals("(")) {
            getToken();

            result = evalExp2();

            if(!token.equals(")"))
                handleErr(UNBALPARENS);
            getToken();
        }
        else result = atom();
        return result;
    }

    // get numbers value
    private double atom() throws ParserException {
        double result = 0.0;
        switch(tokType) {
            case NUMBER:
                try {
                    result = Double.parseDouble(token);
                } catch (NumberFormatException exc) {
                    handleErr(SYNTAX);
                }
                getToken();
                break;
            case VARIABLE:
                result = findVar(token);
                getToken();
                break;
//            case STRING:
//                result = token;
//                getToken();
//                break;
            default:
                handleErr(SYNTAX);
                break;
        }
        return result;
    }

    //    get variable value
    private double findVar(String vName) throws ParserException{
        if (!Character.isLetter(vName.charAt(0))){
            handleErr(SYNTAX);
            return 0.0;
        }
        if (varsMap.get(vName) == null)
            handleErr(VAR_IS_ABS);
        return varsMap.get(vName);
//        return vars[Character.toUpperCase(vName.charAt(0)) - 'A'];
    }

    private void putBack(){
        if (token.equals(EOE)) return;
        for (int i = 0; i < token.length() ; i++)
            expIdx--;
    }

    //handle an error
    private void handleErr(int error) throws ParserException{
        String[] err = {
                "Syntax Error",
                "Unbalanced Parentheses",
                "No Expression Present",
                "Division By Zero",
                "Variable doesn't exist"
        };
        throw new ParserException(err[error] + " at index " + (expIdx-1));
    }

    // Obtain the next token.
    private void getToken() throws ParserException {
        tokType = NONE;
        token = "";
// Check for end of expression.
        if(expIdx == exp.length()) {
            token = EOE;
            return;
        }
// Skip over white space.
        while(expIdx < exp.length() &&
                Character.isWhitespace(exp.charAt(expIdx))) ++expIdx;
// Trailing whitespace ends expression.
        if(expIdx == exp.length()) {
            token = EOE;
            return;
        }
//        if(isStringDelim(exp.charAt(expIdx))){
//            while(!isStringDelim(exp.charAt(expIdx))) {
//                token += exp.charAt(expIdx);
//                expIdx++;
//                if(expIdx > exp.length())
//                    handleErr(SYNTAX);
//            }
//            tokType = STRING;
//        }
        if(isDelim(exp.charAt(expIdx))) { // is operator
            token += exp.charAt(expIdx);
            expIdx++;
            tokType = DELIMITER;
        }
        else if(Character.isLetter(exp.charAt(expIdx))) { // is variable
            while(!isDelim(exp.charAt(expIdx))) {
                token += exp.charAt(expIdx);
                expIdx++;
                if(expIdx >= exp.length()) break;
            }
            tokType = VARIABLE;
        }
        else if(Character.isDigit(exp.charAt(expIdx))) { // is number
            while(!isDelim(exp.charAt(expIdx))) {
                token += exp.charAt(expIdx);
                expIdx++;
                if(expIdx >= exp.length()) break;
            }
            tokType = NUMBER;
        }
        else { // unknown character terminates expression
            token = EOE;
        }
    }

    private boolean isStringDelim(char c) {
        return ("\"'".indexOf(c) != -1);
    }

    private boolean isDelim(char c) {
        return (" +-/*%^=()".indexOf(c) != -1);
    }
}