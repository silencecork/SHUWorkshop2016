package com.example.networkcommunication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		

	}
}