package task3_4.app.state;

import task3_4.exceptions.state.*;
import task3_4.view.util.ConsoleView;

import java.io.*;

public class BinaryStateStorage {

    private static final String STATE_FILE = "task3_4/app_state.bin";

    public void saveState(AppState state) {
        File file = new File(STATE_FILE);

        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(STATE_FILE))) {

            oos.writeObject(state);
            ConsoleView.ok("Состояние сохранено в " + STATE_FILE);

        } catch (IOException e) {
            StateSaveException ex =
                    new StateSaveException("Ошибка сохранения состояния в файл: " + STATE_FILE, e);

            ConsoleView.warn(ex.getMessage());
            throw ex;
        }
    }

    public AppState loadState() {
        File file = new File(STATE_FILE);

        if (!file.exists()) {
            StateFileNotFoundException ex =
                    new StateFileNotFoundException(STATE_FILE);

            ConsoleView.warn(ex.getMessage());
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(file))) {

            Object obj = ois.readObject();

            if (obj instanceof AppState state) {
                ConsoleView.ok("Состояние загружено из " + STATE_FILE);
                return state;
            } else {
                StateLoadException ex =
                        new StateLoadException(
                                "Неверный формат файла состояния. " +
                                "Файл повреждён или несовместим с текущей версией программы.",
                                null
                        );

                ConsoleView.warn(ex.getMessage());
                throw ex;
            }

        } catch (Exception e) {
            StateLoadException ex =
                    new StateLoadException("Ошибка загрузки файла состояния: " + STATE_FILE, e);

            ConsoleView.warn(ex.getMessage());
            throw ex;
        }
    }
}
