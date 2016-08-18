package com.great.stb.adapter;

import java.util.List;

import com.great.stb.R;
import com.great.stb.bean.ImagesSD;
import com.great.stb.bean.Music;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocalImageAdapter extends BaseAdapter {

	private List<ImagesSD> listMusic;
	private Context context;
	public LocalImageAdapter(Context context,List<ImagesSD> listMusic){
		this.context=context;
		this.listMusic=listMusic;
	}
	public void setListItem(List<ImagesSD> listMusic){
		this.listMusic=listMusic;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMusic.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listMusic.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.list_local_image_item, null);
		}
		ImagesSD m=listMusic.get(position);
		//音乐名
		TextView textImageName=(TextView) convertView.findViewById(R.id.image_item_name);
		textImageName.setText(m.getName());
//		textImageName.setText(m.getUrl());
		return convertView;
	}
}
