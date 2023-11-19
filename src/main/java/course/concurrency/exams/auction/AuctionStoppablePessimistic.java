package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;
    private Bid latestBid;
    private boolean stopped;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
    }

    public boolean propose(Bid bid) {
        if (stopped){
            return false;
        }
        synchronized (this) {
            if (latestBid.getPrice() >= bid.getPrice() || stopped) {
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

    public synchronized Bid stopAuction() {
        this.stopped = true;
        return latestBid;
    }
}
