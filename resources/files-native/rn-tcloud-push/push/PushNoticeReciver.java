package com.[@config:appReact.workName].push;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.tac.messaging.TACMessagingReceiver;
import com.tencent.tac.messaging.TACMessagingText;
import com.tencent.tac.messaging.TACMessagingToken;
import com.tencent.tac.messaging.TACNotification;
import com.tencent.tac.messaging.type.PushChannel;
import com.uhutu.ginkgo.ginkgocore.upgrade.SharedPreferencesHelper;

public class PushNoticeReciver extends TACMessagingReceiver {

    private final static String TAG="";

    // 启动 Messaging 服务后，会自动向 Messaging 后台注册，注册完成后会回调此接口。
    @Override
    public void onRegisterResult(Context context, int errorCode, TACMessagingToken token) {

        if(token.getTokenString()!=null&&token.getTokenString().length()>0){
            new SharedPreferencesHelper(context).inValue("pushNoticeToken",token.getTokenString());
        }

        Log.i("messaging", "MyReceiver::OnRegisterResult : code is " + errorCode + ", token is " + token.getTokenString());
    }

    // 反注册后回调此接口。
    @Override
    public void onUnregisterResult(Context context, int code) {


        Log.i("messaging", "MyReceiver::onUnregisterResult : code is " + code);
    }

    // 收到透传消息后回调此接口。
    @Override
    public void onMessageArrived(Context context, TACMessagingText tacMessagingText, PushChannel channel) {


        Log.i("messaging", "MyReceiver::OnTextMessage : message is " + tacMessagingText+ " pushChannel " + channel);
    }

    // 收到通知栏消息后回调此接口。
    @Override
    public void onNotificationArrived(Context context, TACNotification tacNotification, PushChannel pushChannel) {


        Log.i("messaging", "MyReceiver::onNotificationArrived : notification is " + tacNotification + " pushChannel " + pushChannel);

    }

    // 点击通知栏消息后回调此接口。
    @Override
    public void onNotificationClicked(Context context, TACNotification tacNotification, PushChannel pushChannel) {

        Log.i("messaging", "MyReceiver::onNotificationClicked : notification is " + tacNotification + " pushChannel " + pushChannel);

    }

    // 删除通知栏消息后回调此接口
    @Override
    public void onNotificationDeleted(Context context, TACNotification tacNotification, PushChannel pushChannel) {

        Log.i("messaging", "MyReceiver::onNotificationDeleted : notification is " + tacNotification + " pushChannel " + pushChannel);

    }

    // 绑定标签回调
    @Override
    public void onBindTagResult(Context context, int code, String tag) {

        Log.i("messaging", "MyReceiver::onBindTagResult : code is " + code + " tag " + tag);

    }

    // 解绑标签回调
    @Override
    public void onUnbindTagResult(Context context, int code, String tag) {

        Log.i("messaging", "MyReceiver::onUnbindTagResult : code is " + code + " tag " + tag);
    }
}