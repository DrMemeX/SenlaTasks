package task3_4.view.menu;

import task3_4.view.action.IAction;

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
        if(action != null) {
            action.execute();
        }
    }
}
