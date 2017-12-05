package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.validation.EvaluationException;

public interface Environment {

    String ASSIGN = ":=";
    String ADD = "+", SUBTRACT = "-", MULTIPLY = "*";
    String OPEN_BRACKET = "[", CLOSE_BRACKET = "]";

    void clear();
    Object process(String line) throws EvaluationException ;
}
