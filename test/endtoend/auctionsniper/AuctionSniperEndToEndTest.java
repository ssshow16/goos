package auctionsniper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");

    private final ApplicationRunner application = new ApplicationRunner();

    @Before
    public void setKeyboard(){
        System.out.println("setKeyboard");
        System.setProperty("com.objogate.wl.keyboard", "Mac-GB");
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {

        auction.startSellingItem();

        application.startBiddingIn(auction); // 메시지 전송 XMPP 및 UI Open
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); // XMPP로 부터 메시지 수신여부를 확인한다

        auction.reportPrice(1000,98,"other bidder"); // 가격:1000, 증액 98, 'other bidder' 이름으로 메시지 전송
        application.hasShownSniperIsBidding(auction, 1000, 1098); // 최종가격과 입찰

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098,97, ApplicationRunner.SNIPER_XMPP_ID);
        application.hasShownSniperIsWinning(auction, 1098); //

        auction.announceClosed(); // 종료메시지 전송
        application.showsSniperHasWonAuction(auction, 1098); //최종가격
    }

    @Test
    public void sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auction2.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_XMPP_ID);

        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);

        auction.announceClosed();
        auction2.announceClosed();

        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
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
