import java.util.concurrent.Callable;

class Consumer {

    private final Queue buffer;

    Consumer(Queue buffer) {
        this.buffer = buffer;
    }

    Callable<Boolean> startConsuming(int numberOfTasks) {
        return () -> {
            for(int i=1; i < numberOfTasks; i++) {
                Integer nextItem = buffer.getNextItem();
                processItem(nextItem);
            }
            Action.consumerLog("Consumer exits.");
            return true;
        };

    }

    private void processItem(Integer item) {
        // process the item
    }

}
