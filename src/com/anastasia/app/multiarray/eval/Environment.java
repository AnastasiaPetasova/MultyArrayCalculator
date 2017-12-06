package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.validation.EvaluationException;

public interface Environment {

    void clear();
    Object process(String line) throws EvaluationException ;
}
