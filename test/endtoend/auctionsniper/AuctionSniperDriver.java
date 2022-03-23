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
    @SuppressWarnings("unchecked")
    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MainWindow.MAIN_WINDOW_NAME),
                        showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
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
        JTextFieldDriver itemIdField = itemIdField();
        itemIdField.component().component().setText(itemId);

        JButtonDriver jButtonDriver = bidButton();
        jButtonDriver.click();//호출해줘야 아래 콤포넌트 조회시 조회됨. 안그러면 못 참음.
        jButtonDriver.component().component().doClick();

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
        JButtonDriver jButtonDriver = new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
        return jButtonDriver;
    }

}
