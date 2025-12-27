package task9_3;

import java.util.ArrayDeque;
import java.util.Queue;

public class Buffer {
    private final Object lock = new Object();
    private final Queue<Integer> buffer = new ArrayDeque<>();
    private final int capacity;

    public Buffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("объём буфера должен быть больше 0");
        }
        this.capacity = capacity;
    }

    public void put(int value) throws InterruptedException {
        synchronized (lock) {
            while (buffer.size() == capacity) {
                lock.wait();
            }
            buffer.add(value);
            System.out.println("Producer отдал в буфер: " + value + " (буфер=" + buffer.size() + ")");
            lock.notifyAll();
        }
    }

    public void take() throws InterruptedException {
        synchronized (lock) {
            while (buffer.isEmpty()) {
                lock.wait();
            }
            int value = buffer.remove();
            System.out.println("Consumer забрал из буфера: " + value + " (буфер=" + buffer.size() + ")");
            lock.notifyAll();
        }
    }
}
