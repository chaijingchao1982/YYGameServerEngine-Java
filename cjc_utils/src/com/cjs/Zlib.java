package com.cjs;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

public class Zlib {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	/**
	 * 压缩
	 * @param data 待压缩数据
	 * @return byte[] 压缩后的数据
	 */
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		// compresser.setLevel(9);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				bos.close();
			} catch (IOException e2) {
				CJCExceptionUtil.log(sLog, e2);
			}
		}
		compresser.end();
		return output;
	}

	/**
	 * 压缩
	 * @param data 待压缩数据
	 * @param os 输出流
	 */
	public static void compress(byte[] data, OutputStream os) {
		DeflaterOutputStream dos = new DeflaterOutputStream(os);
		try {
			dos.write(data, 0, data.length);
			dos.finish();
			dos.flush();
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/**
	 * 解压缩
	 * @param data 待压缩的数据
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			CJCExceptionUtil.log(sLog, e);
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				CJCExceptionUtil.log(sLog, e);
			}
		}

		decompresser.end();
		return output;
	}

	/**
	 * 解压缩
	 * @param is 输入流
	 * @return byte[] 解压缩后的数据
	 */
	public static byte[] decompress(InputStream is) {
		InflaterInputStream iis = new InflaterInputStream(is);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		try {
			int i = 1024;
			byte[] buf = new byte[i];

			while ((i = iis.read(buf, 0, i)) > 0) {
				o.write(buf, 0, i);
			}
		} catch (IOException e) {
			CJCExceptionUtil.log(sLog, e);
		}
		return o.toByteArray();
	}

	public static boolean compressPic(String srcFilePath, String descFilePath) {
		File file = null;
		BufferedImage src = null;
		FileOutputStream out = null;
		ImageWriter imgWrier;
		ImageWriteParam imgWriteParams;

		// 指定写图片的方式为 jpg
		imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
		imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
		// 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
		imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// 这里指定压缩的程度，参数qality是取值0~1范围内，
		imgWriteParams.setCompressionQuality((float) 0.4);
		imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
				colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		try {
			//			if (StringUtils.isBlank(srcFilePath)) {
			//				return false;
			//			} else {
			file = new File(srcFilePath);
			src = ImageIO.read(file);
			out = new FileOutputStream(descFilePath);

			imgWrier.reset();
			// 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
			// OutputStream构造
			imgWrier.setOutput(ImageIO.createImageOutputStream(out));
			// 调用write方法，就可以向输入流写图片
			imgWrier.write(null, new IIOImage(src, null, null), imgWriteParams);
			out.flush();
			out.close();
			//			}
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		//		try {
		//			DataInputStream dis = new DataInputStream(new FileInputStream(new File("E:\\cocos2d-x-2.1.5\\FatalContact\\Resources\\head\\t0.jpg")));
		//			byte[] data = new byte[dis.available()];
		//			int length = dis.read(data);
		//			System.out.println("origin length = " + length);
		//			byte[] dest = compress(data);
		//			System.out.println("dest length = " + dest.length);
		if (compressPic("E:\\cocos2d-x-2.1.5\\FatalContact\\Resources\\head\\t0.jpg",
				"E:\\cocos2d-x-2.1.5\\FatalContact\\Resources\\head\\tt0.jpg")) {
			System.out.println("success");
		} else {
			System.out.println("fail");
		}
		//		} catch (FileNotFoundException e) {
		//			
		//		} catch (IOException e) {
		//			
		//		}
	}
}
