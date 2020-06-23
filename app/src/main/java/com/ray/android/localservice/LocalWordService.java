package com.ray.android.localservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
	private List<String> resultList = new ArrayList<String>();
	private int counter = 1;
	private final String TAG="LocalWordService";

	//assume we start this service with activity.bindService


	/*
	The system invokes this method to perform one-time setup procedures when the service is initially created
	(before it calls either onStartCommand() or onBind()). If the service is already running, this method is not called.
	*/
	@Override
	public void onCreate() {
		Log.d(TAG,"OnCreate");
	}


/*
	The system invokes this method by calling startService() when another component (such as an activity)
	requests that the service be started. When this method executes, the service is started and can run
	in the background indefinitely.

		*/

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG,"OnStartCommand");
		addResultValues();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				LocalWordService.this.stopSelf();
			}
		}, 2000);

		/*
		START_STICKY 			:  tells the OS to recreate the service after it has enough memory and call onStartCommand() again with a null intent.
		START_NOT_STICKY	 	:  tells the OS to not bother recreating the service again.
		START_REDELIVER_INTENT	:  tells the OS to recreate the service and redeliver the same intent to onStartCommand()
		codes are only relevant when the phone runs out of memory and kills the service before it finishes executing

		 */

		return Service.START_NOT_STICKY;
	}

	/*
	* The system invokes this method by calling bindService() when another component wants to bind
	* with the service (such as to perform RPC).
	* */
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG,"OnBind");
		addResultValues();
		return mBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"Service STOP");
	}

	public class MyBinder extends Binder {
		LocalWordService getService() {
			return LocalWordService.this;
		}
	}

	public List<String> getWordList() {
		return resultList;
	}

	private void addResultValues() {
		Random random = new Random();
		List<String> input = Arrays.asList("Linux", "Android","iPhone","Windows7" );
		String newword=input.get(random.nextInt(3)) + " " + counter++;
		Toast.makeText(getApplicationContext(),newword,Toast.LENGTH_SHORT).show();
		resultList.add(newword);
		if (counter == Integer.MAX_VALUE) {
			counter = 0;
		}
	}
}
