package auctionsniper;

import java.util.EventListener;

/**
 * Created by a1000107 on 2022/03/07.
 */
public interface AuctionEventListener extends EventListener {
    void auctionClosed();
    void currentPrice(int price, int increment);
}
