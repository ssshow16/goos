package auctionsniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static java.lang.String.valueOf;


public class AuctionSniperDriver extends JFrameDriver {

    public AuctionSniperDriver(int timeoutMililis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                new AWTEventQueueProber(timeoutMililis, 100));
    }

    public void showsSniperStatus(
            String itemId,
            int lastPrice,
            int lastBid,
            String statusText) {


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
    }

    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(
                withLabelText("Item"),
                withLabelText("Last Price"),
                withLabelText("Last Bid"),
                withLabelText("State")));
    }

    @SuppressWarnings("unchecked")
    public void startBiddingFor(String itemId) {
        System.out.println("startBiddingFor >>" + itemId);
        itemIdField().replaceAllText(itemId); //입력 및 버튼 클릭 시뮬레이션이 동작안함
        bidButton().click();
    }

    @SuppressWarnings("unchecked")
    private JTextFieldDriver itemIdField() {
        JTextFieldDriver newItemId =
                new JTextFieldDriver(this, JTextField.class, named(MainWindow.NEW_ITEM_ID_NAME));
        newItemId.focusWithMouse();
        return newItemId;
    }

    @SuppressWarnings("unchecked")
    private JButtonDriver bidButton() {
        return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }

}
