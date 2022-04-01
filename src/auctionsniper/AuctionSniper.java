package auctionsniper;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionSniper implements AuctionEventListener{

    private boolean isWinning = false;

    private SniperListener sniperListener;
    private Auction auction;
    private String itemId;

    private SniperSnapshot snapshot;

    public AuctionSniper(String itemId, Auction auction){
        this.itemId = itemId;
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public String getItemId(){
        return this.itemId;
    }

    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    private void notifyChange(){
        sniperListener.sniperStateChanged(snapshot);
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource){
            case FromSniper:
                this.snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                int bid = price + increment;
                this.auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }

        System.out.println(String.format("AuctionSniper.currentPrice %s %s %s", price, increment, priceSource));
        System.out.println("AuctionSniper.currentPrice " + snapshot);

        notifyChange();

    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener sniperListener) {
        this.sniperListener = sniperListener;
    }
}
