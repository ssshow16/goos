package auctionsniper.xmpp;

import auctionsniper.Auction;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static auctionsniper.Main.BID_COMMAND_FORMAT;
import static auctionsniper.Main.JOIN_COMMAND_FORMAT;
import static java.lang.String.format;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class XMPPAuction implements Auction {

    private final Chat chat;

    public XMPPAuction(Chat chat){
        this.chat = chat;
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

}
