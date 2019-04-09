package com.everday.foregroundservice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.ref.WeakReference;

public class KeepManager {
    private static final KeepManager outInstance = new KeepManager();

    private KeepReceiver keepReceiver;

    private WeakReference<Activity> mKeepAct;

    public static KeepManager getInstance(){
        return outInstance;
    }

    public void registerKeep(Context context){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        keepReceiver = new KeepReceiver();
        context.registerReceiver(keepReceiver,filter);
    }

    public void unRegisterKeep(Context context){
        if(null != keepReceiver){
            context.unregisterReceiver(keepReceiver);
        }
    }

    public void startKeep(Context context){
        Intent intent = new Intent(context,KeppActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void finishKeep(){
        if(null!=mKeepAct){
            Activity activity = mKeepAct.get();
            activity.finish();
        }
        mKeepAct = null;
    }

    public void setKeep(KeppActivity keep){
        Log.e("KeepReceiver","添加Activity");
        mKeepAct = new WeakReference<Activity>(keep);
    }
}
