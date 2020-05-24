import java.util.concurrent.Callable;


class Producer {

    private final Queue buffer;

    Producer(Queue buffer) {
        this.buffer = buffer;
    }


    Callable<Boolean> startProducing(int numberOfTasks) {
        return () -> {
            for (int i = 1; i < numberOfTasks; i++) {
                buffer.addItem(i);
            }
            Action.producerLog("Producer exits");
            return true;
        };

    }

}
