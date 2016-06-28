package com.su.searchlocaldemo.Entity;

/**
 * Created by chinaso on 2016/6/27.
 * 实体基础
 */
public class SearchUtil {
    public String name;
    public String number;
    public String sortKey;
    public String sortLetters; //显示数据拼音的首字母
    public SortToken sortToken = new SortToken();//全部拼音

    public int setEntity() {
        return 0;
    }


    public SearchUtil(String name, String number, String sortKey) {
        this.name = name;
        this.number = number;
        this.sortKey = sortKey;
    }

    public void SearchIndex(String sortLetters, SortToken sortToken) {
        this.sortLetters = sortLetters;
        this.sortToken = sortToken;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchUtil other = (SearchUtil) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (sortKey == null) {
            if (other.sortKey != null)
                return false;
        } else if (!sortKey.equals(other.sortKey))
            return false;
        return true;
    }
}
