package com.anastasia.app.multiarray;

import com.anastasia.app.multiarray.eval.MultiArrayEnvironment;

import java.util.Scanner;

public class Main {

    public static final String META_COMMAND_PREFIX = ".";
    public static final String CLEAR = "clear", END = "end", PRINT = "print";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        MultiArrayEnvironment environment = MultiArrayEnvironment.create();

        evaluation:
        while (true) {
            String line = in.nextLine();
            if (line == null) break;

            if (line.startsWith(META_COMMAND_PREFIX)) {
                String command = line.substring(META_COMMAND_PREFIX.length());
                switch (command) {
                    case CLEAR:
                        environment.clear();
                        break;
                    case END:
                        break evaluation;
                    case PRINT:
                        String environmentString = environment.toString();
                        System.out.println(environmentString);
                        break;
                }
            } else {

            }
        }
    }
}
