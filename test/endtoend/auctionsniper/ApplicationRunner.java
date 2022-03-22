package auctionsniper;

import java.io.IOException;

import static auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import static auctionsniper.Main.MainWindow.APPLICATION_TITLE;
import static auctionsniper.SniperState.JOINING;
import static auctionsniper.SniperTableModel.textFor;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer... auctions) {
        startSniper(auctions);
        for(FakeAuctionServer auction: auctions){

            final String itemId = auction.getItemId();
            driver.startBiddingFor(itemId);
            driver.showsSniperStatus(itemId, 0, 0, textFor(JOINING));
        }
    }

    private void startSniper(final FakeAuctionServer... auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(arguements(auctions));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(APPLICATION_TITLE);
        driver.hasColumnTitles();
    }

    protected static String[] arguements(FakeAuctionServer... auctions){
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for(int i = 0 ; i < auctions.length;i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }

//    public void showsSniperHasLostAuction() {
//        driver.showsSniperStatus(itemId, 0, 0, Main.MainWindow.STATUS_LOST);
//    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
                Main.MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(
                auction.getItemId(), winningBid,winningBid,
                Main.MainWindow.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(
                auction.getItemId(), lastPrice, lastPrice,
                Main.MainWindow.STATUS_WON);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

}
