package com.anastasia.app.multiarray.eval;

import com.anastasia.app.multiarray.array.MultiArray;
import com.anastasia.app.multiarray.array.MultiArrayArray;
import com.anastasia.app.multiarray.array.MultiArrayNumber;
import com.anastasia.app.multiarray.validation.EvaluationException;
import com.anastasia.app.multiarray.validation.EvaluationValidator;
import com.anastasia.app.multiarray.validation.MultiArrayParserValidator;
import com.anastasia.app.multiarray.validation.MultyArrayParserException;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MultiArrayParser {

    public static final String ASSIGN = ":=";
    public static final String ADD = "+", SUBTRACT = "-", MULTIPLY = "*";
    public static final char OPEN_BRACKET = '[', CLOSE_BRACKET = ']';

    MultiArrayEnvironment environment;

    MultiArrayParser(MultiArrayEnvironment environment) {
        this.environment = environment;
    }

    public MultiArrayReference parseAssignmentPart(String assignmentPart) throws EvaluationException {
        assignmentPart = assignmentPart.trim();

        Deque<MultiArrayReference> additiveValues = new ArrayDeque<>();
        Deque<String> additiveOperations = new ArrayDeque<>();

        for (int start = 0, length = assignmentPart.length(); start < length; ) {
            int addIndex = assignmentPart.indexOf(ADD, start);
            int subtractIndex = assignmentPart.indexOf(SUBTRACT, start);

            if (addIndex < 0) addIndex = length;
            if (subtractIndex < 0) subtractIndex = length;

            int operationLength = 0;
            int end = length;

            if (addIndex < subtractIndex) {
                end = addIndex;
                operationLength = ADD.length();
            } else {
                end = subtractIndex;
                operationLength = SUBTRACT.length();
            }

            String additivePart = assignmentPart.substring(start, end);
            String additiveOperation = (
                    end == length
                    ? ""
                    : additivePart.substring(end, end + operationLength)
            );

            MultiArrayReference additiveValue = parseAdditivePart(additivePart);

            additiveValues.add(additiveValue);
            additiveOperations.add(additiveOperation);

            start = end + operationLength;
        }

        if (additiveValues.size() == 1) {
            return additiveValues.getFirst();
        }

        MultiArray leftValue = additiveValues.poll().get();
        while (additiveValues.size() > 0) {
            MultiArray rightValue = additiveValues.poll().get();
            String operation = additiveOperations.poll();

            switch (operation) {
                case ADD:
                    leftValue = leftValue.add(rightValue);
                    break;
                case SUBTRACT:
                    leftValue = leftValue.subtract(rightValue);
                    break;
            }
        }

        return new MultiArrayReference(environment, null, leftValue);
    }

    public MultiArrayReference parseAdditivePart(String additivePart) throws EvaluationException {
        additivePart = additivePart.trim();

        Deque<MultiArrayReference> multiplyValues = new ArrayDeque<>();
        Deque<String> multiplyOperations = new ArrayDeque<>();

        for (int start = 0, length = additivePart.length(); start < length; ) {
            int multiplyIndex = additivePart.indexOf(MULTIPLY);

            if (multiplyIndex < 0) multiplyIndex = length;

            int operationLength = 0;
            int end;

            {
                end = multiplyIndex;
                operationLength = MULTIPLY.length();
            }

            String multiplyPart = additivePart.substring(start, end);
            String multiplyOperation = (
                    end == length
                            ? ""
                            : additivePart.substring(end, end + operationLength)
            );

            MultiArrayReference multiplyValue = parseValuePart(multiplyPart);

            multiplyValues.add(multiplyValue);
            multiplyOperations.add(multiplyOperation);

            start = end + operationLength;
        }

        if (multiplyValues.size() == 1) {
            return multiplyValues.getFirst();
        }

        MultiArray leftValue = multiplyValues.poll().get();
        while (multiplyValues.size() > 0) {
            MultiArray rightValue = multiplyValues.poll().get();
            String operation = multiplyOperations.poll();

            switch (operation) {
                case MULTIPLY:
                    leftValue = leftValue.multiply(rightValue);
                    break;
            }
        }

        return new MultiArrayReference(environment, null, leftValue);
    }

    static class ValueParsingResult {

        MultiArray value;
        int lastUsedIndex;
        String name;

        public ValueParsingResult(MultiArray value, int lastUsedIndex) {
            this(value, lastUsedIndex, null);
        }

        public ValueParsingResult(MultiArray value, int lastUsedIndex, String name) {
            this.value = value;
            this.lastUsedIndex = lastUsedIndex;
            this.name = name;
        }
    }

    public MultiArrayReference parseValuePart(String valuePart) throws EvaluationException {
        valuePart = valuePart.trim();

        MultiArrayParserValidator.assertValuePartValid(valuePart);

        char firstChar = valuePart.charAt(0);

        ValueParsingResult result;

        if (OPEN_BRACKET == firstChar) {
            result = parseConstantArrayPart(valuePart);
        } else if (Character.isDigit(firstChar)) {
            result = parseConstantNumberPart(valuePart);
        } else if (Character.isLetter(firstChar)){
            result = parseVariablePart(valuePart);
        } else {
            throw new MultyArrayParserException(
                    String.format(
                            "Значение не может начинаться с символа %c",
                            firstChar
                    )
            );
        }

        String indexesPart = valuePart.substring(result.lastUsedIndex + 1);
        List<Integer> indexes = parseIndexesPart(indexesPart);

        MultiArray value = result.value;
        int lastIndex = -1;
        for (int index : indexes) {
            if (lastIndex != -1) {
                EvaluationValidator.assertValueExists(value, result.name);
                value = value.get(lastIndex);
            }

            lastIndex = index;
        }

        return new MultiArrayReference(environment, result.name, value, lastIndex);
    }

    static int[] getCloseBracketIndexes(String valuePart) throws MultyArrayParserException {
        valuePart = valuePart.trim();

        int n = valuePart.length();
        int[] closeBracketIndexes = new int[n];

        Deque<Integer> openBracketIndexes = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            closeBracketIndexes[i] = -1;

            char ch = valuePart.charAt(i);

            if (OPEN_BRACKET == ch) {
                openBracketIndexes.push(i);
            } else if (CLOSE_BRACKET == ch) {
                if (openBracketIndexes.isEmpty()) {
                    throw new MultyArrayParserException(
                            String.format(
                                    "Закрывающая скобка без соответствующей открывающей: '%s'[%d]",
                                    valuePart, i + 1
                            )
                    );
                }

                int openBracketIndex = openBracketIndexes.pop();
                closeBracketIndexes[openBracketIndex] = i;
            }
        }

        if (!openBracketIndexes.isEmpty()) {
            int openBracketIndex = openBracketIndexes.pop();
            throw new MultyArrayParserException(
                    String.format(
                            "Открывающая скобка без соответствующей закрывающей: '%s'[%d]",
                            valuePart, openBracketIndex + 1
                    )
            );
        }

        return closeBracketIndexes;
    }

    public ValueParsingResult parseConstantArrayPart(String constantArrayPart) throws EvaluationException {
        constantArrayPart = constantArrayPart.trim();

        MultiArrayParserValidator.assertPartWithBracketsValid(constantArrayPart, "массив");

        int[] closeBracketIndexes = getCloseBracketIndexes(constantArrayPart);

        int mainArrayCloseBracketIndex = closeBracketIndexes[0];

        List<String> partStrings = new ArrayList<>();

        int lastSpace = 0;
        for (int i = 1; i < mainArrayCloseBracketIndex; ++i) {
            char ch = constantArrayPart.charAt(i);

            if (OPEN_BRACKET == ch) {
                i = closeBracketIndexes[i];
            } else if (' ' == ch) {
                int partStart = lastSpace + 1;
                if (partStart != i) {
                    String partString = constantArrayPart.substring(partStart, i);
                    partStrings.add(partString);
                }

                lastSpace = i;
            }
        }

        if (lastSpace != mainArrayCloseBracketIndex - 1) {
            String lastPartString = constantArrayPart.substring(lastSpace + 1, mainArrayCloseBracketIndex);
            partStrings.add(lastPartString);
        }

        int size = partStrings.size();

        MultiArray value = new MultiArrayArray(size);
        for (int i = 0; i < size; ++i) {
            String partString = partStrings.get(i);
            MultiArrayReference innerValue = parseValuePart(partString);
            value.set(i, innerValue.get());
        }

        return new ValueParsingResult(
                value,
                mainArrayCloseBracketIndex
        );
    }

    public ValueParsingResult parseConstantNumberPart(String constantNumberPart) throws MultyArrayParserException {
        constantNumberPart = constantNumberPart.trim();

        int digitPrefixSize = 0;
        while (digitPrefixSize < constantNumberPart.length()
                && Character.isDigit(constantNumberPart.charAt(digitPrefixSize))
                ) {
            ++digitPrefixSize;
        }

        MultiArrayParserValidator.assertDigitPrefixSize(digitPrefixSize);

        String numberPart = constantNumberPart.substring(0, digitPrefixSize);
        BigInteger number = new BigInteger(numberPart);

        MultiArray value = new MultiArrayNumber(number);
        return new ValueParsingResult(
                value,
                digitPrefixSize - 1
        );
    }

    public ValueParsingResult parseVariablePart(String variablePart) throws EvaluationException {
        variablePart = variablePart.trim();

        int namePrefixSize = 0;
        while (namePrefixSize < variablePart.length()
                && Character.isLetter(variablePart.charAt(namePrefixSize))
                ) {
            ++namePrefixSize;
        }

        String namePart = variablePart.substring(0, namePrefixSize);

        MultiArray value = environment.getVariable(namePart);
        return new ValueParsingResult(
                value,
                namePrefixSize - 1,
                namePart
        );
    }

    public static final BigInteger
            MIN_BIG_INDEX = BigInteger.valueOf(Integer.MIN_VALUE),
            MAX_BIG_INDEX = BigInteger.valueOf(Integer.MAX_VALUE);

    public List<Integer> parseIndexesPart(String indexesPart) throws MultyArrayParserException {
        indexesPart = indexesPart.trim();

        List<Integer> indexes = new ArrayList<>();
        if (indexesPart.isEmpty()) {
            return indexes;
        }

        MultiArrayParserValidator.assertPartWithBracketsValid(indexesPart, "индексы");

        int[] outerCloseBracketIndexes = getCloseBracketIndexes(indexesPart);
        for (int i = 0; i < indexesPart.length(); ++i) {
            int closeBracketIndex = outerCloseBracketIndexes[i];
            if (-1 != closeBracketIndex) {
                String indexPart = indexesPart.substring(i + 1, closeBracketIndex);

                ValueParsingResult indexParsingResult = parseConstantNumberPart(indexPart);
                MultiArray value = indexParsingResult.value;

                BigInteger indexBigValue = ((MultiArrayNumber)value).getValue();
                if (indexBigValue.compareTo(MIN_BIG_INDEX) < 0) indexBigValue = MIN_BIG_INDEX;
                if (indexBigValue.compareTo(MAX_BIG_INDEX) > 0) indexBigValue = MAX_BIG_INDEX;

                int index = indexBigValue.intValue();
                indexes.add(index);

                i = closeBracketIndex;
            }
        }

        return indexes;
    }
}
