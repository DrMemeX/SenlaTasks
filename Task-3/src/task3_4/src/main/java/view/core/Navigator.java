package view.core;

import exceptions.ui.UserInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.menu.Menu;
import view.menu.MenuItem;
import view.util.ConsoleView;
import view.util.In;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Navigator {

    private static final Logger log = LoggerFactory.getLogger(Navigator.class);

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

                log.info("INPUT choice={} menu={}", choice, currentMenu.getName());

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
                log.warn("USER_INPUT_ERROR menu={} message={}",
                        currentMenu != null ? currentMenu.getName() : "null",
                        e.getMessage());

                ConsoleView.warn(e.getMessage());
                ConsoleView.info("Попробуйте снова.\n");
            } catch (Exception e) {
                log.error("COMMAND_ERROR menu={} message={}",
                        currentMenu != null ? currentMenu.getName() : "null",
                        e.getMessage(),
                        e);

                ConsoleView.warn("Ошибка выполнения команды: " + e.getMessage());
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
            return;
        }

        String commandName = item.getTitle();
        log.info("START command={} menu={}", commandName, currentMenu.getName());

        item.doAction();
        log.info("SUCCESS command={} menu={}", commandName, currentMenu.getName());
    }
}
