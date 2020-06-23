package com.ray.android.localservice;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements ServiceConnection {
    private LocalWordService wordService;
    private MessageReciver messageReciver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                wordList);

        setListAdapter(adapter);
        registerMessageReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Intent intent= new Intent(this, LocalWordService.class);
        //Intent intent= new Intent(this, ForegroundService.class);
        //bindService(intent, this, Context.BIND_AUTO_CREATE);
        //startService(intent);
        startMyIntentService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unbindService(this);

        //Intent intent= new Intent(this, LocalWordService.class);
        //stopService(intent);

        unregisterReceiver(messageReciver);
    }

    private ArrayAdapter<String> adapter;
    private List<String> wordList;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateList:
                if (wordService != null) {
                    Toast.makeText(this, "Number of elements" + wordService.getWordList().size(),
                            Toast.LENGTH_SHORT).show();
                    wordList.clear();
                    wordList.addAll(wordService.getWordList());
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.triggerServiceUpdate:
                //Intent service = new Intent(getApplicationContext(), LocalWordService.class);
                //Intent service = new Intent(getApplicationContext(), ForegroundService.class);
                //getApplicationContext().startService(service);


                break;
        }
    }


    // two options
    // startService()
    // bindService()

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        LocalWordService.MyBinder mbinder = (LocalWordService.MyBinder) binder;
        wordService = mbinder.getService();
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        wordService = null;
    }

    public void startMyIntentService(){

        String request_cat="fashion";
        Intent cbIntent =  new Intent();
        cbIntent.setClass(this, MyIntentService.class);
        cbIntent.putExtra("request_cat", request_cat);
        startService(cbIntent);
    }
    private void registerMessageReceiver(){
        messageReciver = new MessageReciver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyIntentService.CASHBACK_INFO);

        registerReceiver(messageReciver, intentFilter);
    }
    private class MessageReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cbinfo = intent.getStringExtra("message");
            Toast.makeText(context, cbinfo, Toast.LENGTH_SHORT).show();
        }
    }
}