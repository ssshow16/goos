package auctionsniper;

import java.io.IOException;

import static auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(
                            XMPP_HOSTNAME,
                            SNIPER_ID,
                            SNIPER_PASSWORD,
                            auctions.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(Main.MainWindow.STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(Main.MainWindow.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(Main.MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning() {
        driver.showsSniperStatus(Main.MainWindow.STATUS_WINNING);
    }

    public void showsSniHasWonAuction() {
        driver.showsSniperStatus(Main.MainWindow.STATUS_WON);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

}
