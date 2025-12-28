package task9_3;

import java.util.Random;

public class Producer implements Runnable {
    private final Buffer buffer;
    private final int count;
    private final Random random = new Random();

    public Producer(Buffer buffer, int count) {
        this.buffer = buffer;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            int value = random.nextInt(100);
            try {
                buffer.put(value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

