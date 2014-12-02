package com.glassky.core;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.glassky.ui.HomeActivity;

public class AuthSessionTask implements Runnable {
	private static final String TAG = "AuthSessionTask";

	public static final String RESULT_SERVER_RESPONSE = "server_response";
	
	private String clientId;
	private String accessToken;
	private Long uid;
	
	private String session;
	private Long timeStamp;
	private String signature;
	
	private Handler handler;
	
	public AuthSessionTask(Handler handler, String clientId, String accessToken, Long uid, String session,
			Long timeStamp, String signature) {
		this.handler = handler;
		
		this.clientId = clientId;
		this.accessToken = accessToken;
		this.uid = uid;
		
		this.session = session;
		this.timeStamp = timeStamp;
		this.signature = signature;
	}

	@Override
	public void run() {
		String result = null;

		AndroidHttpClient httpClient = null;
		try {
			httpClient = AndroidHttpClient.newInstance("");
			String uri = new StringBuilder("https://ptlogin.4399.com/oauth2/qrAuthSession.do")
				.append("?client_id=").append(clientId)
				.append("&access_token=").append(accessToken)
				.append("&uid=").append(uid)
				.append("&session=").append(session)
				.append("&timestamp=").append(timeStamp)
				.append("&signature=").append(signature)
				.toString();
			
			Log.v(TAG, "Request URI:" + uri);
			
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			result = EntityUtils.toString(httpResponse.getEntity());
		} catch (ClientProtocolException e) {
			Log.e(TAG, "Catch exception:" + e);
		} catch (IOException e) {
			Log.e(TAG, "Catch exception:" + e);
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
		
		Log.v(TAG, "Response data:" + result);

		if (result == null || result.length() == 0) {
			handler.sendEmptyMessage(HomeActivity.TASK_AUTH_SESSION);
			return;
		}
		
		Bundle bundle = new Bundle();
		bundle.putString(RESULT_SERVER_RESPONSE, result);
		
		Message message = new Message();
		message.setData(bundle);
		message.what = HomeActivity.TASK_AUTH_SESSION;
		
		handler.sendMessage(message);
	}
}