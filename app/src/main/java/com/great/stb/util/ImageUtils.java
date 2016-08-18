package com.great.stb.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * 图片处理工具类
 * @author wu
 * @version 1.0
 * @date 2014.08.29
 */
public class ImageUtils {

	/**
	 * 获取本地压缩
	 * @param pathName 图片路径
	 * @param dw 屏幕width
	 * @param dh 屏幕heigth
	 * @return
	 */
	public static Bitmap decodeFileToCompress(String pathName, int dw, int dh) {
		// TODO Auto-generated method stub
		Options opts = new Options();
		opts.inJustDecodeBounds = true; //获取图片信息但不加载到内存
		Bitmap b = BitmapFactory.decodeFile(pathName, opts);
		opts.inJustDecodeBounds = false;
		//获取图片大小
		int imageX = (int) Math.ceil(opts.outWidth/(float)dw);
		int imageY = (int) Math.ceil(opts.outHeight/(float)dh);
		int sample = 1;
		if(imageX > imageY && imageX >= 1) {
			sample = imageX;
		}
		if(imageY > imageX && imageY >= 1) {
			sample = imageY;
		}
		opts.inSampleSize = sample;
		b = BitmapFactory.decodeFile(pathName, opts);
		return b;
	}

	/**
	 * 根据图片路径获取图片类型
	 * @param filePath 路径
	 * @return  类型
	 */
	public static String getImageType(String filePath) {
		String type = "png";
		if(filePath != null && !"".equals(filePath)) {
			type = filePath.substring(filePath.lastIndexOf("."), filePath.length());
		}
		return type;
	}
	
	/*public static void main(String[] args) {
		System.out.println(getImageType("http://192.168.18.234:8080/file/image/ssss.jpg"));
	}*/

}
