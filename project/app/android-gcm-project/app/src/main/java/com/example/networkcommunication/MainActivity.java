package com.example.networkcommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		String token = GCMUtils.getSavedToken(this);
		if (TextUtils.isEmpty(token)) {
			AsyncTask<Void, Void, Void> getTokenTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					String token = GCMUtils.getGCMToken(MainActivity.this);
					if (!TextUtils.isEmpty(token)) {
						GCMUtils.saveToken(MainActivity.this, token);
					}
					return null;
				}
			};
			getTokenTask.execute();
		}
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
