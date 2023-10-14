package course.concurrency.m2_async.cf.min_price;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceAggregator {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final int TIMEOUT_MILLISECONDS = 2900;

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);
    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        List<CompletableFuture<Double>> cfs = shopIds.stream()
                .map(shopId ->
                        CompletableFuture.supplyAsync(() ->
                                        priceRetriever.getPrice(itemId, shopId), EXECUTOR_SERVICE)
                        .completeOnTimeout(Double.NaN, TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
                        .exceptionally(e -> Double.NaN))
                .collect(Collectors.toList());

        return cfs.stream()
                .map(CompletableFuture::join)
                .min(Double::compareTo)
                .orElse(Double.NaN);
    }
}
