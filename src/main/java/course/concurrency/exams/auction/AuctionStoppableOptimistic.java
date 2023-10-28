package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;
    private final AtomicReference<Bid> atomRefBid;
    private final AtomicStampedReference<Boolean> atomStampRefBoolean;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.atomRefBid = new AtomicReference<>(new Bid(0L, 0L, 0L));
        this.atomStampRefBoolean = new AtomicStampedReference<>(false, 0);
    }

    public boolean propose(Bid bid) {
        Bid bidLatest;
        do {
            bidLatest = atomRefBid.get();
            if (bidLatest.getPrice() >= bid.getPrice() || atomStampRefBoolean.getReference()) {
                return false;
            }
        } while (!atomRefBid.compareAndSet(bidLatest, bid));
        notifier.sendOutdatedMessage(bidLatest);
        return true;
    }

    public Bid getLatestBid() {
        return atomRefBid.get();
    }

    public Bid stopAuction() {
        boolean isStopped;
        int stamp;
        do {
            isStopped = atomStampRefBoolean.getReference();
            stamp = atomStampRefBoolean.getStamp();
        } while (!atomStampRefBoolean.compareAndSet(isStopped, true, stamp, ++stamp));
        return atomRefBid.get();
    }
}
