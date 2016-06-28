package com.su.searchlocaldemo;

import com.su.searchlocaldemo.Entity.AppInfo;
import com.su.searchlocaldemo.Entity.Contact;
import com.su.searchlocaldemo.Entity.SearchUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chinaso on 2016/6/28.
 * 模糊搜索，拼音匹配
 */
public class FuzzySearch {
    private ArrayList<Contact> mAllContactsList;
    private ArrayList<AppInfo> mAllAppList;

    public FuzzySearch(ArrayList mAllContactsList, ArrayList mAllAppList) {
        this.mAllAppList = mAllAppList;
        this.mAllContactsList = mAllContactsList;
    }

    public List<SearchUtil> search(String str) {
        List<SearchUtil> filterList = new ArrayList();// 过滤后的list

        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (Contact contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
            for (AppInfo appInfo : mAllAppList) {
                if (appInfo.name != null) {
                    if (appInfo.name.contains(str)) {
                        if (!filterList.contains(appInfo)) {
                            filterList.add(appInfo);
                        }
                    }
                }
            }
        } else {


            for (Contact contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {

                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "").contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
            for (AppInfo appInfo : mAllAppList) {
                if (appInfo.name != null) {

                    if (appInfo.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || appInfo.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "").contains(str.toLowerCase(Locale.CHINESE))
                            || appInfo.sortToken.simpleSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || appInfo.sortToken.wholeSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))) {
                        if (!filterList.contains(appInfo)) {
                            filterList.add(appInfo);
                        }
                    }
                }
            }
        }
        return filterList;
    }

}
