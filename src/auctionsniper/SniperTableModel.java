package auctionsniper;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.List;

import static auctionsniper.Main.MainWindow.*;

public class SniperTableModel extends AbstractTableModel implements SniperListener{

    private static String[] STATUS_TEXT = {
           "Joining","Bidding","Winning","Lost","Won"
    };

    private List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();

    public int getRowCount() {
        return snapshots.size();
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    public void sniperStateChanged(SniperSnapshot snapshot){
        System.out.println("SniperTableModel.sniperStatusChanged " + snapshot);
        this.snapshots.add(snapshot);
        fireTableRowsUpdated(0,0);
    }


    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot snapshot) {
        int row = snapshots.size();
        this.snapshots.add(snapshot);
        fireTableRowsInserted(row, row);
    }
}
