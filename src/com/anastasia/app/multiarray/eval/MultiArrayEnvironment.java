package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.array.MultiArray;
import com.anastasia.app.multiarray.validation.EvaluationException;
import com.anastasia.app.multiarray.validation.EvaluationValidator;

import java.util.HashMap;
import java.util.Map;

public class MultiArrayEnvironment implements Environment {

    public static MultiArrayEnvironment create() {
        return new MultiArrayEnvironment();
    }

    private final MultiArrayParser parser;
    private final Map<String, MultiArray> variables;

    private MultiArrayEnvironment() {
        this.variables = new HashMap<>();
        this.parser = new MultiArrayParser(this);
    }

    @Override
    public void clear() {
        this.variables.clear();
    }

    protected MultiArray getVariable(String name) throws EvaluationException {
        if (name == null) return null;

        MultiArray value = variables.get(name.toUpperCase());

        return value;
    }

    protected void set(String name, MultiArray value) {
        if (name == null) return;

        variables.put(name.toUpperCase(), value);
    }

    @Override
    public Object process(String line) throws EvaluationException {
        String[] assignmentParts = line.split(MultiArrayParser.ASSIGN);
        MultiArrayReference[] assignmentValues = new MultiArrayReference[assignmentParts.length];

        for (int i = 0; i < assignmentParts.length; ++i) {
            assignmentValues[i] = parser.parseAssignmentPart(assignmentParts[i]);
        }

        for (int index = assignmentParts.length - 1; index > 0; --index) {
            MultiArray rightValue = assignmentValues[index].get();
            MultiArrayReference leftReference = assignmentValues[index - 1];

            EvaluationValidator.assertAssignmentPossible(leftReference, assignmentParts[index - 1], rightValue);

            if (assignmentValues[index].name != null && rightValue != null) {
                try {
                    rightValue = rightValue.clone();
                } catch (CloneNotSupportedException e) {
                    throw new EvaluationException(
                            String.format(
                                    "Значение не может быть скопировано: %s",
                                    rightValue.toString()
                            )
                    );
                }
            }

            leftReference.set(this, rightValue);
        }

        if (assignmentParts.length == 1) {
            MultiArray value = assignmentValues[0].get();
            return ( value == null ? "Значение не найдено" : value.toString());
        } else {
            return null;
        }
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

            if (value == null) continue;

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
