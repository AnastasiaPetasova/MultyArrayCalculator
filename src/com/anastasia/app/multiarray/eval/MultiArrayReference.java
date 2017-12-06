package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.array.MultiArray;
import com.anastasia.app.multiarray.validation.EvaluationException;
import com.anastasia.app.multiarray.validation.EvaluationValidator;
import com.anastasia.app.multiarray.validation.MultiArrayValidator;

public class MultiArrayReference {

    MultiArrayEnvironment environment;
    String name;

    public boolean isLeftPart() {
        return name != null;
    }

    MultiArray value;
    int index;

    MultiArrayReference(MultiArrayEnvironment environment, String name, MultiArray value) {
        this(environment, name, value, -1);
    }

    MultiArrayReference(MultiArrayEnvironment environment, String name, MultiArray value, int index) {
        this.environment = environment;
        this.name = name;
        this.value = value;
        this.index = index;
    }

    public MultiArray get() throws EvaluationException {
        if (index == -1) {
            return value;
        } else {
            return value.get(index);
        }
    }

    public void set(MultiArrayEnvironment environment, MultiArray innerValue) throws EvaluationException {
        if (index == -1) {
            environment.set(name, innerValue);
        } else {
            value.set(index, innerValue);
        }
    }
}