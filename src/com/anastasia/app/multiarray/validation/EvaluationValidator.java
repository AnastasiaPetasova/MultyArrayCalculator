package com.anastasia.app.multiarray.validation;

import com.anastasia.app.multiarray.array.MultiArray;

public class EvaluationValidator {

    public static void assertVariableExists(MultiArray value, String name) throws EvaluationException {
        if (value == null) {
            throw new EvaluationException(
                    String.format(
                            "Переменной %s не присвоено значение",
                            name
                    )
            );
        }
    }
}
