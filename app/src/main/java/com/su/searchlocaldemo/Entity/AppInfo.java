package com.su.searchlocaldemo.Entity;

import android.content.Intent;
import android.graphics.drawable.Drawable;

/**
 * Created by chinaso on 2016/6/23.
 * APP信息
 */
public class AppInfo extends SearchUtil {
    public String pkgName;
    public String appName;
    public Drawable appIcon;
    public Intent appIntent;

    public AppInfo(String name, String number, String sortKey, String pagName, Drawable appIcon, Intent appIntent) {
        super(name, number, sortKey);
        this.appName = name;
        this.pkgName = pagName;
        this.appIcon = appIcon;
        this.appIntent = appIntent;
    }

    @Override
    public int setEntity() {
        return 1;
    }


}
