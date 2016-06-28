package com.su.searchlocaldemo.Entity;

/**
 * 联系人信息
 */
public class Contact extends SearchUtil {
    public String name;
    public String number;
    public String simpleNumber;
    public String sortKey;


    @Override
    public int setEntity() {
        return 2;
    }


    public Contact(String name, String number, String sortKey) {
        super(name, number, sortKey);
        this.name = name;
        this.number = number;
        this.sortKey = sortKey;
        if (number != null) {
            this.simpleNumber = number.replaceAll("\\-|\\s", "");
        }
    }


}
