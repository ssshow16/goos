package auctionsniper;

import auctionsniper.util.Announcer;
import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.lang.String.format;

/**
 * Created by a1000107 on 2022/03/04.
 */
public class Main {
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    private final SniperTableModel snipers = new SniperTableModel();

    private MainWindow ui;

    private java.util.List<Auction> notToBeGCd = new ArrayList<Auction>();

    public Main() throws Exception{
        startUserInterface();

    }

    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        ui = new MainWindow(snipers);
                    }
                }
        );
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();

        XMPPConnection connection =
                connection( args[ARG_HOSTNAME],
                        args[ARG_USERNAME],
                        args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(connection);
        main.addUserRequestListenerFor(connection);
    }

    private void addUserRequestListenerFor(final XMPPConnection connection) {
        ui.addUserRequestListener(new UserRequestListener() {
            public void joinAuction(String itemId) {

                snipers.addSniper(SniperSnapshot.joining(itemId));

                Auction auction = new XMPPAuction(connection, itemId);
                notToBeGCd.add(auction);

                auction.addAuctionEventListener(
                        new AuctionSniper(itemId, auction,
                                new SwingThreadSniperListener(snipers)));
                auction.join();
            }
        });
    }

    private void safelyAddItemToModel(final String itemId) throws Exception{
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        snipers.addSniper(SniperSnapshot.joining(itemId));
                    }
                }
        );
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException{
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);

        System.out.println(String.format("Main.connection > . Connect and login with %s , %s, %s ", username, password, AUCTION_RESOURCE));

        return connection;
    }

//    private static String auctionId(String itemId, XMPPConnection connection){
//        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
//    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    public class SwingThreadSniperListener implements SniperListener{

        SniperTableModel snipers;

        public SwingThreadSniperListener(SniperTableModel snipers){
            this.snipers = snipers;
        }
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            snipers.sniperStateChanged(snapshot);
        }
    }

}
