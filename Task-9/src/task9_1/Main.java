package task9_1;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        MyThread t = new MyThread(lock);

        printState(t);

        t.start();
        printState(t);

        synchronized(lock) {
            awaitState(t, Thread.State.BLOCKED);
            printState(t);
        }

        awaitState(t, Thread.State.WAITING);
        printState(t);

        synchronized (lock) {
            lock.notifyAll();
        }

        awaitState(t, Thread.State.TIMED_WAITING);
        printState(t);

        t.join();
        printState(t);
    }

    private static void printState(Thread t) {
        System.out.println(t.getState());
    }

    private static void awaitState(Thread t, Thread.State target) {
        while (t.getState() != target) {
            Thread.yield();
        }
    }
}
