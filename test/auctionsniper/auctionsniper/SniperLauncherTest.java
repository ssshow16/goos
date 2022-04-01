package auctionsniper;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;


public class SniperLauncherTest {

    private final Mockery context = new Mockery();
    private final States auctionState = context.states("auction state").startsAs("not joined");

    private AuctionHouse auctionHouse = context.mock(AuctionHouse.class);
    private Auction auction = context.mock(Auction.class);
    private SniperCollector sniperCollector = context.mock(SniperCollector.class);
    private SniperLauncher launcher = new SniperLauncher(auctionHouse, sniperCollector);

    @Test public void
    addsNewSnipeToCollectorAndThenJoinsAuction(){
        final String itemId = "item 123";
        context.checking(new Expectations(){
            {
                allowing(auctionHouse).auctionFor(itemId); will(returnValue(auction));

                oneOf(auction).addAuctionEventListener(with(sniperForItem(itemId)));
                    when(auctionState.is("not joined"));
                oneOf(sniperCollector).addSniper(with(sniperForItem(itemId)));
                    when(auctionState.is("not joined"));

                one(auction).join(); then(auctionState.is("joined"));
            }
        });

        launcher.joinAuction(itemId);
    }

    protected Matcher<AuctionSniper> sniperForItem(String item) {
        return new FeatureMatcher<AuctionSniper, String>(equalTo(item), "sniper with item id", "item") {
            @Override protected String featureValueOf(AuctionSniper actual) {
                return actual.getItemId();
            }
        };
    }
}