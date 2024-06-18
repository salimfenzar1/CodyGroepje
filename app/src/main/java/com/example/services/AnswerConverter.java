package com.example.services;

public class AnswerConverter {
    public static Answers determineAnswer(String input) {
        // Temporary method to replace when full user story needs to be done
        // TODO: Implement proper method, including yes, no, maybe, and unknown
        return isYes(input);
    }

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