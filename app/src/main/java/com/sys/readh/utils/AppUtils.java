package com.sys.readh.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

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
    public static void copyText(Context context, String str) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, str);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }
    public static void toastShow(Context context, String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    //是否安装某个app
    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
    //检查监听服务是否开启
    public static boolean checkStealFeature1(Context context,String service) {
        int ok = 0;
        LogUtil.d("ok:"+ok);
        try {
            ok = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter ms = new TextUtils.SimpleStringSplitter(':');
        if (ok == 1) {
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                ms.setString(settingValue);
                while (ms.hasNext()) {
                    String accessibilityService = ms.next();
                    LogUtil.d(accessibilityService);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false ;
    }
}
