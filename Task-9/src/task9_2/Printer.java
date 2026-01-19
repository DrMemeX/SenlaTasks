package task9_2;

public class Printer {
    private final Object lock = new Object();
    private boolean turn = true;

    public void print(String name, boolean myTurn) throws InterruptedException {
        synchronized (lock) {
            while (turn != myTurn) {
                lock.wait();
            }
            System.out.println(name);
            turn = !turn;
            lock.notifyAll();
        }
    }
}
