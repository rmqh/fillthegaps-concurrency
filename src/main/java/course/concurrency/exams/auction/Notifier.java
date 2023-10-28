package course.concurrency.exams.auction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifier {

    private final ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

    public void sendOutdatedMessage(Bid bid) {
        service.submit(this::imitateSending);
    }

    private void imitateSending() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    public void shutdown() {
    }
}
