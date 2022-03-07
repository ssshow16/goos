package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionMessageTranslator implements MessageListener{

    private final AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener){
        this.listener = listener;
    }

    public void processMessage(Chat unusedChat, Message message) {
        listener.auctionClosed();
    }
}
