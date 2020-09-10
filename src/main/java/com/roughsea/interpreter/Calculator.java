package com.roughsea.interpreter;

import com.roughsea.interpreter.parser.Parser;
import com.roughsea.interpreter.parser.ParserException;

import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame{

    private Parser p = new Parser();

    private TextField expressionField = new TextField(25);
    private TextField resultField = new TextField(25);

    public Calculator(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setTitle("Calculator");
        setLocationRelativeTo(null);
        setResizable(false);

        add(new Label("Expression", Label.CENTER));
        add(expressionField);
        add(new Label("Result", Label.CENTER));
        add(resultField);

        expressionField.addActionListener(e -> {
            try {
                resultField.setText(p.evaluate(expressionField.getText()) + "");
            } catch (ParserException ex) {
                resultField.setText(ex.toString());
            }
        });

        resultField.setEditable(false);

        setVisible(true);
    }
}
