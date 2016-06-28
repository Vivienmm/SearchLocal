package com.su.searchlocaldemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.su.searchlocaldemo.CharactorUtil.CharacterParser;
import com.su.searchlocaldemo.CharactorUtil.GetLetter;
import com.su.searchlocaldemo.CharactorUtil.ParseSortKey;
import com.su.searchlocaldemo.Entity.AppInfo;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by chinaso on 2016/6/28.
 * 获取应用信息
 */
public class LoadAppInfo {
    private Context mContext;
    private CharacterParser mCharacterParser;
    public LoadAppInfo(Context context, CharacterParser characterParser){
        mContext=context;
        mCharacterParser=characterParser;
    }

    public ArrayList<AppInfo> getAppInfo (){

        PackageManager pm = mContext.getPackageManager();
        ArrayList mAllAppList = new ArrayList();
        // Return a List of all packages that are installed on the device.
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            // 判断系统/非系统应用
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)    // 非系统应用
            {

                String name = packageInfo.applicationInfo.loadLabel(pm)
                        .toString();
                String pkgname = "本地应用";
                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
                AppInfo info = new AppInfo(name, null, null, pkgname, icon, intent);
                //转成拼音，方便搜索和排序
                String sortLetters = new GetLetter(mCharacterParser, info.appName).getSortLetter();
                info.sortLetters = sortLetters;
                info.sortKey = name;
                info.sortToken = new ParseSortKey(mCharacterParser, info.sortKey).parseApptoSortKey(info.appName);

                mAllAppList.add(info);
            } else {
                //本地应用超级多，要不要列出来？
               /* String name=packageInfo.applicationInfo.loadLabel(pm)
                        .toString();
                String pkgname="本地应用";
                Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
                Intent intent=pm.getLaunchIntentForPackage(packageInfo.packageName);
                AppInfo info = new AppInfo(name,null,null,pkgname,icon,intent);

                //转成拼音，方便搜索和排序
                String sortLetters = new GetLetter(characterParser,info.appName).getSortLetter();
                info.sortLetters=sortLetters;
                info.sortKey=name;
                info.sortToken = new ParseSortKey(characterParser,info.sortKey).parseApptoSortKey(info.appName);
                mAllInfoList.add(info);
                mAllAppList.add(info);*/
            }

        }
        return mAllAppList;
    }
}
