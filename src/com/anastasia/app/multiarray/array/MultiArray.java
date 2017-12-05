package com.anastasia.app.multiarray.array;

public interface MultiArray {

    MultiArray add(MultiArray other) throws MultiArrayException;
    MultiArray subtract(MultiArray other) throws MultiArrayException;
    MultiArray multiply(MultiArray other) throws MultiArrayException;

    int length();
    MultiArray get(int index) throws MultiArrayException;
    void set(int index, MultiArray value) throws MultiArrayException;
}
