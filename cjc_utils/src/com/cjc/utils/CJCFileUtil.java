package com.cjc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.string.CJCStringUtil;

/**
 * @author CJC
 * @date 2015-10
 */
public class CJCFileUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("./temp/app_private_key_pkcs8.pem");
		System.out.println(readString(file));
	}

	/**
	 * 续写文本到文件中
	 * @param filePath
	 * @param str
	 */
	public static void continueToWriteStringToFile(String filePath, String str) {
		FileWriter fw = null;
		try {
			// 传递一个true参数，代表不覆盖已有的文件。并在已有文件的末尾处进行数据续写。
			fw = new FileWriter(filePath, true);
			fw.write(str);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e2) {
				CJCExceptionUtil.log(sLog, e2);
			}
		}
	}

	public static byte[] readFileBytes(String filePath) {
		InputStream in = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				return null;
			}

			in = new FileInputStream(filePath);
			bis = new BufferedInputStream(in);
			dis = new DataInputStream(bis);

			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			byte[] buf = new byte[1024];

			while (true) {
				int len = dis.read(buf);
				if (len < 0)
					break;
				dos.write(buf, 0, len);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (bos != null) {
					bos.close();
				}

				if (dis != null) {
					dis.close();
				}
				if (bis != null) {
					bis.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				CJCExceptionUtil.log(sLog, e2);
			}
		}
	}

	/**
	 * 读文本文件
	 * @param file
	 * @return
	 */
	public static String readString(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file)); // 创建一个BufferedReader类来读取文件
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) { // 使用readLine方法，一次读一行
				sb.append(System.lineSeparator());
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					CJCExceptionUtil.log(sLog, e);
				}
			}
		}
	}

	/**
	 * 读文本文件
	 * @param path
	 * @return
	 */
	public static String readString(String path) {
		return readString(new File(path));
	}

	/**
	 * 获取不带扩展名的文件
	 * @param fileName
	 * @return
	 */
	public static String getFileNameNoEx(String fileName) {
		if ((fileName != null) && (fileName.length() > 0)) {
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length()))) {
				return fileName.substring(0, dot);
			}
		}
		return fileName;
	}

	/**
	 * 获取拓展名
	 * @param fileName
	 * @return
	 */
	public static String getExName(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 创建文件夹
	 * @param dirPath
	 * @return
	 */
	public static boolean mkdirs(String dirPath) {
		File file = new File(dirPath);
		if (file.exists()) {
			return false;
		}

		return file.mkdirs();
	}

	/**
	 * 删除文件夹
	 * @param dirPath
	 * @return 是否成功执行了删除操作
	 */
	public static boolean deleteDirs(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return false;
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDirs(files[i].getAbsolutePath());
			}
			files[i].delete();
		}

		return dir.delete();
	}

	/**
	 * 删除文件
	 * @param filePath
	 * @return 是否成功执行了删除操作
	 */
	public static boolean deleteFile(String filePath) {
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return f.delete();
	}

	/**
	 * 清除文件夹
	 * @param dirPath
	 */
	public static void clearDir(String dirPath) {
		CJCFileUtil.deleteDirs(dirPath);
		CJCFileUtil.mkdirs(dirPath);
	}

	/**
	 * 拷贝文件夹
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copyDir(String src, String dst) throws IOException {
		File file = new File(src);
		if (!file.exists()) {
			System.out.println(CJCStringUtil.append("copyDir err file is not exists path=", file.getAbsolutePath()));
			return;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File f : list) {
				String name = f.getName();
				String newPath = src + File.separator + name;
				String newCopyPath = dst + File.separator + name;

				File newFile = new File(dst);
				if (!newFile.exists()) {
					newFile.mkdir();
				}

				copyDir(newPath, newCopyPath);
			}
		} else if (file.isFile()) {
			Files.copy(Paths.get(src), Paths.get(dst), StandardCopyOption.REPLACE_EXISTING);
		} else {
			sLog.info("file type err path={}", file.getAbsolutePath());
		}
	}
}
