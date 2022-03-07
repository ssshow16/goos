package auctionsniper;

import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        application.startBiddingIn(auction); // 메시지 전송 XMPP 및 UI Open
        auction.hasReceivedJoinRequestFromSniper(); // XMPP로 부터 메시지 수신여부를 확인한다
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }

    @After
    public void stopAuction() {
        auction.stop();
    }

    @After
    public void stopApplication() {
        application.stop();
    }
}
