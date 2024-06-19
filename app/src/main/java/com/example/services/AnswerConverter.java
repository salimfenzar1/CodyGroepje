package com.example.services;

public class AnswerConverter {
    public static Answers determineAnswer(String input) {

        input = input.trim().toLowerCase();

        if (input.contains("ja")) {
            return Answers.YES;
        } else if (input.contains("nee")) {
            return Answers.NO;
        } else {
            return Answers.UNKNOWN;
        }
    }

    @Deprecated
    private static Answers isYes(String input) {
        switch (input.trim().toLowerCase()) {
            case "ja":
                return Answers.YES;
            case "nee":
                return Answers.NO;
            default:
                return Answers.UNKNOWN;
        }
    }

    public enum Answers {
        YES,
        NO,
        MAYBE,
        UNKNOWN
    }
}