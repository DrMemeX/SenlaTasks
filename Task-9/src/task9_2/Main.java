package task9_2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Printer printer = new Printer();

        Scanner console = new Scanner(System.in);
        int counter = readPositiveInt(console);

        Thread t1 = createPrintingThread(printer, counter, true, "T1");
        Thread t2 = createPrintingThread(printer, counter, false, "T2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static int readPositiveInt(Scanner console) {
        while (true) {
            System.out.println("Сколько раз печатать каждому потоку? ");
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

    private static Thread createPrintingThread(Printer printer, int counter, boolean myTurn, String name) {
        return new Thread(() -> {
            for (int i = 0; i < counter; i++) {
                try {
                    printer.print(Thread.currentThread().getName(), myTurn);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, name);
    }
}
