package auctionsniper;

public class SniperLauncher implements UserRequestListener {

    private AuctionHouse auctionHouse;
    private SniperCollector collector;

    public SniperLauncher(AuctionHouse auctionHouse, SniperCollector collector){ //SniperPortfolio. SniperCollector 구현채
        this.auctionHouse = auctionHouse;
        this.collector = collector;
    }

    public void joinAuction(String itemId) {
        //옥션에 참여할때 마다 Auction / Sniper 생성하여 연결,
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(itemId, auction);
        auction.addAuctionEventListener(sniper);
        collector.addSniper(sniper); //Collector에 저장관리한다.
        auction.join();
    }
}
