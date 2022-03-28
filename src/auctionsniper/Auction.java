package auctionsniper;

/**
 * Created by a1000107 on 2022/03/07.
 */
public interface Auction {
    void bid(int amount);
    void join();
    void addAuctionEventListener(AuctionEventListener auctionEventListener);
}
