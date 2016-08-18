package com.great.stb.util;

import java.io.File;
import java.io.IOException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

/**
 * 手机工具类
 *
 * @author wu
 *
 */
public class SystemAppUtils {

	/**
	 * 检测该包名所对应的应用是否存在
	 *
	 * @param packageName
	 * @return
	 */
	public static boolean checkPackage(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 下载
	 * @param context 上下文
	 * @param url     下载地址
	 * @param path    保存路径
	 */
	public static void downLoadApk(Context context,String url,final String path,final Activity activity) {
		FinalHttp fh = new FinalHttp();
		fh.download(url, path, new AjaxCallBack<File>() {
			@Override
			public void onSuccess(File t) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setDataAndType(Uri.fromFile(t),"application/vnd.android.package-archive");
				activity.startActivity(intent);
				//finish();
				Log.i("TAG", "安装推送app" + path);
				Log.i("TAG", "安装推送app成功");
				super.onSuccess(t);
			}

			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
			}

		});
	}

}
