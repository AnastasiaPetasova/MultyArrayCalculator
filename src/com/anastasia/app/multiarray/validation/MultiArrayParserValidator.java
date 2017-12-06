package com.anastasia.app.multiarray.validation;

import com.anastasia.app.multiarray.eval.MultiArrayParser;

public class MultiArrayParserValidator {

    public static void assertValuePartValid(String multiplyPart) throws MultyArrayParserException {
        multiplyPart = multiplyPart.trim();
        if (multiplyPart.isEmpty()) {
            throw new MultyArrayParserException(
                    String.format(
                        "Пропущено значение"
                    )
            );
        }
    }

    public static void assertPartWithBracketsValid(String partWithBrackets, String partName) throws MultyArrayParserException {
        partWithBrackets = partWithBrackets.trim();
        if (partWithBrackets.charAt(0) != MultiArrayParser.OPEN_BRACKET || partWithBrackets.charAt(partWithBrackets.length() - 1) != MultiArrayParser.CLOSE_BRACKET) {
            throw new MultyArrayParserException(
                    String.format(
                            "Элемент '%s' должен начинаться и заканчиваться квадратными скобками: %s",
                            partName, partWithBrackets
                    )
            );
        }
    }

    public static void assertDigitPrefixSize(int digitPrefixSize) throws MultyArrayParserException {
        if (digitPrefixSize == 0) {
            throw  new MultyArrayParserException(
                    "Число не может быть пустым"
            );
        }
    }
}
