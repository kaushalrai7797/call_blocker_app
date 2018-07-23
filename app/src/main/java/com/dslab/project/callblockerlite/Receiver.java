package com.dslab.project.callblockerlite;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by mukul on 14/4/17.
 */

public class Receiver extends BroadcastReceiver {

    Object ts;
    Method method1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
            DatabaseHelper helper = new DatabaseHelper(context);
            Cursor cursor = helper.getData();
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            while(cursor.moveToNext()){
                String num = cursor.getString(0);
                String num1 = "+91"+cursor.getString(0);
                Log.i("numm",num);
                Log.i("nummm",number);
                if(num1.equals(number) || num.equals(number)){
                    blockThisNumber(context,intent);
                    return;
                }
            }
        }
    }

    private void blockThisNumber(Context context , Intent intent) {

        Log.i("blocked","blocked");

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephonyManager.getClass().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = telephonyManager.getClass().getDeclaredMethod("getITelephony");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        method.setAccessible(true);
        try {
            ts = method.invoke(telephonyManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            method1 = ts.getClass().getDeclaredMethod("endCall");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            method1.invoke(ts);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
