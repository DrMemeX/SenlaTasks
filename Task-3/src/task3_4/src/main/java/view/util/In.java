package view.util;

import exceptions.ui.EmptyInputException;
import exceptions.ui.InvalidInputFormatException;
import exceptions.ui.InvalidMenuChoiceException;

import java.util.Scanner;

public final class In {
    private final static In INSTANCE = new In();
    private final Scanner sc = new Scanner(System.in);

    private In() { }

    public static In get() {
        return INSTANCE;
    }

    public String line(String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s.isBlank()) {
            throw new EmptyInputException();
        }
        return s;
    }

    public int intInRange(String prompt, int min, int max) {
        System.out.print(prompt);
        String s = sc.nextLine().trim();

        if (s.isBlank()) {
            throw new EmptyInputException();
        }

        int v;
        try {
            v = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new InvalidInputFormatException(s);
        }

        if (v < min || v > max) {
            throw new InvalidMenuChoiceException(v);
        }

        return v;
    }
}
