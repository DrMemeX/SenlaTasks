package view.menu;

import view.action.IAction;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final String name;
    private final List<MenuItem> items = new ArrayList<>();

    public Menu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getItems() {
        return items;
    }


    public Menu add(String title, IAction action) {
        items.add(new MenuItem(title, action));
        return this;
    }

    public Menu add(String title, Menu submenu) {
        MenuItem mi = new MenuItem(title, null);
        mi.setNextMenu(submenu);
        items.add(mi);
        return this;
    }
}
