package view.core;

import view.menu.Menu;

public class Builder {

    private Menu rootMenu;

    public Builder() {

    }

    public Builder buildMenu(Menu menu) {
        this.rootMenu = menu;
        return this;
    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}
