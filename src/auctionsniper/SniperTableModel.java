package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.MainWindow.STATUS_JOINING;

public class SniperTableModel extends AbstractTableModel{

    private final SniperState STARTINT_UP = new SniperState("", 0,0);
    private String statusText = STATUS_JOINING;
    private SniperState sniperState = STARTINT_UP;

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
                return statusText;
            default:
                throw new IllegalStateException("No column at " + columnIndex);
        }
    }

    public void sniperStatusChanged(SniperState sniperState, String newStatusText){
        this.sniperState = sniperState;
        this.statusText = newStatusText;
        fireTableRowsUpdated(0,0);
    }
}
