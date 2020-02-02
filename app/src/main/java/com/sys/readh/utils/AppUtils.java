package com.sys.readh.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 系统相关
 * @author sys
 */
public class AppUtils {
    //获取微信版本
    public static String getMMVersion(Context context){
        return  getVersion(context,"com.tencent.mm");
    }
    //获取企业微信版本
    public static String getWeWorkVersion(Context context){
        return  getVersion(context,"com.tencent.wework");
    }
    public static String getVersion(Context context,String packname){
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packname, 0);
            String version = info.versionName;
            LogUtil.d(packname+"版本号"+version);
            return version;
        } catch (Exception e) {
            LogUtil.e(packname+"errorversion:",e);
        }
        return "无";
    }
    //获取本应用version
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }



}
