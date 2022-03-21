package auctionsniper;

/**
 * Created by a1000107 on 2022/03/21.
 */
public enum Column {
    ITEM_IDENTIFIER,
    LAST_PRICE,
    LAST_BID,
    SNIPER_STATUS;

    public static Column at(int offset){
        return values()[offset];
    }
}
