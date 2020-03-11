package com.qgyyzs.globalcosmetics.eventbus;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class AnyEventBid {
    private final String searchString;

    public AnyEventBid(String searchString) {
        this.searchString=searchString;
    }

    public String getSearchString() {
        return searchString;
    }
}
