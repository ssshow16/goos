package xmpp;

import auctionsniper.ApplicationRunner;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.FakeAuctionServer;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class XMPPAuctionTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPConnection connection;


    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        Auction auction = new XMPPAuction(connection, auctionServer);
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            public void currentPrice(int price, int increment, PriceSource priceSource) {

            }
        };
    }

}