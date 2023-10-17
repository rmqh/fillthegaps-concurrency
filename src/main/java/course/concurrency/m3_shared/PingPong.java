package course.concurrency.m3_shared;

public class PingPong {

    private static final Object OBJ_SYNC = new Object();
    private static final String PING = "Ping";
    private static final String PONG = "Pong";

    private static boolean flag;

    public static void ping() {
        while (true) {
            synchronized (OBJ_SYNC) {
                while (flag) {
                    waitForNotification();
                }
                print(PING);
                flag = !flag;
                sendNotification();
            }
        }
    }

    public static void pong() {
        while (true) {
            synchronized (OBJ_SYNC) {
                while (!flag) {
                    waitForNotification();
                }
                print(PONG);
                flag = !flag;
                sendNotification();
            }
        }
    }

    private static void waitForNotification() {
        try {
            OBJ_SYNC.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendNotification() {
        OBJ_SYNC.notify();
    }

    private static void print(String msg) {
        System.out.println(msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> ping());
        Thread t2 = new Thread(() -> pong());
        t1.start();
        t2.start();
    }
}
