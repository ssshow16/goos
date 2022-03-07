package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by a1000107 on 2022/03/07.
 */
public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;

    private final AuctionMessageTranslator translator =
            new AuctionMessageTranslator();
    //jmock
    private final Mockery context = new Mockery();
    private final AuctionEventListener listener =  context.mock(AuctionEventListener.class);

    @Test
    public void notifiesAuctionClosedWhenCloseMessageReceived() {
        context.checking(new Expectations() {{
            oneOf(listener).auctionClosed(); //listener.auctionClosed()이 호출되었는지를 확인한다
        }});

        Message message = new Message();
        message.setBody("SOLVersion: 1.1; Event: CLOSE;");

        translator.processMessage(UNUSED_CHAT, message);
    }
}