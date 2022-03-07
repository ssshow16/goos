package auctionsniper;

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

/**
 * Created by a1000107 on 2022/03/04.
 */
public class Main implements SniperListener{
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT =
            ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;


    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
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
        Auction auction = new Auction() {
            public void bid(int amount) {
                try {
                    chat.sendMessage(String.format(BID_COMMAND_FORMAT, amount));
                }catch (XMPPException e){
                    e.printStackTrace();
                }
            }
        };
        chat.addMessageListener(new AuctionMessageTranslator(new AuctionSniper(auction, this)));
        chat.sendMessage(JOIN_COMMAND_FORMAT);
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

//    public void auctionClosed() {
//
//    }
//
//    public void currentPrice(int price, int increment) {
//
//    }

    public void sniperLost() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }

    public void sniperBidding() {

    }

    public class MainWindow extends JFrame{

        public static final String STATUS_JOINING = "Joining";
        public static final String STATUS_LOST = "Lost";
        public static final String STATUS_BIDDING = "Bidding";

        public static final String SNIPER_STATUS_NAME = "sniper status";

        private final JLabel sniperStatus = createLabel(STATUS_JOINING);


        public MainWindow(){
            super("Auction Sniper");
            setName(MAIN_WINDOW_NAME);
            add(sniperStatus);
            pack();
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setVisible(true);
        }

        private JLabel createLabel(String initialText){
            JLabel result = new JLabel(initialText);
            result.setName(SNIPER_STATUS_NAME);
            result.setBorder(new LineBorder(Color.BLACK));
            return result;
        }

        public void showStatus(String status){
            sniperStatus.setText(status);
        }
    }
}
