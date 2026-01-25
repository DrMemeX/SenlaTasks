package controller.ui;

import view.core.Builder;
import view.core.Navigator;
import view.menu.Menu;

public class MenuController {
    private final Builder builder;
    private final Navigator navigator;

    public MenuController(Builder builder, Navigator navigator) {
        this.builder = builder;
        this.navigator = navigator;
    }

    public void run() {
        Menu root = builder.getRootMenu();
        if (root != null) {
            navigator.run(root);
        }
    }
}
