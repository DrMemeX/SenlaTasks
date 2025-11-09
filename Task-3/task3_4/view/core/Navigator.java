package task3_4.view.core;

import task3_4.view.menu.Menu;
import task3_4.view.menu.MenuItem;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.util.List;

public class Navigator {

    private Menu currentMenu;

    public Navigator() {}

    public void run(Menu startMenu) {
        this.currentMenu = startMenu;
        while (currentMenu != null) {
            printMenu();
            int choice = In.get().intInRange("Выберите пункт: ", 0, currentMenu.getItems().size());
            if (choice == 0) {
                currentMenu = null;
                break;
            }
            navigate(choice);
        }
    }

    public void printMenu() {
        ConsoleView.title(currentMenu.getName());
        List<MenuItem> items = currentMenu.getItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, items.get(i).getTitle());
        }
        System.out.println("0) Выход");
        ConsoleView.hr();
    }

    public void navigate(int index) {
        MenuItem item = currentMenu.getItems().get(index - 1);

        if (item.getNextMenu() != null) {
           currentMenu = item.getNextMenu();
           return;
        }
        item.doAction();
    }
}