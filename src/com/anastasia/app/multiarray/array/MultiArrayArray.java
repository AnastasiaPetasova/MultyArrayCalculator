package com.anastasia.app.multiarray.array;

import com.anastasia.app.multiarray.validation.MultiArrayException;

public class MultiArrayArray extends MultiArrayImpl {

    private final MultiArray[] values;

    public MultiArrayArray(int length) {
        this.values = new MultiArray[length];
    }

    @Override
    protected MultiArray addWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = this.get(index).add(other.get(index));
            result.set(index, indexResult);
        }

        return result;
    }

    @Override
    protected MultiArray subtractWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = this.get(index).subtract(other.get(index));
            result.set(index, indexResult);
        }

        return result;
    }

    @Override
    protected MultiArray multiplyWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = this.get(index).multiply(other);
            result.set(index, indexResult);
        }

        return result;
    }

    @Override
    public int length() {
        return values.length;
    }

    @Override
    protected MultiArray getWithoutValidation(int index) {
        return values[index];
    }

    @Override
    public void setWithoutValidation(int index, MultiArray value) {
        values[index] = value;
    }
}
