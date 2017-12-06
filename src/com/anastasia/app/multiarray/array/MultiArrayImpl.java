package com.anastasia.app.multiarray.array;

import com.anastasia.app.multiarray.validation.MultiArrayException;
import com.anastasia.app.multiarray.validation.MultiArrayValidator;

public abstract class MultiArrayImpl implements MultiArray {

    protected abstract MultiArray addWithoutValidation(MultiArray other) throws MultiArrayException;

    @Override
    public MultiArray add(MultiArray other) throws MultiArrayException {
        MultiArrayValidator.assertEqualLength(this, other);
        return addWithoutValidation(other);
    }

    protected abstract MultiArray subtractWithoutValidation(MultiArray other) throws MultiArrayException;

    @Override
    public MultiArray subtract(MultiArray other) throws MultiArrayException {
        MultiArrayValidator.assertEqualLength(this, other);
        return subtractWithoutValidation(other);
    }

    protected abstract MultiArray multiplyWithoutValidation(MultiArray other) throws MultiArrayException;

    @Override
    public MultiArray multiply(MultiArray other) throws MultiArrayException {
        return multiplyWithoutValidation(other);
    }

    @Override
    public MultiArray get(int index) throws MultiArrayException {
        MultiArrayValidator.assertIndexInRange(length(), index);
        return getWithoutValidation(index);
    }

    @Override
    public void set(int index, MultiArray value) throws MultiArrayException {
        MultiArrayValidator.assertIndexInRange(length(), index);
        setWithoutValidation(index, value);
    }

    protected MultiArray getWithoutValidation(int index) {
        throw new UnsupportedOperationException();
    }

    protected void setWithoutValidation(int index, MultiArray value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultiArray clone() throws CloneNotSupportedException {
        return (MultiArray) super.clone();
    }
}
