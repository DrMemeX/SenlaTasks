package task3_4.view.util;

public final class ConsoleView {

    private static final ConsoleView INSTANCE = new ConsoleView();
    private ConsoleView() {}

    public static void  hr() {
        System.out.println("--------------------------------------------------");
    }

    public static void title(String t) {
        hr();
        System.out.println(t);
        hr();
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void warn(String msg) {
        System.out.println("[!] " + msg);
    }

    public static void ok(String msg) {
        System.out.println("[OK] " + msg);
    }
}
