package course.concurrency.exams.auction;

public class AuctionPessimistic implements Auction {

    private final Notifier notifier;
    private volatile Bid latestBid;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
    }

    public boolean propose(Bid bid) {
        if (latestBid.getPrice() >= bid.getPrice()) {
            return false;
        }
        synchronized (this) {
            if (latestBid.getPrice() >= bid.getPrice()) {
                return false;
            }
            latestBid = bid;
        }
        notifier.sendOutdatedMessage(bid);
        return true;
    }

    public Bid getLatestBid() {
        return latestBid;
    }
}
