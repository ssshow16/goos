package auctionsniper;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionSniper implements AuctionEventListener{

    private boolean isWinning = false;

    private SniperListener sniperListener;
    private Auction auction;

    public AuctionSniper(Auction auction, SniperListener sniperListener){
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        if(isWinning){
            sniperListener.sniperWon();
        }else{
            sniperListener.sniperLost();
        }
    }

    public void currentPrice(int price, int increment, PriceSource priceSource) {

        isWinning = priceSource == PriceSource.FromSniper;

        if(isWinning){
            sniperListener.sniperWinning();
        }else{
            this.auction.bid(price + increment);
            sniperListener.sniperBidding();
        }
    }
}
