package task9_4;

public class TimeDaemon extends Thread {
    private final int seconds;

    public TimeDaemon(int seconds) {
        this.seconds = seconds;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            System.out.println(java.time.LocalTime.now());
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }
}
