package com.anastasia.app.multiarray.array;

import com.anastasia.app.multiarray.validation.MultiArrayException;

import java.math.BigInteger;

public class MultiArrayNumber extends MultiArrayImpl {

    BigInteger value;

    public MultiArrayNumber(BigInteger value) {
        this.value = value;
    }

    public BigInteger getValue() {
        return value;
    }

    @Override
    protected MultiArray addWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArrayNumber otherNumber = (MultiArrayNumber) other;

        BigInteger resultNumber = this.value.add(otherNumber.value);
        return new MultiArrayNumber(resultNumber);
    }

    @Override
    protected MultiArray subtractWithoutValidation(MultiArray other) {
        MultiArrayNumber otherNumber = (MultiArrayNumber) other;

        BigInteger resultNumber = this.value.subtract(otherNumber.value);
        return new MultiArrayNumber(resultNumber);
    }

    @Override
    protected MultiArray multiplyWithoutValidation(MultiArray other) throws MultiArrayException {
        MultiArray result;

        if (other instanceof MultiArrayNumber) {
            MultiArrayNumber otherNumber = (MultiArrayNumber) other;

            BigInteger resultNumber = this.value.multiply(otherNumber.value);
            result = new MultiArrayNumber(resultNumber);
        } else if (other instanceof MultiArrayArray){
            result = new MultiArrayArray(other.length());
            for (int index = 0, length = result.length(); index < length; ++index) {
                MultiArray indexResult = this.multiply(other.get(index));
                result.set(index, indexResult);
            }
        } else {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
