package com.example.networkcommunication;

import java.util.ArrayList;
import com.example.networkcommunication.MainActivity.Photo;
import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAdapter extends BaseAdapter {
	
	private ArrayList<Photo> mData;
	
	public PhotoAdapter(ArrayList<Photo> data) {
		mData = data;
	}

	@Override
	public int getCount() {
		return (mData != null) ? mData.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
		}
		
		ImageView image = (ImageView) convertView.findViewById(R.id.icon);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView author = (TextView) convertView.findViewById(R.id.author);
		
		Photo photo = (Photo) getItem(position);
		
		title.setText(photo.title);
		author.setText(photo.author);
		
		Picasso.with(parent.getContext()).load(photo.media).fit().centerCrop().into(image);
		
		return convertView;
	}

}
