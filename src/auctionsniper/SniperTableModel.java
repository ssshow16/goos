package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.MainWindow.*;

public class SniperTableModel extends AbstractTableModel implements SniperListener{

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

    public void sniperStateChanged(SniperSnapshot snapshot){
        System.out.println("SniperTableModel.sniperStatusChanged " + snapshot);
        this.snapshot = snapshot;
        fireTableRowsUpdated(0,0);
    }


    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot joining) {
    }
}
