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
        return Column.at(columnIndex).valueIn(snapshot);
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
