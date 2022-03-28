package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionMessageTranslator;
import auctionsniper.util.Announcer;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static auctionsniper.Main.AUCTION_ID_FORMAT;
import static auctionsniper.Main.BID_COMMAND_FORMAT;
import static auctionsniper.Main.JOIN_COMMAND_FORMAT;
import static java.lang.String.format;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class XMPPAuction implements Auction {

   private final Announcer<AuctionEventListener> auctionEventListeners =
            Announcer.to(AuctionEventListener.class);

    private final Chat chat;

    public XMPPAuction(XMPPConnection connection, String itemId){
        chat = connection.getChatManager().createChat(
                auctionId(itemId,connection),
                new AuctionMessageTranslator(
                        connection.getUser(),
                        auctionEventListeners.announce()
                )
        );
    }

    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    public void join() {
        sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(String message) {
        try {
            System.out.println("XMPPAuction > " + message);
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    private String auctionId(String itemId, XMPPConnection connection){
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    public void addAuctionEventListener(AuctionEventListener auctionEventListener) {
        
    }
}
