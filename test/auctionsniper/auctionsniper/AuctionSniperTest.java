package auctionsniper;

import auctionsniper.AuctionEventListener.PriceSource;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import static auctionsniper.AuctionEventListener.PriceSource.*;

@RunWith(JMock.class)
public class AuctionSniperTest {

    private final Mockery context = new Mockery();
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final Auction auction = context.mock(Auction.class);


    private final String ITEM_ID = "1234";
    private final AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);
    private final States sniperState = context.states("sniper");


    @Test
    public void reportLostWhenAuctionCloses(){
        context.checking(new Expectations() {{
            one(sniperListener).sniperLost();
        }});

        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;

        context.checking(new Expectations() {{
            one(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperBidding(
                    new SniperState(ITEM_ID, price, bid)
            );
        }});

        sniper.currentPrice(price, increment, FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
          atLeast(1).of(sniperListener).sniperWinning();
        }});

        sniper.currentPrice(135, 45, FromSniper);
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {

        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperLost();
        }});

        sniper.auctionClosed();
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {

        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperBidding(with(any(SniperState.class)));
                then(sniperState.is("bidding"));        //
            atLeast(1).of(sniperListener).sniperLost();
                when(sniperState.is("bidding"));        //입찰중이어야함.
        }});

        //allow는 호출 할 수 있지만, 반드시 호출할 필요는 없음.
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperWinning(); then(sniperState.is("winning"));
            atLeast(1).of(sniperListener).sniperWon(); when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 45, PriceSource.FromSniper);
        sniper.auctionClosed();
    }
}