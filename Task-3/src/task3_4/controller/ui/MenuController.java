package task3_4.controller.ui;

import task3_4.view.core.Builder;
import task3_4.view.core.Navigator;
import task3_4.view.menu.Menu;

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
