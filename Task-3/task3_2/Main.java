package task3_2;

import task3_2.flowers.*;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bouquet bouquet = new Bouquet();
        Random random = new Random();

        System.out.println("### Цветочный магазин ###");

        int roses = getFlowerCount(scanner, random, "роз");
        int tulips = getFlowerCount(scanner, random, "тюльпанов");
        int chamomiles = getFlowerCount(scanner, random, "ромашек");

        for (int i = 0; i < roses; i++) bouquet.addFlower(new Rose());
        for (int i = 0; i < tulips; i++) bouquet.addFlower(new Tulip());
        for (int i = 0; i < chamomiles; i++) bouquet.addFlower(new Chamomile());

        System.out.println("\nБукет готов!");
        bouquet.showBouquet();

        scanner.close();
    }

    private static int getFlowerCount(Scanner scanner, Random random, String flowerName) {
        System.out.print("Введите количество " + flowerName + ": ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
            if (count < 0) throw new NumberFormatException();
        } catch (Exception e) {
            count = random.nextInt(26);
            System.out.println("Некорректный ввод. Установлено случайное значение: " + count);
            return count;
        }

        return count;
    }
}
