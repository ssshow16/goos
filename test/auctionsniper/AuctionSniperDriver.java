package auctionsniper;

import auctionsniper.ui.MainWindow;
import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static org.hamcrest.Matchers.equalTo;

/**
 * Created by a1000107 on 2022/03/04.
 */
public class AuctionSniperDriver extends JFrameDriver {

    public AuctionSniperDriver(int timeoutMililis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                new AWTEventQueueProber(timeoutMililis, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JLabelDriver(
                this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(statusText)
        );
    }
}
