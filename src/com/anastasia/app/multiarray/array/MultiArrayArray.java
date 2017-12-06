package com.anastasia.app.multiarray.array;

import com.anastasia.app.multiarray.validation.MultiArrayException;

public class MultiArrayArray extends MultiArrayImpl {

    private MultiArray[] values;

    public MultiArrayArray(int length) {
        this.values = new MultiArray[length];
    }

    @Override
    protected MultiArray addWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = getWithoutValidation(index).add(other.get(index));
            result.set(index, indexResult);
        }

        return result;
    }

    @Override
    protected MultiArray subtractWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = getWithoutValidation(index).subtract(other.get(index));
            result.set(index, indexResult);
        }

        return result;
    }

    @Override
    protected MultiArray multiplyWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result = new MultiArrayArray(length());
        for (int index = 0, length = length(); index < length; ++index) {
            MultiArray indexResult = getWithoutValidation(index).multiply(other);
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('[');

        for (int index = 0, length = length(); index < length; ++index) {
            if (index > 0) stringBuilder.append(' ');

            String elementString = getWithoutValidation(index).toString();
            stringBuilder.append(elementString);
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }

    @Override
    public MultiArrayArray clone() throws CloneNotSupportedException {
        MultiArrayArray cloned = (MultiArrayArray) super.clone();

        cloned.values = new MultiArray[length()];
        for (int i = 0; i < length(); ++i) {
            MultiArray itemCloned = getWithoutValidation(i).clone();
            cloned.setWithoutValidation(i, itemCloned);
        }

        return cloned;
    }
}
