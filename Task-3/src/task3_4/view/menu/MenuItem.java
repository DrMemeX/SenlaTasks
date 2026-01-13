package task3_4.view.menu;

import task3_4.exceptions.dao.DaoException;
import task3_4.exceptions.domain.DomainException;
import task3_4.exceptions.ui.UserInputException;
import task3_4.view.action.IAction;
import task3_4.view.util.ConsoleView;

public class MenuItem {
    private final String title;
    private final IAction action;
    private Menu nextMenu;

    public MenuItem(String title, IAction action) {
        this.title = title;
        this.action = action;
    }

    public String getTitle() {return title;}

    public IAction getAction() {return action;}

    public Menu getNextMenu() {return nextMenu;}
    public void setNextMenu(Menu nextMenu) {this.nextMenu = nextMenu;}

    public void doAction() {
        if (action == null) return;

        try {
            action.execute();
        } catch (UserInputException | DomainException e) {
            ConsoleView.warn(e.getMessage());
        } catch (DaoException e) {
            ConsoleView.warn("Ошибка базы данных: " + e.getMessage());
        } catch (RuntimeException e) {
            ConsoleView.warn("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
