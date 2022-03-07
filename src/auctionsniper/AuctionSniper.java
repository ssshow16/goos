package auctionsniper;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionSniper implements AuctionEventListener{

    private SniperListener sniperListener;
    private Auction auction;

    public AuctionSniper(Auction auction, SniperListener sniperListener){
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {

        switch (priceSource){
            case FromSniper:
                sniperListener.sniperWinning();
                break;
            case FromOtherBidder:
                this.auction.bid(price + increment);
                sniperListener.sniperBidding();
                break;
        }

    }
}
