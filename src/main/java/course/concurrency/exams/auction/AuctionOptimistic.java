package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;
    private final AtomicReference<Bid> refBid = new AtomicReference<>(new Bid(0L, 0L, 0L));

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    public boolean propose(Bid bid) {
        Bid bidLatest;
        do {
            bidLatest = refBid.get();
            if (bidLatest.getPrice() >= bid.getPrice()){
                return false;
            }
        } while (!refBid.compareAndSet(bidLatest, bid));
        notifier.sendOutdatedMessage(bidLatest);
        return true;
    }

    public Bid getLatestBid() {
        return refBid.get();
    }
}
