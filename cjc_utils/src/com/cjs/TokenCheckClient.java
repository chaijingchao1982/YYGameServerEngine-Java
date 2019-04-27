package com.cjs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;

/**
 * 令牌验证类
 * 
 * @author JasonChan
 * @date 2013-6-3
 */
public class TokenCheckClient {
	public static final java.lang.String GET = "GET";
	public static final java.lang.String POST = "POST";
	public static final java.lang.String PUT = "PUT";
	public static final java.lang.String DELETE = "DELETE";
	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";
	public static final int connectTimeout = 10000;// 连接超时时间
	private String tokenCheckUrl;

	public TokenCheckClient() {

	}

	public TokenCheckClient(String tokenCheckUrl) {
		this.tokenCheckUrl = tokenCheckUrl;
	}

	/**
	 * token验证
	 * 
	 * @param uid
	 * @param token
	 * @return 0成功；1网络连接失败；2验证失败;3未知错误
	 */
	public int tokenCheck(String uid, String token, int sid) {
		try {
			StringBuilder sb = new StringBuilder(256);
			sb.append("uid=");
			sb.append(uid);
			sb.append("&token=");
			sb.append(URLEncoder.encode(token, "utf-8"));
			sb.append("&sid=");
			sb.append(sid);
			String sa = RSASignature.sign(sb.toString(), RSAKey.RSA_PRIVATE);
			sb.append("&signature=");
			sb.append(URLEncoder.encode(sa, "utf-8"));
			String result = execute(tokenCheckUrl, sb.toString(), true);
			if (result.equals(SUCCESS))
				return 0;
			else
				return 2;
		} catch (IOException e) {
			return 1;
		} catch (Exception e) {
			return 3;
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
			OutputStreamWriter writer = new OutputStreamWriter(ops);
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
		return tokenCheckUrl;
	}

	public void setTokenCheckUrl(String tokenCheckUrl) {
		this.tokenCheckUrl = tokenCheckUrl;
	}
}
