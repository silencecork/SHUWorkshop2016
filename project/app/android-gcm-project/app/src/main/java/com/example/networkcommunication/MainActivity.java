package com.example.networkcommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		AsyncTask<Void, Void, Void> getTokenTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				String token = GCMUtils.getGCMToken(MainActivity.this);
				return null;
			}
		};

		getTokenTask.execute();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		

	}
}
