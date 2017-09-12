package com.zdv.shangtongtianxia.receive;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.socks.library.KLog;
import com.zdv.shangtongtianxia.MainActivity;


import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class PushReceiver extends BroadcastReceiver {
    public static String RECEIVE_REDIRECT_MESSAGE = "receive_redirect_message";
    private NotificationManager nm;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (null == nm) {
           // nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
        KLog.d("onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            KLog.d("JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            KLog.d("接受到推送下来的自定义消息");
            processCustomMessage(context, bundle);
        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            KLog.d("接受到推送下来的通知");
            receivinzgNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            KLog.d("用户点击打开了通知");
            openNotification(context, bundle);
        } else {
            KLog.d("Unhandled intent - " + intent.getAction());
        }
    }

    private void receivinzgNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
       // KLog.d(" title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
      //  KLog.d("message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
      //  KLog.d("extras : " + extras);
        KLog.v(title+"---"+message+"----"+extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("myKey");
        } catch (Exception e) {
            KLog.w("Unexpected: extras is not a valid json");
            return;
        }

        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);


    }
    private void processCustomMessage(Context context, Bundle bundle) {

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        KLog.v(title+"---"+message+"----"+extras);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(message);
            Intent intent = new Intent();
            intent.setAction(RECEIVE_REDIRECT_MESSAGE);
            intent.putExtra("type",jsonObject.optString("type"));
            intent.putExtra("memcode",jsonObject.optString("memcode"));

            KLog.v(jsonObject.optString("type")+jsonObject.optString("memcode"));
            context.sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
       // NotificationHelper.showMessageNotification(context, nm, title, message, channel);
    }


}
