package com.great.stb.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import com.great.stb.bean.UpdateInfo;

import android.util.Xml;


public class UpdateInfoParser {

	/**
	 * 解析服务器返回的输入流
	 *
	 * @param is
	 * @return 更新信息 null解析失败
	 */
	public static UpdateInfo getUpdateInfos(InputStream is) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int type = parser.getEventType();
			UpdateInfo info = new UpdateInfo();
			while (type != XmlPullParser.END_DOCUMENT) {

				switch (type) {
					case XmlPullParser.START_TAG:
						if("version".equals(parser.getName())){
							String version = parser.nextText();
							info.setVersion(version);
						}else if("description".equals(parser.getName())){
							String description = parser.nextText();
							info.setDescription(description);
						}else if("path".equals(parser.getName())){
							String path = parser.nextText();
							info.setApkurl(path);
						}
						break;
				}
				type = parser.next();
			}
			return info;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
