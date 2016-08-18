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
 * FragmentVideoPaly04
 *
 * @author kevin
 *
 */
public class FragmentVideoPaly04 extends Fragment {

	private List<Integer> videoSetList04 = new ArrayList<Integer>();
	private List<String> videoSetListFilePath =new ArrayList<String>();
	private GridView gv_video_list04;

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

		view = inflater.inflate(R.layout.fragment_video_paly04, null);

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

		gv_video_list04 = (GridView) view.findViewById(R.id.gv_video_list04);
		gv_video_list04.setAdapter(new AppsAdapter());

		ElementDAO dao = new ElementDAO(getActivity());
		ArrayList<ActDataMode> listFilePath = dao.getAllByWhereActDataMode(AdvActivity.codeVideo);
		for(int i=54;i<listFilePath.size();i++){
			videoSetListFilePath.add(listFilePath.get(i).getFilePath());
		}

		List<Integer> list = new ArrayList<Integer>();

		list = mCallbacks.getVideoSet();

		for(int i=0 ;i<list.size();i++){
			if(i>=54&&i<72){
				videoSetList04.add(list.get(i));
			}
		}
		initListener();

	}

	private void initListener() {

		gv_video_list04.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

			}

		});

	}

	public class AppsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return videoSetList04.size();
		}

		@Override
		public Object getItem(int position) {
			return videoSetList04.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = View.inflate(getActivity(),
					R.layout.list_video_gridview04, null);
			TextView tv_video_set = (TextView) view
					.findViewById(R.id.tv_video_set);
			ImageButton ib_video_set_bg = (ImageButton) view
					.findViewById(R.id.ib_video_set_bg04);
			Log.i("FragmentVideoPaly04", "position=" + position);
			int set = position + 55;
			Log.i("FragmentVideoPaly04", "set=" + set);
			tv_video_set.setText("第" + set + "集");

			ib_video_set_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Util.getVideoFromURL(getActivity(), videoSetListFilePath.get(position));
				}
			});

			if (position < 6) {
				ib_video_set_bg.setNextFocusUpId(R.id.tv_video_play04);
			}

			return view;

		}

	}

}
