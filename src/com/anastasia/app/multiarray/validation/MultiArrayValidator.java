package com.anastasia.app.multiarray.validation;

import com.anastasia.app.multiarray.array.MultiArray;

public class MultiArrayValidator {

    public static void assertEqualLength(MultiArray first, MultiArray second) throws MultiArrayException {
        if (first.length() != second.length()) {
            throw new MultiArrayException(
                    String.format(
                            "Длины массивов не совпадают: %d и %d",
                            first.length(), second.length()
                    )
            );
        }
    }

    public static void assertIndexInRange(int length, int index) throws MultiArrayException {
        if (index < 0 || length <= index) {
            throw new MultiArrayException(
                    String.format(
                            "Индекс выходит за границы массива: [%d; %d], но индекс %d",
                            0, length - 1, index
                    )
            );
        }
    }
}
