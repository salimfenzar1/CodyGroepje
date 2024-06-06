package com.example;

import android.util.Log;

public class AnswerConverter {

    private static final String[] yesAnswers = {
            "ja",
            "wel",
            "zeker",
            "absoluut",
            "natuurlijk",
            "zeker",
            "inderdaad",
            "uiteraard",
            "jazeker",
            "graag",
            "helemaal",
            "precies",
            "exact",
            "klopt",
            "waar",
            "jep",
            "jaja",
            "juist",
            "oke",
            "akkoord",
            "prima",
    };

    private static final String[] noAnswers = {
            "nee",
            "nope",
            "geen",
            "niet",
            "no",
            "uitgesloten",
            "negatief",
            "niks",
            "nooit",
    };

    private static final String[] maybeAnswers = {
            "hmm",
            "nou",
            "maar",
            "even",
            "denken",
            "wacht",
            "laat",
            "nadenken",
            "kijken",
            "bedoel",
            "eigenlijk",
            "klinkt",
            "voorstellen",
            "vraag",
            "weet",
            "twijfel",
            "lastig",
            "moeite",
            "snap",
            "nog",
            "helder",
            "werk",
            "details",
            "regels",
            "inzicht",
            "onzeker",
            "moeilijk",
            "begrijp",
            "onduidelijk",
            "vraagtekens",
            "verduidelijking",
            "misschien",
            "begrip",
            "volg",
            "twijfels",
            "verheldering",
            "uit",
            "herhalen",
            "interpretatie",
            "nuances",
            "toelichting",
            "zelfverzekerd",
            "voorbeeld",
            "antwoord",
            "ruimte"
    };


    public static Answers determineAnswer(String input) {
        // Temporary method to replace when full user story needs to be done
        // TODO: Implement proper method, including yes, no, maybe, and unknown
        input = input.trim().toLowerCase();
        // Maybe
        {
            for (String answer : maybeAnswers) {
                if (input.contains(answer)) {
                    Log.i("AnswerConverter", "Output: Maybe, Input contains: " + answer);
                    return Answers.MAYBE;
                }
            }
            if (input.contains("zeker") && input.contains("niet")) {
                Log.i("AnswerConverter", "Output: Maybe, Input contains: niet, zeker");
                return Answers.MAYBE;
            }
        }

        // No
        {
            for (String answer : noAnswers) {
                if (input.contains(answer)) {
                    Log.i("AnswerConverter", "Output: No, Input contains: " + answer);
                    return Answers.NO;
                }
            }
        }

        // Yes
        {
            for (String answer : yesAnswers) {
                if (input.contains(answer)) {
                    Log.i("AnswerConverter", "Output: Yes, Input contains: " + answer);
                    return Answers.YES;
                }
            }
        }


        return Answers.UNKNOWN;
    }

    private static Answers simpleIsYes(String input) {
        switch (input.trim().toLowerCase()) {
            case "ja": return Answers.YES;
            case "nee": return Answers.NO;
            default: return Answers.UNKNOWN;
        }
    }
    public enum Answers {
        YES,
        NO,
        MAYBE,
        UNKNOWN
    }
}
