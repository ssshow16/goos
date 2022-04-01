package auctionsniper;

import java.util.EventListener;

/**
 * Created by a1000107 on 2022/04/01.
 */
public interface PortfolioListener extends EventListener{
    void sniperAdded(AuctionSniper sniper);
}
