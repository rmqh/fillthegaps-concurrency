package course.concurrency.m3_shared;

public class RefundService {

    private ShopStatistics stat;

    public synchronized void processRefund(long cartId) {
        Long count = getCountFromCart(cartId);
        Long price = getPriceFromCart(cartId);
        // …
        synchronized (stat) {
            Long currentCount = stat.getTotalCount();
            Long currentRevenue = stat.getTotalRevenue();
            stat.reset();
            stat.addData(currentCount - count, price);
        }
    }

    private Long getCountFromCart(long cartId) {
        return null;
    }

    private Long getPriceFromCart(long cartId) {
        return null;
    }
}
