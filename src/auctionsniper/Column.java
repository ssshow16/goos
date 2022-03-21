package auctionsniper;

/**
 * Created by a1000107 on 2022/03/21.
 */
public enum Column {
    ITEM_IDENTIFIER("Item"){
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.itemId;
        }
    },
    LAST_PRICE("Last Price"){
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastPrice;
        }
    },
    LAST_BID("Last Bid"){
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastBid;
        }
    },
    SNIPER_STATUS("State"){
        public Object valueIn(SniperSnapshot snapshot) {
            return SniperTableModel.textFor(snapshot.state);
        }
    };

    public final String name;
    private Column(String name){
        this.name = name;
    }

    public static Column at(int offset){
        return values()[offset];
    }

    abstract public Object valueIn(SniperSnapshot snapshot);
}
