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
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); // XMPP로 부터 메시지 수신여부를 확인한다

        auction.reportPrice(1000,98,"other bidder"); // 가격:1000, 증액 98, 'other bidder' 이름으로 메시지 전송
        application.hasShownSniperIsBidding(1000, 1098); // 최종가격과 입찰

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000,98, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(1098); //낙

        auction.announceClosed(); // 종료메시지 전송
        application.showsSniperHasWonAuction(1098); //최종가격
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
