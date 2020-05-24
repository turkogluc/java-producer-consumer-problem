
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Main {

    private static int SIZE = 20;
    private static int NUMBER_OF_TASKS = 50;

    public static void main(String[] args) {

        Queue buffer = new Queue(SIZE);
        Consumer consumer = new Consumer(buffer);
        Producer producer = new Producer(buffer);

        ExecutorService executorService = Executors.newWorkStealingPool();

        List<Callable<Boolean>> taskList = new ArrayList<>();
        taskList.add(consumer.startConsuming(NUMBER_OF_TASKS));
        taskList.add(producer.startProducing(NUMBER_OF_TASKS));


        try {
            List <Future <Boolean>> futures = executorService.invokeAll(taskList);

            // wait for all threads to finish
            for (Future <Boolean> future : futures) {
                future.get();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
            System.out.println("Main exits.");
        }
    }
}
