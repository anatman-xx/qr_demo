package com.glassky.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class AuthSessionAsyncTask extends AsyncTask<Object, Integer, String> {
	private static final String LOG_TAG = "AuthSessionAsyncTask";
	
	private String clientId;
	private String accessToken;
	private Long uid;
	
	private String session;
	private Long timeStamp;
	private String signature;
	
	public AuthSessionAsyncTask(String clientId, String accessToken, Long uid, String session, Long timeStamp,
			String signature) {
		this.clientId = clientId;
		this.accessToken = accessToken;
		this.uid = uid;
		
		this.session = session;
		this.timeStamp = timeStamp;
		this.signature = signature;
	}

	@Override
	protected String doInBackground(Object... params) {
		try {
			HttpClient httpClient = AndroidHttpClient.newInstance("");
			String uri = new StringBuilder("https://ptlogin.4399.com/oauth2/qrAuthorize.do")
				.append("?client_id=").append(clientId)
				.append("&access_token=").append(accessToken)
				.append("&uid=").append(uid)
				.append("&session=").append(session)
				.append("&timestamp=").append(timeStamp)
				.append("&signature=").append(signature)
				.toString();
			
			Log.v(LOG_TAG, "Request URI:" + uri);
			
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			return EntityUtils.toString(httpResponse.getEntity());
		} catch (ClientProtocolException e) {
			Log.w(LOG_TAG, "Catch exception:" + e);
		} catch (IOException e) {
			Log.w(LOG_TAG, "Catch exception:" + e);
		}

		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
	}
}