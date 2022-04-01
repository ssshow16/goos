package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static auctionsniper.Main.AUCTION_RESOURCE;

/**
 * Created by a1000107 on 2022/04/01.
 */
public class XMPPAuctionHouse implements AuctionHouse{

    private XMPPConnection connection;

    private XMPPAuctionHouse(XMPPConnection connection){
        this.connection = connection;
    }

    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        System.out.println(String.format("Main.connection > . Connect and login with %s , %s, %s ", username, password, AUCTION_RESOURCE));

        return new XMPPAuctionHouse(connection);
    }

    public void disconnect() {
        connection.disconnect();
    }
}
