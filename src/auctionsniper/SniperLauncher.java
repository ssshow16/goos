package auctionsniper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000107 on 2022/04/01.
 */
public class SniperLauncher implements UserRequestListener {

    private AuctionHouse auctionHouse;
    private SniperCollector collector;


//    private SniperTableModel snipers;

//    public SniperLauncher(AuctionHouse auctionHouse, SniperTableModel snipers){
    public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector){
        this.auctionHouse = auctionHouse;
//        this.snipers = snipers;
        this.collector = collector;
    }

    public void joinAuction(String itemId) {
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(itemId, auction);
        auction.addAuctionEventListener(sniper);
        collector.addSniper(sniper);
        auction.join();
    }
}
