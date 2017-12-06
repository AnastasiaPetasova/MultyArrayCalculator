package com.anastasia.app.multiarray.validation;

import com.anastasia.app.multiarray.array.MultiArray;
import com.anastasia.app.multiarray.eval.MultiArrayReference;

public class EvaluationValidator {

    public static void assertValueExists(MultiArray value, String name) throws EvaluationException {
        if (value == null) {
            throw new EvaluationException(
                    String.format(
                            "Переменной %s не присвоено значение",
                            name
                    )
            );
        }
    }

    public static void assertAssignmentPossible(MultiArrayReference reference, String referenceString, MultiArray value) throws EvaluationException {
        if (!reference.isLeftPart()) {
            throw new EvaluationException(
                    String.format(
                            "Невозможно присвоить значение выражению, " +
                                    "не являющемуся именнованной переменной или ее частью: %s := %s",
                            referenceString, value.toString()
                    )
            );
        }
    }
}
