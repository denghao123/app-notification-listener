package com.dh.notice;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.os.Build;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HelperNotificationListenerService extends NotificationListenerService {

    public static String SEND_MSG_BROADCAST = "notify_msg";

    @Override
    public void onListenerConnected() {
        Intent intent = new Intent();
        intent.putExtra("msg", "开始监听");
        intent.setAction(SEND_MSG_BROADCAST);
        sendBroadcast(intent);
        System.out.println("onListenerConnected 成功联接...");

    }

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification sbn,RankingMap rankingMap) {
        // 获取接收消息APP的包名
        String packageName  = sbn.getPackageName();
        String content = null;

        // 当 API > 18 时，使用 extras 获取通知的详细信息
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bundle extras = sbn.getNotification().extras;
            if (extras != null) {
                // 获取通知标题
                String title = extras.getString(Notification.EXTRA_TITLE, "");
                // 获取通知内容
                String text = extras.getString(Notification.EXTRA_TEXT, "");
                content =  packageName + "---" + title + "---" + text;
            }
        } else {
            Map<String, Object> notiInfo = getNotiInfo(sbn.getNotification());
            if (null != notiInfo) {
                content =packageName + "---" + notiInfo.get("title") + "---" + notiInfo.get("text");
            }
        }
        if (content == null || content.length() == 1) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("msg", content);
        intent.setAction(SEND_MSG_BROADCAST);
        sendBroadcast(intent);
    }

    private Map<String, Object> getNotiInfo(Notification notification) {
        int key = 0;
        if (notification == null)
            return null;
        RemoteViews views = notification.contentView;
        if (views == null)
            return null;
        Class secretClass = views.getClass();

        try {
            Map<String, Object> text = new HashMap<>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions"))
                    continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i].get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();
                    Object value = null;
                    Integer type = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        }
                    }
                    // 经验所得 type 等于9 10为短信title和内容，不排除其他厂商拿不到的情况
                    if (type != null && (type == 9 || type == 10)) {
                        if (key == 0) {
                            text.put("title", value != null ? value.toString() : "");
                        } else if (key == 1) {
                            text.put("text", value != null ? value.toString() : "");
                        } else {
                            text.put(Integer.toString(key), value != null ? value.toString() : null);
                        }
                        key++;
                    }
                }
                key = 0;

            }
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//
//    // 在删除消息时触发
//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Bundle extras = sbn.getNotification().extras;
//        // 获取接收消息APP的包名
//        String notificationPkg = sbn.getPackageName();
//        // 获取接收消息的抬头
//        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);
//        // 获取接收消息的内容
//        String notificationText = extras.getString(Notification.EXTRA_TEXT);
//        System.out.println("移除消息： "+notificationPkg +" & "+ notificationTitle + " & " + notificationText);
//    }
}