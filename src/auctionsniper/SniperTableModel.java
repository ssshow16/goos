package auctionsniper;

import com.objogate.exception.Defect;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.List;

public class SniperTableModel extends AbstractTableModel implements SniperListener, PortfolioListener{

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
        int row = rowMatching(snapshot);
        snapshots.set(row, snapshot);
        fireTableRowsUpdated(row, row);
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public static String textFor(SniperState state){
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniperSnapshot(SniperSnapshot snapshot) {
        int row = snapshots.size();
        this.snapshots.add(snapshot);
        fireTableRowsInserted(row, row);
    }

    private int rowMatching(SniperSnapshot newSnapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (newSnapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + newSnapshot);
    }

    //PortfolioListener
    public void sniperAdded(AuctionSniper sniper) {
        sniper.addSniperListener(new SwingThreadSniperListener(this));
        addSniperSnapshot(sniper.getSnapshot());
    }
}
