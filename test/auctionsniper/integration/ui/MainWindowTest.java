package integration.ui;

import auctionsniper.AuctionSniperDriver;
import auctionsniper.SniperTableModel;
import auctionsniper.UserRequestListener;
import auctionsniper.MainWindow;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class MainWindowTest {

    private final SniperTableModel tableModel = new SniperTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Before
    public void setKeyboard(){
//        System.setProperty("com.objogate.wl.keyboard", "Mac-GB");
    }

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        System.setProperty("com.objogate.wl.keyboard", "Mac-GB");

        final ValueMatcherProbe<String> buttonProbe
                 = new ValueMatcherProbe<String>(
                equalTo("itemid"), "join button"
        ); // '-' 처리 못하여 이름을 변경함.

        mainWindow.addUserRequestListener(
                new UserRequestListener(){
                    public void joinAuction(String itemId){
                        System.out.println("test joinAuction " + itemId);
                        buttonProbe.setReceivedValue(itemId);
                    }
                }
        );
        driver.startBiddingFor("itemid");
        driver.check(buttonProbe);
    }
}