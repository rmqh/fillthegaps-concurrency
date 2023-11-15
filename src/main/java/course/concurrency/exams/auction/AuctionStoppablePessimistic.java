package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private final Notifier notifier;
    private Bid latestBid;
    private boolean isStopped;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
        this.latestBid = new Bid(0L, 0L, 0L);
    }

    public boolean propose(Bid bid) {
        if (isStopped){
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

    public synchronized Bid stopAuction() {
        this.isStopped = true;
        return latestBid;
    }
}
