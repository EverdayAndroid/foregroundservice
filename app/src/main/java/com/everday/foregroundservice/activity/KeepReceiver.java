package com.everday.foregroundservice.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class KeepReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(TextUtils.equals(action,Intent.ACTION_SCREEN_OFF)){
            Log.e("KeepReceiver","关屏");
            KeepManager.getInstance().startKeep(context);
        }else if(TextUtils.equals(action,Intent.ACTION_SCREEN_ON)){
            Log.e("KeepReceiver","开屏");
            KeepManager.getInstance().finishKeep();
        }
    }
}
