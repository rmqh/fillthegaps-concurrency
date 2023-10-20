package course.concurrency.m3_shared;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShopStatistics {

    private static final Lock LOCK = new ReentrantLock();

    private Long totalCount = 0L;
    private Long totalRevenue = 0L;

    public void addData(Long count, Long price) {
        try {
            LOCK.lock();
            totalCount += count;
            totalRevenue += (price * count);
        } finally {
            LOCK.unlock();
        }
    }

    public Long getTotalCount() {
        try {
            LOCK.lock();
            return totalCount;
        } finally {
            LOCK.unlock();
        }
    }

    public Long getTotalRevenue() {
        try {
            LOCK.lock();
            return totalRevenue;
        } finally {
            LOCK.unlock();
        }
    }

    public void reset() {
        try {
            LOCK.lock();

            totalCount = 0L;
            totalRevenue = 0L;
        } finally {
            LOCK.unlock();
        }
    }
}
