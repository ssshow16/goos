package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.MainWindow.*;

public class SniperTableModel extends AbstractTableModel{

    private static String[] STATUS_TEXT = {
           "Joining","Bidding","Winning","Lost","Won"
    };

    private SniperSnapshot snapshot = new SniperSnapshot("",0,0, SniperState.JOINING);

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)){
            case ITEM_IDENTIFIER:
                return snapshot.itemId;
            case LAST_PRICE:
                return snapshot.lastPrice;
            case LAST_BID:
                return snapshot.lastBid;
            case SNIPER_STATUS:
                return textFor(snapshot.state);
            default:
                throw new IllegalStateException("No column at " + columnIndex);
        }
    }

    public void sniperStatusChanged(SniperSnapshot snapshot){
        System.out.println("SniperTableModel.sniperStatusChanged " + snapshot);
        this.snapshot = snapshot;
        fireTableRowsUpdated(0,0);
    }

    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }
}
