package task3_2;

import task3_2.flowers.*;
import java.util.ArrayList;
import java.util.List;

public class Bouquet {
    private List<Flower> flowers = new ArrayList<>();

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Flower f : flowers) {
            total += f.getPrice();
        }
        return total;
    }

    public void showBouquet() {
        if (flowers.isEmpty()) {
            System.out.println("Букет пуст.");
        } else {
            System.out.println("Состав букета: ");
            for (Flower f : flowers) {
                System.out.println(" -" + f);
            }
            System.out.println("Общая стоимость: " + getTotalPrice() + " руб.");
        }
    }

    public void clear() {
        flowers.clear();
        System.out.println("Букет очищен.");
    }
}
