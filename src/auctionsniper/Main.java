package auctionsniper;

import auctionsniper.xmpp.XMPPAuction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static auctionsniper.Main.MainWindow.STATUS_JOINING;
import static com.sun.java.swing.ui.CommonUI.createLabel;

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

    private MainWindow ui;

    private Chat notToBeGCd;

    public Main() throws Exception{
        startUserInterface();

    }

    private void startUserInterface() throws Exception{
        SwingUtilities.invokeAndWait(
                new Runnable() {
                    public void run() {
                        ui = new MainWindow();
                    }
                }
        );
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();

        main.joinAuction(
                connection(
                        args[ARG_HOSTNAME],
                        args[ARG_USERNAME],
                        args[ARG_PASSWORD]
                ),
                args[ARG_ITEM_ID]
        );
    }

    private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException{


        disconnectWhenUICloses(connection);


        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                null
        );
        this.notToBeGCd = chat;

        Auction auction = new XMPPAuction(chat);

        chat.addMessageListener(new AuctionMessageTranslator(
                connection.getUser(),
                new AuctionSniper(auction, new SniperStateDisplayer())));
        auction.join();
    }

    private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException{
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, XMPPConnection connection){
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private void disconnectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                connection.disconnect();
            }
        });
    }

    public class SniperStateDisplayer implements SniperListener{
        public void sniperLost() {
            showStatus(MainWindow.STATUS_LOST);
        }

        public void sniperBidding(){
            showStatus(MainWindow.STATUS_BIDDING);
        }

        public void sniperWinning(){
            showStatus(MainWindow.STATUS_WINNING);
        }

        public void sniperWon() {
            showStatus(MainWindow.STATUS_WON);
        }

        private void showStatus(final String status){

            System.out.println("SniperStateDisplayer >> " + status);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ui.showStatusText(status);
                }
            });
        }

    }

    public class MainWindow extends JFrame{

        private final SniperTableModel snipers = new SniperTableModel();

        public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
        public static final String APPLICATION_TITLE = "Auction Sniper";

        public static final String STATUS_JOINING = "Joining";
        public static final String STATUS_LOST = "Lost";
        public static final String STATUS_BIDDING = "Bidding";
        public static final String STATUS_WINNING = "Winning";
        public static final String STATUS_WON = "Won";

        private static final String SNIPERS_TABLE_NAME = "Snipers Table";

        public MainWindow(){
            super(APPLICATION_TITLE);
            setName(MAIN_WINDOW_NAME);
            fillContentPane(makeSnipersTable());
            pack();
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private JTable makeSnipersTable() {
            final JTable snipersTable = new JTable(snipers);
            snipersTable.setName(SNIPERS_TABLE_NAME);
            return snipersTable;
        }

        private void fillContentPane(JTable snipersTable) {
            final Container contentPane = getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
        }

        public void showStatusText(String statusText){

            System.out.println("MainWindow >" + statusText);
            snipers.setStatusText(statusText);
        }
    }
}
