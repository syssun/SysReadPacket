package com.sys.readh.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.annotation.RequiresApi;
import com.sys.readh.utils.LogUtil;

/**
 * 打开通知
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class SysNotificationListenerService extends NotificationListenerService {

    private SharedPreferences sharedPreferences; //缓存本地使用

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sys_notificationListener","通知已开启...");
        editor.commit();
        LogUtil.d("通知已开启...");
    }
    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sys_notificationListener","通知已断开...");
        editor.commit();
        LogUtil.d("通知已断开...");
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        //企业微信 ，微信
        if("com.tencent.wework".equals(packageName) || "com.tencent.mm".equals(packageName)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Bundle extras = notification.extras;
                CharSequence notificationExtraText = extras.getCharSequence(Notification.EXTRA_TEXT);
                final String str = "" + notificationExtraText;
                String keywords = sharedPreferences.getString("sys_notification_keyword", "红包");
                String[] keywordArray = keywords.split(";");
                for(String keyword : keywordArray){
                    if(keyword != null && keyword.length() > 0){
                        if(str.contains(keyword)){
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                                break;
                            } catch (PendingIntent.CanceledException e) {
                                LogUtil.e("通知出错",e);
                            }
                        }
                    }
                }
            }
        }
    }
}

