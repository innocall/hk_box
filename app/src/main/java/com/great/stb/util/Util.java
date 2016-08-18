package com.great.stb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import com.great.stb.bean.ImagesSD;
import com.great.stb.bean.Music;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

public class Util {

	private static List<String> filenames = new ArrayList<String>();

	/**
	 * 保存图片到sd卡
	 *
	 * @param bitmap
	 * @param picname
	 */
	public static void saveImage2SD(Bitmap bitmap, String picname) {
		String SDCarePath = Environment.getExternalStorageDirectory()
				.toString();
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			Log.v("TestFile", "SD card is not avaiable/writeable right now.");
			return;
		}
		FileOutputStream b = null;
		File file = new File(SDCarePath + "/myImage/");
		file.mkdirs();// 创建文件夹
		String fileName = SDCarePath + "/myImage/" + picname + ".png";
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * url获取图片Bitmap
	 *
	 * @param aurl
	 * @return
	 */
	public static Bitmap getImageFromURL(String urlStr) {

		Bitmap bitmap = null;
		try {
			if (urlStr != null && !"".equals(urlStr)) {
				String urlSunStr01 = urlStr.substring(0,
						urlStr.lastIndexOf("/"));
				String urlSubStr02 = urlStr
						.substring(urlStr.lastIndexOf("/") + 1);
				String urlSubStr03 = URLEncoder.encode(urlSubStr02, "UTF-8");

				// http://61.233.25.22/AgencyFile/2/20140115153646_A%E5%A5%97LOGO%E5%9B%BE.png
				URL url = new URL(urlSunStr01 + "/" + urlSubStr03);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
			} else {
				return null;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap getImageFromURL2(String urlStr, int dw, int dh) {

		Bitmap bitmap = null;
		try {
			if (urlStr != null) {
				String urlSunStr01 = urlStr.substring(0,
						urlStr.lastIndexOf("/"));
				String urlSubStr02 = urlStr
						.substring(urlStr.lastIndexOf("/") + 1);
				String urlSubStr03 = URLEncoder.encode(urlSubStr02, "UTF-8");
				// http://61.233.25.22/AgencyFile/2/20140115153646_A%E5%A5%97LOGO%E5%9B%BE.png
				URL url = new URL(urlSunStr01 + "/" + urlSubStr03);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				// byte[] b = inputStream2ByteArr(is);
				// bitmap = BitmapFactory.decodeStream(is);
				bitmap = decodeBitmap(is, dw, dh);
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return bitmap;
	}

	private static Bitmap decodeBitmap(InputStream is, int dw, int dh)
			throws IOException {
		if (is == null) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置该属性可以不占用内存，并且能够得到bitmap的宽高等属性，此时得到的bitmap是空
		options.inJustDecodeBounds = true;
		byte[] data = inputStream2ByteArr(is);// 将InputStream转为byte数组，可以多次读取
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
				options);
		// 设置计算得到的压缩比例
		int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
		int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);
		// 真正的解析位图
		options.inJustDecodeBounds = false;
		if (heightRatio > widthRatio) {
			options.inSampleSize = heightRatio;
		} else {
			options.inSampleSize = widthRatio;
		}
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		return bitmap;
	}

	private static byte[] inputStream2ByteArr(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buff)) != -1) {
			outputStream.write(buff, 0, len);
		}
		inputStream.close();
		outputStream.close();
		return outputStream.toByteArray();
	}

	/**
	 * drawable 转化为bitmap
	 *
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitmap的缩放
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * 在线播放视频
	 *
	 * @param context
	 * @param urlStr
	 */
	public static void getVideoFromURL(Context context, String urlStr) {

		// Uri uri = Uri.parse( "http://m.itrends.com.cn/lvxxx/video/1.mp4" );
		// Uri uri = Uri.parse( "/sdcard/test.mp4" );
		Uri uri = Uri.parse(urlStr);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setType("video/*");
		intent.setDataAndType(uri, "video/*");
		context.startActivity(intent);
	}

	/**
	 * dip转化为px
	 *
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 获取U盘所以音乐
	 * @return
	 */
	public static List<ImagesSD> getUSHostMusicData() {
		List<ImagesSD> musicList = new ArrayList<ImagesSD>();
		File f = new File("/mnt/usbhost0/");
		File f2 = new File("/mnt/usbhost1/");
		File[] files = f.listFiles();
		File[] files2 = f2.listFiles();
		if ((files == null || files.length == 0) && (files2 == null || files2.length == 0))
			return musicList;
		if(files !=null) {
			musicList = getAllMusicFile(files, musicList);
		}
		if(files2 != null) {
			musicList = getAllMusicFile(files2, musicList);
		}
		return musicList;
	}

	public static List<ImagesSD> getAllMusicFile(File[] file,List<ImagesSD> musicList) {
		for (int i = 0; i < file.length; i++) {
			File f = file[i];
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				if (fs != null && fs.length != 0) {
					getAllMusicFile(fs, musicList);
				}
			} else {
				if (isMusicFile(f.getPath())) {
					ImagesSD iamgesd = new ImagesSD();
					iamgesd.setName(f.getPath().substring(
							f.getPath().lastIndexOf("/") + 1));
					iamgesd.setUrl(f.getPath());
					musicList.add(iamgesd);
				}
			}
		}
		return musicList;
	}

	private static boolean isMusicFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		/**
		 * 依据文件扩展名判断是否为视频文件
		 */
		if (end.equals("mp3")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	/**
	 * 得到sd卡音乐列表
	 *
	 * @param context
	 * @return
	 */
	public static List<Music> getMusicData(Context context) {
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			// 获取所有歌曲

			Cursor cursor = cr.query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

			if (null == cursor) {
				return null;
			}
			if (cursor.moveToFirst()) {
				do {
					Music m = new Music();
					String title = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.TITLE));
					String singer = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));
					if ("<unknown>".equals(singer)) {
						singer = "未知艺术家";
					}
					String album = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					long size = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.SIZE));
					long time = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Audio.Media.DURATION));
					String url = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATA));
					String name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					String sbr = name.substring(name.length() - 3,
							name.length());
					// Log.e("--------------", sbr);
					if (sbr.equals("mp3")) {
						m.setTitle(title);
						m.setSinger(singer);
						m.setAlbum(album);
						m.setSize(size);
						m.setTime(time);
						m.setUrl(url);
						m.setName(name);
						musicList.add(m);
					}
				} while (cursor.moveToNext());
			}
		}
		return musicList;

	}

	public static List<ImagesSD> getUShostVideoData(Context context) {
		List<ImagesSD> musicList = new ArrayList<ImagesSD>();
		File f = new File("/mnt/usbhost0/");
		File f2 = new File("/mnt/usbhost1/");
		File[] files = f.listFiles();
		File[] files2 = f2.listFiles();
		if ((files == null || files.length == 0) && (files2 == null || files2.length == 0))
			return musicList;
		if(files !=null) {
			musicList = getAllVideoFile(files, musicList);
		}
		if(files2 != null) {
			musicList = getAllVideoFile(files2, musicList);
		}
		return musicList;
	}

	public static List<ImagesSD> getAllVideoFile(File[] file,List<ImagesSD> musicList) {
		for (int i = 0; i < file.length; i++) {
			File f = file[i];
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				if (fs != null && fs.length != 0) {
					getAllVideoFile(fs, musicList);
				}
			} else {
				if (isVideoFile(f.getPath())) {
					ImagesSD iamgesd = new ImagesSD();
					iamgesd.setName(f.getPath().substring(
							f.getPath().lastIndexOf("/") + 1));
					iamgesd.setUrl(f.getPath());
					musicList.add(iamgesd);
				}
			}
		}
		return musicList;
	}

	private static boolean isVideoFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		/**
		 * 依据文件扩展名判断是否为视频文件
		 */
		if (end.equalsIgnoreCase("mp4")|| end.equalsIgnoreCase("flv")|| end.equalsIgnoreCase("avi")|| end.equalsIgnoreCase("rm")
				|| end.equalsIgnoreCase("rmvb")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	/**
	 * 得到sd卡视频列表
	 * @param context
	 * @return
	 */
	public static List<Music> getVideoData(Context context) {
		List<Music> musicList = new ArrayList<Music>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			// 获取所有歌曲
			Cursor cursor = cr.query(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
			if (null == cursor) {
				return null;
			}
			if (cursor.moveToFirst()) {
				do {
					Music m = new Music();
					String title = cursor.getString(cursor
							.getColumnIndex(MediaStore.Video.Media.TITLE));
					String singer = cursor.getString(cursor
							.getColumnIndex(MediaStore.Video.Media.ARTIST));
					if ("<unknown>".equals(singer)) {
						singer = "未知艺术家";
					}
					String album = cursor.getString(cursor
							.getColumnIndex(MediaStore.Video.Media.ALBUM));
					long size = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Video.Media.SIZE));
					long time = cursor.getLong(cursor
							.getColumnIndex(MediaStore.Video.Media.DURATION));
					String url = cursor.getString(cursor
							.getColumnIndex(MediaStore.Video.Media.DATA));
					String name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
					String sbr = name.substring(name.length() - 3,
							name.length());

					String sbr_two = name.substring(name.length() - 2,
							name.length());
					String sbr_four = name.substring(name.length() - 4,
							name.length());
					// Log.e("--------------", sbr);
					if (sbr.equalsIgnoreCase("mp4")
							|| sbr.equalsIgnoreCase("flv")
							|| sbr.equalsIgnoreCase("avi")
							|| sbr_two.equalsIgnoreCase("rm")
							|| sbr_four.equalsIgnoreCase("rmvb")) {
						m.setTitle(title);
						m.setSinger(singer);
						m.setAlbum(album);
						m.setSize(size);
						m.setTime(time);
						m.setUrl(url);
						m.setName(name);
						musicList.add(m);
					}
				} while (cursor.moveToNext());
			}
		}
		return musicList;

	}

	/**
	 * 得到sd卡图片列表
	 *
	 * @param context
	 * @return
	 */
	public static List<ImagesSD> getImageData(Context context) {
		List<ImagesSD> musicList = new ArrayList<ImagesSD>();
		ContentResolver cr = context.getContentResolver();
		if (cr != null) {
			// 获取所有歌曲

			Cursor cursor = cr.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
					null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);

			if (null == cursor) {
				return null;
			}
			if (cursor.moveToFirst()) {
				do {
					ImagesSD iamgesd = new ImagesSD();
					String name = cursor
							.getString(cursor
									.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
					String end = name.substring(name.lastIndexOf(".") + 1,
							name.length()).toLowerCase();
					String url = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					if (end.equals("jpg") || end.equals("gif")
							|| end.equals("png") || end.equals("jpeg")
							|| end.equals("bmp")) {
						iamgesd.setName(name);
						iamgesd.setUrl(url);
						musicList.add(iamgesd);
					} else {

					}

				} while (cursor.moveToNext());
			}
		}
		return musicList;
	}

	public static List<ImagesSD> getImagesFromSD() {
		List<ImagesSD> imageList = new ArrayList<ImagesSD>();
		/*
		 * File f = new File("/sdcard/"); if
		 * (Environment.getExternalStorageState().equals(
		 * Environment.MEDIA_MOUNTED)) { f = new File("/mnt/usbhost0/"); // f =
		 * new // File(Environment.getExternalStorageDirectory().toString()); }
		 * else { // 确认插入sd卡 return imageList; }
		 */
		File f = new File("/mnt/usbhost0/");
		File f2 = new File("/mnt/usbhost1/");
		File[] files = f.listFiles();
		File[] files2 = f2.listFiles();
		if ((files == null || files.length == 0) && (files2 == null || files2.length == 0))
			return imageList;
		if(files !=null) {
			imageList = getAllFile(files, imageList);
		}
		if(files2 != null) {
			imageList = getAllFile(files2, imageList);
		}
		/*
		 * for (int i = 0; i < files.length; i++) { File file = files[i]; if
		 * (isImageFile(file.getPath())){ ImagesSD iamgesd = new ImagesSD();
		 * iamgesd
		 * .setName(file.getPath().substring(file.getPath().lastIndexOf("/") +
		 * 1)); iamgesd.setUrl(file.getPath()); imageList.add(iamgesd); } }
		 */
		return imageList;
	}

	public static List<ImagesSD> getAllFile(File[] file,
											List<ImagesSD> imageList) {
		for (int i = 0; i < file.length; i++) {
			File f = file[i];
			if (f.isDirectory()) {
				File[] fs = f.listFiles();
				if (fs != null && fs.length != 0) {
					getAllFile(fs, imageList);
				}
			} else {
				if (isImageFile(f.getPath())) {
					ImagesSD iamgesd = new ImagesSD();
					iamgesd.setName(f.getPath().substring(
							f.getPath().lastIndexOf("/") + 1));
					iamgesd.setUrl(f.getPath());
					imageList.add(iamgesd);
				}
			}
		}
		return imageList;
	}

	private static boolean isImageFile(String fName) {
		boolean re;
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/**
		 * 依据文件扩展名判断是否为图像文件
		 */
		if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			re = true;
		} else {
			re = false;
		}
		return re;
	}

	/**
	 * 是否连网
	 *
	 * @param act
	 * @return
	 */
	public static boolean detect(Context act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						act.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

	/**
	 * 有线获取本机MAC地址方法
	 *
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {
		String mac = null;
		try {
			Enumeration localEnumeration = NetworkInterface
					.getNetworkInterfaces();

			while (localEnumeration.hasMoreElements()) {
				NetworkInterface localNetworkInterface = (NetworkInterface) localEnumeration
						.nextElement();
				String interfaceName = localNetworkInterface.getDisplayName();

				if (interfaceName == null) {
					continue;
				}

				if (interfaceName.equals("eth0")) {
					// MACAddr = convertMac(localNetworkInterface
					// .getHardwareAddress());
					mac = convertToMac(localNetworkInterface
							.getHardwareAddress());
					if (mac != null && mac.startsWith("0:")) {
						mac = "0" + mac;
					}
					break;
				}

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return mac;
	}

	private static String convertToMac(byte[] mac) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			byte b = mac[i];
			int value = 0;
			if (b >= 0 && b <= 16) {
				value = b;
				sb.append("0" + Integer.toHexString(value));
			} else if (b > 16) {
				value = b;
				sb.append(Integer.toHexString(value));
			} else {
				value = 256 + b;
				sb.append(Integer.toHexString(value));
			}
			if (i != mac.length - 1) {
				sb.append(":");
			}
		}
		return sb.toString();
	}

	/**
	 * 返回当前应用程序的版本号
	 *
	 * @return
	 */
	public static String getVersion(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(),
					0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach
			return "";
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		if(sourceFile != null) {
			BufferedInputStream inBuff = null;
			BufferedOutputStream outBuff = null;
			try {
				// 新建文件输入流并对它进行缓冲
				inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
				// 新建文件输出流并对它进行缓冲
				outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
				// 缓冲数组
				byte[] b = new byte[1024 * 5];
				int len;
				while ((len = inBuff.read(b)) != -1) {
					outBuff.write(b, 0, len);
				}
				// 刷新此缓冲的输出流
				outBuff.flush();
			} finally {
				// 关闭流
				if (inBuff != null)
					inBuff.close();
				if (outBuff != null)
					outBuff.close();
			}
		}
	}

	/**
	 * 当前正在运行的应用的包名
	 *
	 * @param context
	 * @return
	 */
	public static String getCurrentPk(Context context) {

		// ActivityManager am = (ActivityManager)
		// context.getSystemService("activity");
		//
		// ComponentName topActivity = am.getRunningTasks(1).get(0).topActivity;
		// String packageName = topActivity.getPackageName();
		// String className = topActivity.getClassName();
		Stack<Activity> activityStack = new Stack<Activity>();
		Activity lastElement = activityStack.lastElement();
		String packageName = lastElement.getPackageName();

		return packageName;

	}

}
