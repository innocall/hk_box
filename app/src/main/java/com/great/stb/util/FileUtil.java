package com.great.stb.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	public static final File DEFAULT_PARENT = new File(".");

	public static void copyFile(File fromFile, File toFile) throws IOException {
		if (toFile.isDirectory()) {
			throwIaxUnlessCanWriteDir(toFile, "toFile");
			if (fromFile.isFile()) {
				File targFile = new File(toFile, fromFile.getName());
				copyValidFiles(fromFile, targFile);
			} else if (fromFile.isDirectory()) {
				copyDir(fromFile, toFile);
			} else {
			}
		} else if (toFile.isFile()) {
			copyValidFiles(fromFile, toFile);
		} else {
			ensureParentWritable(toFile);
			if (fromFile.isFile()) {
				copyValidFiles(fromFile, toFile);
			} else if (fromFile.isDirectory()) {
				toFile.mkdirs();
				throwIaxUnlessCanWriteDir(toFile, "toFile");
				copyDir(fromFile, toFile);
			} else {
			}
		}
	}

	public static File ensureParentWritable(File path) {
		File pathParent = path.getParentFile();
		if (null == pathParent)
			pathParent = DEFAULT_PARENT;
		if (!(pathParent.canWrite()))
			pathParent.mkdirs();
		throwIaxUnlessCanWriteDir(pathParent, "pathParent");
		return pathParent;
	}

	public static int copyDir(File fromDir, File toDir, String fromSuffix,
							  String toSuffix, FileFilter delegate) throws IOException {
		String[] fromFiles;
		if ((null == fromDir) || (!(fromDir.canRead())))
			return 0;

		boolean haveSuffix = (null != fromSuffix) && (0 < fromSuffix.length());
		int slen = (!(haveSuffix)) ? 0 : fromSuffix.length();

		if (!(toDir.exists())) {
			toDir.mkdirs();
		}

		if (!(haveSuffix)) {
			fromFiles = fromDir.list();
		} else {
			FilenameFilter filter = new FilenameFilter() {
				private String val$fromSuffix;

				public boolean accept(File dir, String name) {
					return ((new File(dir, name).isDirectory()) || (name
							.endsWith(this.val$fromSuffix)));
				}

			};
			fromFiles = fromDir.list(filter);
		}
		int result = 0;
		int MAX = (null == fromFiles) ? 0 : fromFiles.length;
		for (int i = 0; i < MAX; ++i) {
			String filename = fromFiles[i];
			File fromFile = new File(fromDir, filename);
			if (fromFile.canRead())
				if (fromFile.isDirectory()) {
					result += copyDir(fromFile, new File(toDir, filename),
							fromSuffix, toSuffix, delegate);
				} else if (fromFile.isFile()) {
					if (haveSuffix)
						filename = filename.substring(0, filename.length()
								- slen);

					if (null != toSuffix)
						filename = filename + toSuffix;

					File targetFile = new File(toDir, filename);
					if ((null == delegate) || (delegate.accept(targetFile)))
						copyFile(fromFile, targetFile);

					++result;
				}
		}

		return result;
	}

	public static int copyDir(File fromDir, File toDir, String fromSuffix,
							  String toSuffix) throws IOException {
		return copyDir(fromDir, toDir, fromSuffix, toSuffix, (FileFilter) null);
	}

	public static int copyDir(File fromDir, File toDir) throws IOException {
		return copyDir(fromDir, toDir, null, null);
	}

	public static void copyStream(InputStream in, OutputStream out)
			throws IOException {
		int MAX = 4096;
		byte[] buf = new byte[4096];
		for (int bytesRead = in.read(buf, 0, 4096); bytesRead != -1; bytesRead = in
				.read(buf, 0, 4096))
			out.write(buf, 0, bytesRead);
	}

	public static void copyValidFiles(File fromFile, File toFile)
			throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(fromFile);
			out = new FileOutputStream(toFile);
			copyStream(in, out);
		} finally {
			if (out != null)
				out.close();

			if (in != null)
				in.close();
		}
	}

	public static void throwIaxUnlessCanWriteDir(File dir, String label) {
		if (!(canWriteDir(dir)))
			throw new IllegalArgumentException(label + " not writable dir: "
					+ dir);
	}

	public static boolean canWriteDir(File dir) {
		return ((null != dir) && (dir.canWrite()) && (dir.isDirectory()));
	}

	/**
	 * 保存文件
	 * @param toSaveString
	 * @param filePath 保存目录
	 */
	public static void saveFile(String toSaveString, File path, String filePath) {
		try {
			File saveFile = new File(path,filePath);
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(toSaveString.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件内容
	 * @param filePath
	 * @return 文件内容
	 */
	public static String readFile(String filePath) {
		String str = "";
		try {
			File readFile = new File(filePath);
			if (!readFile.exists()) {
				return null;
			}
			FileInputStream inStream = new FileInputStream(readFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}