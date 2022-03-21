package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.MainWindow.*;

public class SniperTableModel extends AbstractTableModel{

    private static String[] STATUS_TEXT = {
            Main.MainWindow.STATUS_JOINING,
            Main.MainWindow.STATUS_BIDDING,
            STATUS_WINNING,
            STATUS_LOST,
            STATUS_WON
    };

    private String state = STATUS_JOINING;
    private SniperSnapshot sniperState = new SniperSnapshot("",0,0, SniperState.JOINING);

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)){
            case ITEM_IDENTIFIER:
                return sniperState.itemId;
            case LAST_PRICE:
                return sniperState.lastPrice;
            case LAST_BID:
                return sniperState.lastBid;
            case SNIPER_STATUS:
                return state;
            default:
                throw new IllegalStateException("No column at " + columnIndex);
        }
    }

    public void sniperStatusChanged(SniperSnapshot sniperState){
        System.out.println("SniperTableModel.sniperStatusChanged " + sniperState);
        this.sniperState = sniperState;
        this.state = STATUS_TEXT[sniperState.state.ordinal()];
        fireTableRowsUpdated(0,0);
    }
}
