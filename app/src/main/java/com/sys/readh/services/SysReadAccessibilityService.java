package com.sys.readh.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.sys.readh.R;
import com.sys.readh.utils.AppUtils;
import com.sys.readh.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class SysReadAccessibilityService extends AccessibilityService {
    private SharedPreferences sharedPreferences;
    private String currentActivity;
    private static final String package_wework = "com.tencent.wework";
    private static final String package_mm = "com.tencent.mm";
    private static final String keywordArray[] = {"红包]", "拼手气红包", "拼手气红包]", "红包"};
    private static final String h = "已领取"; //去掉过滤的红包
    //企业微信
    //聊天界面
    private static final String WMessageList = "com.tencent.wework.msg.controller.MessageListActivity";
    //红包开 界面
    private static final String WRedEnvelope = "com.tencent.wework.enterprise.redenvelopes.controller.RedEnvelopeCollectorActivity";
    //红包详情页
    private static final String WRedEnvelopeDetail = "com.tencent.wework.enterprise.redenvelopes.controller.RedEnvelopeDetailActivity";

    //微信开红包
    private static final String mmReadStr = "微信红包";
    private static final String MMessageList[] = {"android.widget.LinearLayout", "android.widget.FrameLayout", "android.widget.ListView"};
    private static final String MRedEnvelope = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
    private static final String MRedEnvelopeDetail = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    //
    private Boolean f = false; //控制 红包是否已领取

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        LogUtil.d("已启动");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        f = false;
        LogUtil.d("event=" + event.toString());
        String packagename = event.getPackageName().toString();
        int eveType = event.getEventType();
        //监听通知栏变化
        if (eveType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            onNotificationStateChanged(event);
            return;
        }
        //企业微信
        if (package_wework.equals(packagename)) {
            weworkEvent(event, packagename);
            return;
        }
        //微信
        if (package_mm.equals(packagename)) {
            //com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI
            mmEvent(event, packagename);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void mmEvent(AccessibilityEvent event, String packagename) {
        int evet = event.getEventType();
        String activityName = event.getClassName().toString();
        currentActivity = activityName;
        LogUtil.d(currentActivity);
        LogUtil.d(Arrays.asList(MMessageList).contains(currentActivity) + "");
        if ((evet == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || evet == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)) {
            if (MRedEnvelope.equals(currentActivity)) {
                LogUtil.d("openPacket");
                openReadPacket(packagename); // 先开红包
                return;
            } else if (Arrays.asList(MMessageList).contains(currentActivity) && !"android.widget.TextView".equals(currentActivity)) {
                queryReadPacket(packagename); //查找 // 消息列表  去掉支付界面
                return;
            } else if (MRedEnvelopeDetail.equals(currentActivity)) {
                if (getSharedPreferences("sys_seting_autoclose", true)) {
                    closeReadDetail(); // 关闭红包详情页面
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void weworkEvent(AccessibilityEvent event, String packagename) {
        int evet = event.getEventType();
        String activityName = event.getClassName().toString();
        currentActivity = activityName;
        if ((evet == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || evet == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)) {
            if (WRedEnvelope.equals(currentActivity)) {
                LogUtil.d("openPacket");
                openReadPacket(packagename); // 先开红包
                return;
            } else if ("android.widget.ListView".equals(currentActivity)
                    || WMessageList.equals(currentActivity)) { // 消息列表
                queryReadPacket(packagename); //查找
                return;
            } else if (WRedEnvelopeDetail.equals(currentActivity)) {
                if (getSharedPreferences("sys_seting_autoclose", true)) {
                    closeReadDetail(); // 关闭红包详情页面
                }
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void closeReadDetail() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            performGlobalAction(GLOBAL_ACTION_BACK); // 模拟按返回按钮
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void queryReadPacket(String packagename) {

        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        String searchText = getResources().getString(R.string.open_red_packet); // 红包
        if (package_mm.equals(packagename)) { //如果是微信
            searchText = getResources().getString(R.string.open_red_packet_mm); // 红包
        }
        AccessibilityNodeInfo node = getLastRedpackNode(rootNode, searchText); //查找含有红包的节点
        if (node != null && node.getText() != null) {
            //红包  只含有红包的字
            LogUtil.d("node:" + node.toString());
            if (!searchText.equals(node.getText().toString())) { //去掉其他的节点
                return;
            }
            if (node.getParent() != null && node.getParent().getParent() != null) {
                AccessibilityNodeInfo nodeParent = node.getParent().getParent();
                boolean flag = bl(nodeParent, h);//去掉 红包已领取
                if (flag) {
                    return;
                }
                if (node.isClickable()) { //直接命中可点击的红包
                    LogUtil.d("接命中可点击的红包 " + flag);
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return;
                }
                AccessibilityNodeInfo parent = null;
                while ((parent = node.getParent()) != null) {
                    LogUtil.d("node.getParent() " + node.getParent().toString());
                    if (parent.isClickable()) {
                        parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        break;
                    }
                    node = node.getParent();
                }
            }
        }
    }

    private AccessibilityNodeInfo getLastRedpackNode(AccessibilityNodeInfo rootNode, String searchText) {
        AccessibilityNodeInfo resultNode = null;
        if (rootNode != null) {
            List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByText(searchText);
            if (nodeInfoList != null && nodeInfoList.size() > 0) {
                return nodeInfoList.get(nodeInfoList.size() - 1);
            }
        }
        return resultNode;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void openReadPacket(String packagename) {
        final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            String haveBeenOpened = getResources().getString(R.string.red_packet_have_opened); // 手慢了，红包派完了
            String redPacketExpired = getResources().getString(R.string.red_packet_expired); // 红包已过期
            List<AccessibilityNodeInfo> resultList = nodeInfo.findAccessibilityNodeInfosByText(haveBeenOpened);
            List<AccessibilityNodeInfo> resultList2 = nodeInfo.findAccessibilityNodeInfosByText(redPacketExpired);
            LogUtil.d("手慢了，红包派完了 resultList=" + resultList.size());
            LogUtil.d("该红包已过期 resultList2=" + resultList2.size());
            // 判断红包是否已抢完，如已经抢完则自动关闭抢红包页面，如没有抢完则自动抢红包
            if (resultList.size() > 0 || resultList2.size() > 0) { // 红包已抢完
                LogUtil.d("红包已抢完或已失效");
                if (!getSharedPreferences("sys_seting_autoclose", true)) {
                    return;
                }
                performGlobalAction(GLOBAL_ACTION_BACK); // 模拟按返回键
            } else {
                String viewId = getOpenBtnIdWithPack(packagename); // 获取已安装版本企业微信红包开按钮的Id
                if (!TextUtils.isEmpty(viewId)) {
                    List<AccessibilityNodeInfo> list = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        list = nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
                    }
                    nodeInfo.recycle();
                    for (AccessibilityNodeInfo item : list) {
                        if (item.isClickable()) {
                            item.performAction(AccessibilityNodeInfo.ACTION_CLICK); //点击
                            break;
                        }
                    }
                }
            }
        }
    }

    private void onNotificationStateChanged(AccessibilityEvent event) {
        List<CharSequence> textList = event.getText();
        if (textList != null && textList.size() > 0) {
            LogUtil.d(textList.toArray().toString());
            String content = textList.get(textList.size() - 1).toString(); //最新的一条消息
            for (String keyword : keywordArray) {
                if (keyword != null && keyword.length() > 0) {
                    if (content.contains(keyword)) {
                        //模拟打开通知栏消息
                        Parcelable parcelableData = event.getParcelableData();
                        if (parcelableData != null && parcelableData instanceof Notification) {
                            Notification notification = (Notification) parcelableData;
                            PendingIntent pendingIntent = notification.contentIntent;
                            try {
                                pendingIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean getSharedPreferences(String key, boolean defaultValue) {
        if (sharedPreferences != null) {
            boolean value = sharedPreferences.getBoolean(key, defaultValue);
            return value;
        }
        return defaultValue;
    }

    public boolean bl(AccessibilityNodeInfo nodeParent, String h) {
        //遍历节点
        //红包已领取
        LogUtil.d("nodeParent:" + nodeParent.toString());
        for (int i = 0; i < nodeParent.getChildCount(); i++) {
            AccessibilityNodeInfo childenode = nodeParent.getChild(i);
            if (childenode.getChildCount() > 1) {
                bl(childenode, h);
            } else {
                if (childenode.getText() != null) {
                    LogUtil.d(childenode.getText().toString());
                    if (h.equals(childenode.getText().toString())) {
                        f = true;
                        break;
                    }

                }
            }
        }
        return f;
    }

    @Override
    public void onInterrupt() {
    }

    private String getOpenBtnIdWithPack(String packagename) {
        if (package_wework.equals(packagename)) {
            return sharedPreferences.getString("sys_weworkbtnid", "");
        }
        if (package_mm.equals(packagename)) {
            return sharedPreferences.getString("sys_mmbtnid", "");
        }
        return "";
    }
//com.tencent.wework:id/dvx  3.0.4

}
