package com.great.stb.fragment;

import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.microlog4android.config.PropertyConfigurator;
import com.great.stb.R;
import com.great.stb.dao.BlackListDao;

/**
 * Fragment02
 *
 * @author kevin
 */
public class Fragment02 extends Fragment {

	private List<ResolveInfo> apps;
	private GridView gv_apps_list;
	private View view;
	private AppsAdapter adapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			initData();
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment02, null);
		gv_apps_list = (GridView) view.findViewById(R.id.gv_apps_list);
		UpdatgeReceiver ur = new UpdatgeReceiver();
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
		intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
		intentFilter.addDataScheme("package");
		getActivity().registerReceiver(ur, intentFilter);
		loadApps(getActivity().getPackageManager());
		initListener();
		return view;
	}

	private void initData() {
		adapter = new AppsAdapter();
		gv_apps_list.setAdapter(adapter);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				onSetWallpaper();
				return true;
		}
		return false;
	}

	public void onSetWallpaper() {
		// 生成一个设置壁纸的请求
		final Intent pickWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
		Intent chooser = Intent.createChooser(pickWallpaper,
				"chooser_wallpaper");
		// 发送设置壁纸的请求
		startActivity(chooser);
	}

	private void initListener() {

		gv_apps_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				ResolveInfo info = apps.get(position);
				// 该应用的包名
				String pkg = info.activityInfo.packageName;
				// 应用的主activity类
				String cls = info.activityInfo.name;
				ComponentName componet = new ComponentName(pkg, cls);

				Intent intent = new Intent();
				intent.setComponent(componet);
				startActivity(intent);

			}

		});
	}

	private void loadApps(final PackageManager packageManager) {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		BlackListDao blackDao = new BlackListDao(getActivity().getApplicationContext());
		final List<String> blackList = blackDao.getAllRemoveAppPackageName();
		blackList.add("com.great.stb");
		new Thread() {
			public void run() {
				if(apps != null) {
					apps.clear();
				}
				apps = packageManager.queryIntentActivities(mainIntent, 0);
				for (String black : blackList) {
					for (int i = 0; i < apps.size(); i++) {
						if (black.equals(apps.get(i).activityInfo.packageName)) {
							apps.remove(i);
						}
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	public class AppsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return apps.size();
		}

		public void remove(int i) {
			apps.remove(i);
		}

		@Override
		public Object getItem(int position) {
			return apps.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
							ViewGroup parent) {
			ResolveInfo info = apps.get(position);
			if(convertView == null) {
				convertView = View.inflate(getActivity(),R.layout.list_apply_gridview, null);
			}
			ImageView iv_apply_icon = (ImageView) convertView .findViewById(R.id.iv_apply_icon);
			TextView tv_apply_name = (TextView) convertView.findViewById(R.id.tv_apply_name);
			ImageView ib_apply_bg = (ImageView) convertView.findViewById(R.id.ib_apply_bg);
			ib_apply_bg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ResolveInfo info = apps.get(position);
					// 该应用的包名
					String pkg = info.activityInfo.packageName;
					// 应用的主activity类
					String cls = info.activityInfo.name;
					ComponentName componet = new ComponentName(pkg, cls);

					Intent intent = new Intent();
					intent.setComponent(componet);
					startActivity(intent);
				}
			});
			if (position < 6) {
				ib_apply_bg.setNextFocusUpId(R.id.tv_title_02);
			}
			if (position % 6 == 0) {
				ib_apply_bg.setNextFocusLeftId(View.NO_ID);
			}
			Drawable loadIcon = info.activityInfo.loadIcon(getActivity().getPackageManager());
			iv_apply_icon.setImageDrawable(loadIcon);
			tv_apply_name.setText(info.activityInfo.loadLabel(getActivity()
					.getPackageManager()));
			return convertView;
		}

	}

	/*定义广播当应用程序改变时通知更新*/
	class UpdatgeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			PropertyConfigurator.getConfigurator(context).configure();

			//接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
			if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
				String packageNameInstall = intent.getDataString().substring(8);
				PackageManager packageManager = null;
				ApplicationInfo applicationInfo = null;
				try {
					packageManager = context.getPackageManager();
					applicationInfo = packageManager.getApplicationInfo(packageNameInstall, 0);
				} catch (NameNotFoundException e) {
					applicationInfo = null;
				}
				if(adapter != null) {
					//Log.i("Fragment02", "程序安装");
					loadApps(packageManager);
					adapter.notifyDataSetChanged();
				}

			}

			//接收广播：设备上删除了一个应用程序包。
			if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
				String packageNameUnStall = intent.getDataString().substring(8);
				PackageManager packageManager = null;
				ApplicationInfo applicationInfo = null;
				String applicationName = null;
				try {
					packageManager = context.getPackageManager();
					applicationInfo = packageManager.getApplicationInfo(packageNameUnStall, 0);
				} catch (NameNotFoundException e) {
					applicationInfo = null;
				}
				if(applicationInfo==null){
					applicationName =intent.getDataString().substring(8);
				}else{
					applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
				}
				if(adapter != null) {
					//Log.i("Fragment02", "adapter不为空2" + applicationName);
					for(int i=0; i<apps.size();i++) {
						ResolveInfo info = apps.get(i);
						String pkg = info.activityInfo.packageName;
						//Log.i("Fragment02",pkg + " ; " + applicationName);
						if(pkg.equals(applicationName)) {
							//Log.i("Fragment02","删除" + applicationName);
							adapter.remove(i);
						}
					}
					adapter.notifyDataSetChanged();
				}
			}
		}
	}


}
