package auctionsniper;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionSniper implements AuctionEventListener{

    private SniperListener sniperListener;

    public AuctionSniper(SniperListener sniperListener){
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    public void currentPrice(int price, int increment) {

    }
}
