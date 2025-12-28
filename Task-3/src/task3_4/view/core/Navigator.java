package task3_4.view.core;

import task3_4.exceptions.ui.UserInputException;
import task3_4.view.menu.Menu;
import task3_4.view.menu.MenuItem;
import task3_4.view.util.ConsoleView;
import task3_4.view.util.In;

import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class Navigator {

    private Menu currentMenu;
    private final Deque<Menu> history = new ArrayDeque<>();

    public Navigator() {
    }

    public void run(Menu startMenu) {
        this.currentMenu = startMenu;

        while (currentMenu != null) {
            try {
                printMenu();

                int choice = In.get().intInRange(
                        "Выберите пункт: ",
                        0,
                        currentMenu.getItems().size()
                );

                if (choice == 0) {
                    if (history.isEmpty()) {
                        currentMenu = null;
                        break;
                    } else {
                        currentMenu = history.pop();
                        continue;
                    }
                }

                navigate(choice);

            } catch (UserInputException e) {
                ConsoleView.warn(e.getMessage());
                ConsoleView.info("Попробуйте снова.\n");
            }
        }
    }

    private void printMenu() {
        ConsoleView.title(currentMenu.getName());
        List<MenuItem> items = currentMenu.getItems();

        for (int i = 0; i < items.size(); i++) {
            ConsoleView.info((i + 1) + ") " + items.get(i).getTitle());
        }

        String zeroLabel = history.isEmpty() ? "Выход" : "Назад";
        ConsoleView.info("0) " + zeroLabel);
        ConsoleView.hr();
    }

    private void navigate(int index) {
        MenuItem item = currentMenu.getItems().get(index - 1);

        if (item.getNextMenu() != null) {
            history.push(currentMenu);
            currentMenu = item.getNextMenu();
        } else {
            item.doAction();
        }
    }
}
