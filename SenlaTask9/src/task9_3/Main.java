package task9_3;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner console = new Scanner(System.in);

        int capacity = readPositiveInt(console, "Введите размер буфера (целое > 0): ");
        int count = readPositiveInt(console, "Введите количество элементов (целое > 0): ");

        Buffer buffer = new Buffer(capacity);

        Thread producer = new Thread(new Producer(buffer, count), "Producer");
        Thread consumer = new Thread(new Consumer(buffer, count), "Consumer");

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
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
