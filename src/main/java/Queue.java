import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Queue {

    private final Deque <Integer> buffer;
    private final ReentrantLock mutex;
    private final Semaphore consumerSemaphore;
    private final Semaphore producerSemaphore;

    public Queue(int size) {
        this.buffer = new LinkedList <>();
        this.mutex = new ReentrantLock();
        this.consumerSemaphore = new Semaphore(0);
        this.producerSemaphore = new Semaphore(size);
    }

    void addItem(Integer i) throws InterruptedException {

        // wait to get permit to add new item. If queue reaches the limit it will be blocked
        Action.producerLog("Wait to get permit");
        producerSemaphore.acquire();

        // exclusive lock for queue to add item
        mutex.lock();
        Action.producerLog("Lock. ITEM: " + i);
        Action.producerLog("Add to buffer. ITEM: " + i);

        // add item
        buffer.addFirst(i);

        Action.producerLog("Unlock. ITEM: " + i);
        // release the lock
        mutex.unlock();

        Action.producerLog("Signal the consumer. ITEM: " + i);
        //  signal the consumer that new item added and can be retrieved
        consumerSemaphore.release();
    }

    Integer getNextItem() throws InterruptedException {
        // wait to get permit. If queue is empty it will be blocked
        Action.consumerLog("Wait to get permit.");
        consumerSemaphore.acquire();

        // exclusive lock for queue to retrieve item
        mutex.lock();
        Action.consumerLog("Lock.");

        // get the item and remove it from queue
        Integer lastItem = buffer.removeLast();

        // release the lock
        Action.consumerLog("Unlock.");
        mutex.unlock();

        producerSemaphore.release();

        return lastItem;
    }

}
