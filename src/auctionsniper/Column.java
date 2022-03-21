package auctionsniper;

/**
 * Created by a1000107 on 2022/03/21.
 */
public enum Column {
    ITEM_IDENTIFIER{
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.itemId;
        }
    },
    LAST_PRICE{
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastPrice;
        }
    },
    LAST_BID{
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastBid;
        }
    },
    SNIPER_STATUS{
        public Object valueIn(SniperSnapshot snapshot) {
            return SniperTableModel.textFor(snapshot.state);
        }
    };

    public static Column at(int offset){
        return values()[offset];
    }

    abstract public Object valueIn(SniperSnapshot snapshot);
}
