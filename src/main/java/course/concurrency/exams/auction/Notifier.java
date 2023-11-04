package course.concurrency.exams.auction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Notifier {

    private static final int iterations = 3_000_000;

    private final ExecutorService service = new ThreadPoolExecutor(
        32,
        64,
        60L,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(iterations)
    );

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
