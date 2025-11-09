package task3_4.app;

import task3_4.model.service.BookStore;
import task3_4.controller.ui.MenuController;
import task3_4.view.core.Builder;
import task3_4.view.core.Navigator;
import task3_4.view.factory.DefaultUiActionFactory;
import task3_4.view.factory.IUiActionFactory;

public final class App {
    private App() {}
    public static void run() {
        BookStore store = new BookStore();

        AppInitializer.initialize(store);

        IUiActionFactory factory = new DefaultUiActionFactory(store);
        Builder builder = new Builder().buildMenu(factory.buildRootMenu());
        MenuController controller = new MenuController(builder, new Navigator());

        controller.run();
    }
}