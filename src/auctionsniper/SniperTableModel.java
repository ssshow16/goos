package auctionsniper;

import javax.swing.table.AbstractTableModel;

import static auctionsniper.Main.MainWindow.STATUS_JOINING;

public class SniperTableModel extends AbstractTableModel{

    private String statusText = STATUS_JOINING;

    public int getRowCount() {
        return 1;
    }

    public int getColumnCount() {
        return 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return statusText;
    }

    public void sniperStatusChanged(SniperState sniperState, String newStatusText){
        this.statusText = newStatusText;
        fireTableRowsUpdated(0,0);
    }
}
