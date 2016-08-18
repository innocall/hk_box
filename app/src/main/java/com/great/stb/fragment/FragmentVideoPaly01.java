package com.great.stb.fragment;

import java.util.ArrayList;
import java.util.List;

import com.great.stb.R;
import com.great.stb.activity.AdvActivity;
import com.great.stb.bean.ActDataMode;
import com.great.stb.dao.ElementDAO;
import com.great.stb.util.Util;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Fragment02
 *
 * @author kevin
 *
 */
public class FragmentVideoPaly01 extends Fragment {

	private List<Integer> videoSetList01 =new ArrayList<Integer>();
	private List<String> videoSetListFilePath =new ArrayList<String>();
	private GridView gv_video_list01;

	private View view;

	// 通信
	private Callbacks mCallbacks = sDummyCallbacks;

	public interface Callbacks {
		public List<Integer> getVideoSet();
	}

	private static Callbacks sDummyCallbacks = new Callbacks() {

		@Override
		public List<Integer> getVideoSet() {
			return null;
		}
	};

	// =====
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_video_paly01, null);

		initData();

		return view;

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}

	private void initData() {

		gv_video_list01 = (GridView) view.findViewById(R.id.gv_video_list01);
		gv_video_list01.setAdapter(new AppsAdapter());

		//集数地址
		ElementDAO dao = new ElementDAO(getActivity());
		ArrayList<ActDataMode> listFilePath = dao.getAllByWhereActDataMode(AdvActivity.codeVideo);
		for(int i=0;i<listFilePath.size();i++){
			videoSetListFilePath.add(listFilePath.get(i).getFilePath());
		}

		//=========

		List<Integer> list = new ArrayList<Integer>();
		list = mCallbacks.getVideoSet();

		for(int i=0 ;i<list.size();i++){
			if(i<18){
				videoSetList01.add(list.get(i));
			}
		}
		initListener();

	}

	private void initListener() {

		gv_video_list01.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

			}

		});

	}

	public class AppsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return videoSetList01.size();
		}

		@Override
		public Object getItem(int position) {
			return videoSetList01.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = View.inflate(getActivity(),
					R.layout.list_video_gridview01, null);
			TextView tv_video_set = (TextView) view
					.findViewById(R.id.tv_video_set);
			ImageButton ib_video_set_bg = (ImageButton) view
					.findViewById(R.id.ib_video_set_bg01);
			Log.i("FragmentVideoPaly01", "position=" + position);
			int set = position + 1;
			Log.i("FragmentVideoPaly01", "set=" + set);
			tv_video_set.setText("第" + set + "集");
			if(set == 1) {
				ib_video_set_bg.setFocusable(true);
				ib_video_set_bg.requestFocus();
				ib_video_set_bg.setFocusableInTouchMode(true);
			}
			ib_video_set_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.getVideoFromURL(getActivity(), videoSetListFilePath.get(position));
				}
			});

			if (position < 6) {
				ib_video_set_bg.setNextFocusUpId(R.id.tv_video_play01);
			}

			return view;

		}

	}

}
