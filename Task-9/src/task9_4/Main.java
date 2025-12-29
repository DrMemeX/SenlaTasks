package task9_4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner console = new Scanner(System.in);

        System.out.println("Служебный поток на протяжении m секунд\n" +
                        "будет каждые n секунд выводить системное время.");

        int mSeconds = readPositiveInt(console,
                "Введите m: ");
        int nSeconds = readPositiveInt(console,
                "Введите n: ");

        TimeDaemon timeDaemon = new TimeDaemon(nSeconds);
        timeDaemon.start();

        Thread.sleep(mSeconds * 1000L);
    }

    private static int readPositiveInt(Scanner console, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = console.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("Введите число больше 0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }
}
