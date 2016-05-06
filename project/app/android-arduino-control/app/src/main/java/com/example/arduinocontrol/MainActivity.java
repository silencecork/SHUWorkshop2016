package com.example.arduinocontrol;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final String ARDUINO_YUN_IP = "192.168.43.169";
    private Switch mLightSwitch;
    private View mBackground;
    private int mCurrentBackgroundColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackground = findViewById(R.id.background);
        mBackground.setBackgroundColor(mCurrentBackgroundColor);

        mLightSwitch = (Switch) findViewById(R.id.switch1);
        mLightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    changeBackgroundColor(Color.WHITE, Color.BLACK);
                    sendLightOnOffRequest(false);
                } else {
                    changeBackgroundColor(Color.BLACK, Color.WHITE);
                    sendLightOnOffRequest(true);
                }
            }
        });
    }

    private void changeBackgroundColor(int startColor, int endColor) {
        ObjectAnimator colorFade = ObjectAnimator.ofObject(mBackground, "backgroundColor", new ArgbEvaluator(), startColor, endColor);
        colorFade.setDuration(3000);
        colorFade.start();
    }

    private void sendLightOnOffRequest(boolean isOn) {
        String url = (isOn) ?
                "http://" + ARDUINO_YUN_IP + "/arduino/digital/1" :
                "http://" + ARDUINO_YUN_IP + "/arduino/digital/0";
        
        StringRequest request = new StringRequest(Request.Method.GET, url, mOnSuccessListener, mOnErrorListener);
        NetworkManager.getInstance(MainActivity.this).request(null, request);
    }

    private Response.Listener<String> mOnSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("NetworkResponse", response);
        }
    };

    private Response.ErrorListener mOnErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("NetworkError", "", error);
            Toast.makeText(MainActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
        }
    };
}
