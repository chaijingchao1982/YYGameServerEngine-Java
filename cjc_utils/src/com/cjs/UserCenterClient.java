package com.cjs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

/**
 * 用户中心类
 * 
 * @author JasonChan
 * @date 2013-6-3
 */
public class UserCenterClient {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static final java.lang.String GET = "GET";
	public static final java.lang.String POST = "POST";
	public static final java.lang.String PUT = "PUT";
	public static final java.lang.String DELETE = "DELETE";
	public static final String SUCCESS = "0";
	public static final int FAIL = 1000; // 未知错误
	public static final int connectTimeout = 10000;// 连接超时时间
	private String userCenterUrl;

	public UserCenterClient() {

	}

	public UserCenterClient(String userCenterUrl) {
		this.userCenterUrl = userCenterUrl;
	}

	public static void main(String[] args) {
		String m = "0";
		String uid = "112adf";
		String un = "中文来一个";
		String pw = "111111";
		String em = "12@11.com";
		String opw = "";

		try {
			StringBuilder sb = new StringBuilder(512);
			sb.append("m=");
			sb.append(m);
			sb.append("&uid=");
			sb.append(uid);
			sb.append("&un=");
			sb.append(URLEncoder.encode(un, "utf-8"));
			sb.append("&pw=");
			sb.append(URLEncoder.encode(pw, "utf-8"));
			sb.append("&em=");
			sb.append(URLEncoder.encode(em, "utf-8"));
			sb.append("&opw=");
			sb.append(URLEncoder.encode(opw, "utf-8"));
			String sa = RSASignature.sign(sb.toString(), RSAKey.RSA_PRIVATE);
			sb.append("&signature=");
			sb.append(URLEncoder.encode(sa, "utf-8"));
			System.out.println(sb.toString());

			StringBuilder sb2 = new StringBuilder(512);
			sb2.append("m=");
			sb2.append(m);
			sb2.append("&uid=");
			sb2.append(uid);
			sb2.append("&un=");
			sb2.append(URLEncoder.encode(un, "utf-8"));
			sb2.append("&pw=");
			sb2.append(URLEncoder.encode(pw, "utf-8"));
			sb2.append("&em=");
			sb2.append(URLEncoder.encode(em, "utf-8"));
			sb2.append("&opw=");
			sb2.append(URLEncoder.encode(opw, "utf-8"));
			boolean result = RSASignature.doCheck(sb2.toString(), sa, RSAKey.RSA_PUBLIC);
			System.out.println(result);
		} catch (UnsupportedEncodingException e) {
			CJCExceptionUtil.log(sLog, e);
		}
	}

	/**
	 * 修改信息
	 * 
	 * @param m
	 * @param uid
	 * @param un
	 * @param pw
	 * @param em
	 * @param opw
	 * @return int
	 */
	public int modify(String m, String uid, String un, String pw, String em, String opw) {
		try {
			StringBuilder sb = new StringBuilder(512);
			sb.append("m=");
			sb.append(m);
			sb.append("&uid=");
			sb.append(uid);
			sb.append("&un=");
			sb.append(URLEncoder.encode(un, "utf-8"));
			sb.append("&pw=");
			sb.append(URLEncoder.encode(pw, "utf-8"));
			sb.append("&em=");
			sb.append(URLEncoder.encode(em, "utf-8"));
			sb.append("&opw=");
			sb.append(URLEncoder.encode(opw, "utf-8"));
			String sa = RSASignature.sign(sb.toString(), RSAKey.RSA_PRIVATE);
			sb.append("&signature=");
			sb.append(URLEncoder.encode(sa, "utf-8"));
			String result = execute(userCenterUrl, sb.toString(), true);
			try {
				return Integer.parseInt(result);
			} catch (Exception ex) {
				return FAIL;
			}
		} catch (IOException e) {
			return FAIL;
		} catch (Exception e) {
			return FAIL;
		}
	}

	/**
	 * 提交数据
	 * 
	 * @param strUrl
	 * @param reqBody
	 * @param isPost
	 * @return String
	 * @throws IOException
	 */
	private String execute(String strUrl, String reqBody, boolean isPost) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection connect = (HttpURLConnection) url.openConnection();
		connect.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		if (isPost && reqBody != null && reqBody.length() > 0) {
			String i = Integer.toString(reqBody.length());
			connect.setRequestProperty("Content-Length", i);
		}
		connect.setConnectTimeout(connectTimeout);
		connect.setReadTimeout(connectTimeout * 3);
		if (isPost)
			connect.setRequestMethod(POST);
		else
			connect.setRequestMethod(GET);
		connect.setDoOutput(true);
		connect.connect();
		if (isPost && reqBody != null && reqBody.length() > 0) {

			OutputStream ops = connect.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(ops, "utf-8");
			writer.write(reqBody);
			writer.flush();
			writer.close();
		}
		InputStream ips = connect.getInputStream();
		if (connect.getContentEncoding() != null) {
			String encode = connect.getContentEncoding().toLowerCase();
			if (encode.indexOf("gzip") > -1) {// 压缩流
				ips = new GZIPInputStream(ips);
			}
		}
		String respBody = "";
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
			}
			respBody = sb.toString();
		} catch (Exception e) {

		}
		connect.disconnect();
		return respBody;
	}

	public String getTokenCheckUrl() {
		return userCenterUrl;
	}

	public void setTokenCheckUrl(String tokenCheckUrl) {
		this.userCenterUrl = tokenCheckUrl;
	}
}
