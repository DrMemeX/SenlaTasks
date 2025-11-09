package task3_4.view.util;

import java.util.Scanner;

public final class In {
    private final static In INSTANCE = new In();
    private final Scanner sc = new Scanner(System.in);

    private In() {}

    public static In get() {
        return INSTANCE;
    }

    public String line(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public int intInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max)
                    throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.printf("Введите число от %d до %d%n", min, max);
            }
        }
    }
}
