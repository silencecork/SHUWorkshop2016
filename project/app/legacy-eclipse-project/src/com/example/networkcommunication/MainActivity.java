package com.example.networkcommunication;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.networkcommunication.volleymgr.NetworkManager;

public class MainActivity extends Activity {
	
	private ListView mListView;
	private ArrayList<Photo> mDatas;

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final Photo photo = mDatas.get(position);
			
			AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
			b.setTitle(photo.title);
			b.setMessage(Html.fromHtml(photo.desc));
			b.setPositiveButton("Open", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(photo.link));
					startActivity(intent);
				}
			});
			b.setNegativeButton("Close", null);
			b.show();
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (ListView) findViewById(R.id.flickr_photo_list);
		mListView.setOnItemClickListener(mOnItemClickListener );
		
		StringRequest request = new StringRequest(Request.Method.GET, "http://www.flickr.com/services/feeds/photos_public.gne?tags=soccer&format=json&jsoncallback=?", mCompleteListener, mErrorListener);
		NetworkManager.getInstance(this).request(null, request);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		NetworkManager.getInstance(this).stop();
	}
	
	private Listener<String> mCompleteListener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			response = response.substring(1, response.length() -1);
			try {
				JSONObject json = new JSONObject(response);
				String searchTitle = json.getString("title");
				getActionBar().setTitle(searchTitle);
				
				mDatas = new ArrayList<Photo>();
				
				JSONArray array = json.getJSONArray("items");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonPhoto = array.getJSONObject(i);
					
					Photo photo = new Photo();
					
					photo.title = (!jsonPhoto.has("title")) ? "" : jsonPhoto.getString("title");
					photo.link = (!jsonPhoto.has("link")) ? "" : jsonPhoto.getString("link");
					JSONObject mediaJson = jsonPhoto.getJSONObject("media");
					photo.media = (!mediaJson.has("m")) ? "" : mediaJson.getString("m");
					photo.desc = (!jsonPhoto.has("description")) ? "" : jsonPhoto.getString("description");
					photo.author = (!jsonPhoto.has("author")) ? "" : jsonPhoto.getString("author");
					mDatas.add(photo);
				}
				
				
				PhotoAdapter adapter = new PhotoAdapter(mDatas);
				mListView.setAdapter(adapter);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
	
	private ErrorListener mErrorListener = new ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			
		}
	};
	
	public class Photo {
		public String title;
		public String link;
		public String media;
		public String desc;
		public String author;
		
		@Override
		public String toString() {
			return title;
		}
		
	}
}