package task9_3;

public class Consumer implements Runnable {
    private final Buffer buffer;
    private final int count;

    public Consumer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            try {
                buffer.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

