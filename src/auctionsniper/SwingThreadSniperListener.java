package auctionsniper;

public class SwingThreadSniperListener implements SniperListener{

    SniperTableModel snipers;

    public SwingThreadSniperListener(SniperTableModel snipers){
        this.snipers = snipers;
    }
    public void sniperStateChanged(final SniperSnapshot snapshot) {
        snipers.sniperStateChanged(snapshot);
    }
}
