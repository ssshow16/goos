package auctionsniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.equalTo;


/**
 * Created by a1000107 on 2022/03/04.
 */
public class AuctionSniperDriver extends JFrameDriver{

    public AuctionSniperDriver(int timeoutMililis){
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(Main.MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                    new AWTEventQueueProber(timeoutMililis, 100));
    }

    public void showsSniperStatus(
            String itemId,
            int lastPrice,
            int lastBid,
            String statusText){


        System.out.println("AuctionSniperDriver >" + itemId + "," + lastPrice + "," + lastBid + "," + statusText);

        new JTableDriver(this)
                .hasRow(
                        matching(
                                withLabelText(itemId),
                                withLabelText(valueOf(lastPrice)),
                                withLabelText(valueOf(lastBid)),
                                withLabelText(statusText)
                        )
                );
//        new JTableDriver(this).hasCell(
//                withLabelText(
//                        equalTo(statusText)
//                )
//        );
    }
}
