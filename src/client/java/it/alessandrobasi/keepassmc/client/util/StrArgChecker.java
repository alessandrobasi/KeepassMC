package it.alessandrobasi.keepassmc.client.util;

import java.util.List;

public class StrArgChecker {

    public enum Result {
        FIRST_ARG,
        BOTH_ARG;
    }


    public static Result check(String arg1, String arg2) {

        List<String> setarg1 = extract(arg1);
        List<String> setarg2 = extract(arg2);


        // both args could have brackets
        if(setarg1.getFirst().equals(setarg2.getFirst()) && setarg1.getLast().equals(setarg2.getLast())) {
            return Result.BOTH_ARG;
        }

        return Result.FIRST_ARG;

    }

    private static List<String> extract(String arg) {
        int len = arg.length();

        String firstChar = arg.substring(0, 1);
        String lastChar = arg.substring(len -1, len);

        return List.of(firstChar, lastChar);

    }


}
