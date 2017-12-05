package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.array.MultiArray;
import com.anastasia.app.multiarray.validation.EvaluationException;
import com.anastasia.app.multiarray.validation.EvaluationValidator;

import java.util.HashMap;
import java.util.Map;

public class MultiArrayEnvironment {

    public static MultiArrayEnvironment create() {
        return new MultiArrayEnvironment();
    }

    Map<String, MultiArray> variables;

    private MultiArrayEnvironment() {
        this.variables = new HashMap<>();
    }

    public void clear() {
        this.variables.clear();
    }

    public MultiArray get(String name) throws EvaluationException {
        MultiArray value = variables.get(name);
        EvaluationValidator.assertVariableExists(value, name);

        return value;
    }

    public void set(String name, MultiArray value) {
        variables.put(name, value);
    }
}
