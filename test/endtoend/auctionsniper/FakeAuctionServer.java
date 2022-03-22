package auctionsniper;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by a1000107 on 2022/03/04.
 */
public class FakeAuctionServer {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        currentChat = chat;
                        chat.addMessageListener(messageListener);
                    }
                });
    }

    public String getItemId(){
        return this.itemId;
    }

    public void hasReceivedJoinRequestFrom(String sniperId)
            throws InterruptedException {
        receivesAMessageMatching(sniperId, equalTo(Main.JOIN_COMMAND_FORMAT));
    }


    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException{
        assertThat(currentChat.getParticipant(), equalTo(sniperId));//서버로 부터 메시지를 대기해야하기에 식별자를 먼저 검사함.
        receivesAMessageMatching(sniperId, equalTo(format(Main.BID_COMMAND_FORMAT, bid)));
    }

    private void receivesAMessageMatching(String sniperId,
                                          Matcher<? super String> messageMatcher)
            throws InterruptedException {
        messageListener.receivesAMessage(messageMatcher);
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void announceClosed() throws XMPPException {
//        currentChat.sendMessage(new Message());
        currentChat.sendMessage("SOLVersion: 1.1; Event:CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException{
        currentChat.sendMessage(
                String.format("SOLVerion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder)
        );
    }


    public class SingleMessageListener implements MessageListener {
        private final ArrayBlockingQueue<Message> messages =
                new ArrayBlockingQueue<Message>(1);

        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        @SuppressWarnings("unchecked")
        public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
            final Message message = messages.poll(5, TimeUnit.SECONDS);
            assertThat(message, hasProperty("body", messageMatcher));
        }
    }
}
