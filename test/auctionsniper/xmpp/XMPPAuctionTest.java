package xmpp;

import auctionsniper.ApplicationRunner;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.FakeAuctionServer;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static auctionsniper.FakeAuctionServer.*;
import static auctionsniper.Main.AUCTION_ID_FORMAT;
import static org.junit.Assert.*;


public class XMPPAuctionTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPConnection connection;


    @Before
    public void openConnection() throws XMPPException {
        connection = new XMPPConnection(XMPP_HOSTNAME);
        connection.connect();
        connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, AUCTION_RESOURCE);
//        System.out.println();
        System.out.println(String.format("Test.connect > %s, %s, %s",
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD,
                AUCTION_RESOURCE ));
    }
    @Before public void startAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }

    @After public void stopAuction() {
        auctionServer.stop();
    }

    @After
    public void closeConnection() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = new XMPPAuction(connection,  auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();

        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(20, TimeUnit.SECONDS));
    }

    private String auctionId(String itemId, XMPPConnection connection){
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() {
                System.out.println("auctionClosed");
                auctionWasClosed.countDown();
            }

            public void currentPrice(int price, int increment, PriceSource priceSource) {

            }
        };
    }

}