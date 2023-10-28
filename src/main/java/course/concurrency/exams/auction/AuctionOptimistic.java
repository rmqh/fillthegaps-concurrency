package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;

public class AuctionOptimistic implements Auction {

    private final Notifier notifier;
    private final AtomicReference<Bid> atomRefBid;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.atomRefBid = new AtomicReference<>(new Bid(0L, 0L, 0L));
    }

    public boolean propose(Bid bid) {
        Bid bidLatest;
        do {
            bidLatest = atomRefBid.get();
            if (bidLatest.getPrice() >= bid.getPrice()) {
                return false;
            }
        } while (!atomRefBid.compareAndSet(bidLatest, bid));
        notifier.sendOutdatedMessage(bidLatest);
        return true;
    }

    public Bid getLatestBid() {
        return atomRefBid.get();
    }
}
