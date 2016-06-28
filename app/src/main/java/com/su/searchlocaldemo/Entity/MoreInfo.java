package com.su.searchlocaldemo.Entity;

/**
 * Created by chinaso on 2016/6/28.
 * 作为补充布局，分隔
 */
public class MoreInfo extends SearchUtil {

    public MoreInfo(String name, String number, String sortKey) {
        super(name, number, sortKey);
    }

    @Override
    public int setEntity() {
        return 3;
    }
}
