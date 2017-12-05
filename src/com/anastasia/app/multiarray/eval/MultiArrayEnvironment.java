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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int size = variables.size();
        stringBuilder.append(
                String.format(
                        "Количество переменных = %d",
                        size
                )
        );

        for (Map.Entry<String, MultiArray> variableEntry : variables.entrySet()) {
            String name = variableEntry.getKey();
            MultiArray value = variableEntry.getValue();

            stringBuilder.append('\n');
            stringBuilder.append(
                String.format(
                        "%s = %s",
                        name, value.toString()
                )
            );
        }

        return stringBuilder.toString();
    }
}
