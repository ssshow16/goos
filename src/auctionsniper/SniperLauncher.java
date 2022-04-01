package auctionsniper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a1000107 on 2022/04/01.
 */
public class SniperLauncher implements UserRequestListener {

    private SniperTableModel snipers;
    private AuctionHouse auctionHouse;
    private List<Auction> notToBeGCd = new ArrayList<Auction>();

    public SniperLauncher(AuctionHouse auctionHouse, SniperTableModel snipers){
        this.auctionHouse = auctionHouse;
        this.snipers = snipers;
    }

    public void joinAuction(String itemId) {

        snipers.addSniper(SniperSnapshot.joining(itemId));

        Auction auction = auctionHouse.auctionFor(itemId);
        notToBeGCd.add(auction);

        auction.addAuctionEventListener(
                new AuctionSniper(itemId, auction,
                        new SwingThreadSniperListener(snipers)));
        auction.join();
    }
}
