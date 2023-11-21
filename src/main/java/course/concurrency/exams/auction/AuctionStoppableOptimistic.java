package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private final Notifier notifier;
    private final AtomicMarkableReference<Bid> atomMarkRefBid;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
        this.atomMarkRefBid = new AtomicMarkableReference<>(new Bid(0L, 0L, 0L), false);
    }

    public boolean propose(Bid bid) {
        Bid bidLatest;
        do {
            bidLatest = atomMarkRefBid.getReference();
            if (bidLatest.getPrice() >= bid.getPrice() || atomMarkRefBid.isMarked()) {
                return false;
            }
        } while (!atomMarkRefBid.compareAndSet(bidLatest, bid, false, false));
        notifier.sendOutdatedMessage(bid);
        return true;
    }

    public Bid getLatestBid() {
        return atomMarkRefBid.getReference();
    }

    public Bid stopAuction() {
        boolean isStopped;
        do {
            isStopped = atomMarkRefBid.attemptMark(atomMarkRefBid.getReference(), true);
        } while (!isStopped);
        return atomMarkRefBid.getReference();
    }
}
