package com.su.searchlocaldemo.CharactorUtil;

import com.su.searchlocaldemo.Entity.SearchUtil;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 * 按照拼音比较，实现排序
 *
 */
public class PinyinComparator implements Comparator<SearchUtil> {

	public int compare(SearchUtil o1, SearchUtil o2) {
		if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
			return -1;
		} else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
			return 1;
		} else {
			return o1.sortLetters.compareTo(o2.sortLetters);
		}
	}

}
