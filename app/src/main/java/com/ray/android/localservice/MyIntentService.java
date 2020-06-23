package com.ray.android.localservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService{
    final static String CASHBACK_INFO = "cashback_info";
    final  String TAG = "MyIntentService";
    public MyIntentService() {
        super("Cashback IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String cb_category = intent.getStringExtra("request_cat");

        String cbinfo = getCashbackInfo(cb_category);
        sendCashbackInfoToClient(cbinfo);

        Log.d(TAG,"onHandleIntent");
    }
    private String getCashbackInfo(String cbcat){
        String cashback;
        if("electronics".equals(cbcat)){
            cashback = "Upto 20% cashback on electronics";
        }else if("fashion".equals(cbcat)){
            cashback = "Upto 60% cashbak on all fashion items";
        }else{
            cashback = "All other categories except fashion and electronics, flat 30% cashback";
        }
        return cashback;
    }
    private void sendCashbackInfoToClient(String msg){
        Intent intent = new Intent();
        intent.setAction(CASHBACK_INFO);
        intent.putExtra("message",msg);
        sendBroadcast(intent);

    }
}