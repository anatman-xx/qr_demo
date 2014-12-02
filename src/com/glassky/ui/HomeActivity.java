package com.glassky.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassky.R;
import com.glassky.core.AuthSessionTask;
import com.glassky.core.ConfirmAuthTask;
import com.google.zxing.client.android.CaptureActivity;

public class HomeActivity extends Activity {
	private static final String TAG = "HomeActivity";
	
	private static final String clientId = "test";
	private static final String accessToken = "306de38c34ce488ff87f519c095417c1";
	private static final Long uid = 1225645781l;

	public static final int ACTIVITY_CAPTURE = 0;
	public static final int ACTIVITY_CONFIRM_AUTH = 1;
	
	public static final int TASK_AUTH_SESSION = 0;
	public static final int TASK_CONFIRM_AUTH = 1;
	
	public static final int DIALOG_INFO = 0;
	
	@SuppressLint("HandlerLeak")
	private final Handler HOME_ACTIVITY_HANDLER = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case TASK_AUTH_SESSION:
				Bundle data = message.getData();
				
				if (data == null) {
					return;
				}
				
				String rawResult = data.getString(AuthSessionTask.RESULT_SERVER_RESPONSE);
				
				if (rawResult == null || rawResult.length() == 0) {
					return;
				}
				
				try {
					JSONObject result = new JSONObject(rawResult);
					Integer code = (Integer) result.get("code");
					
					if (code != 300) {
						Log.e(TAG, "Auth session failed:" + rawResult);
						
						AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
						dialogBuilder.setMessage("授权失败")
								.setCancelable(false)
								.setPositiveButton("ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
//										dialog.dismiss();
									}
								});
						
						AlertDialog dialog = dialogBuilder.create();
						
						dialog.show();
						return;
					}
				} catch (Exception e) {
					Log.e(TAG, "Cache exception:" + e);
				}
				
				HomeActivity.this.startActivityForResult(new Intent(HomeActivity.this, ConfirmAuthActivity.class),
						ACTIVITY_CONFIRM_AUTH);
				
				break;
				
			case TASK_CONFIRM_AUTH:
				data = message.getData();
				
				if (data == null) {
					return;
				}

				rawResult = data.getString(AuthSessionTask.RESULT_SERVER_RESPONSE);
				
				if (rawResult == null || rawResult.length() == 0) {
					return;
				}

				try {
					JSONObject result = new JSONObject(rawResult);
					Integer code = (Integer) result.get("code");
					
					if (code == 300) {
						Log.v(TAG, "Auth session success");
						
						AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
						dialogBuilder.setMessage("授权成功")
								.setCancelable(false)
								.setPositiveButton("ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
//										dialog.dismiss();
									}
								});
						
						AlertDialog dialog = dialogBuilder.create();
						
						dialog.show();
						return;
					}
				} catch (Exception e) {
					Log.e(TAG, "Cache exception:" + e);
				}

				break;
				
			default:
				break;
			}
		}
	};

	private Button openCamera;
	private TextView scanResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		openCamera = (Button) findViewById(R.id.open_camera);
		scanResult = (TextView) findViewById(R.id.scan_result);

		openCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(HomeActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, ACTIVITY_CAPTURE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Log.v(TAG, "Request code:" + requestCode + " Result code:" + resultCode);

		if (requestCode == ACTIVITY_CAPTURE && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			
			onCaptureQrCode(result);
		} else if (requestCode == ACTIVITY_CONFIRM_AUTH && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			boolean result = bundle.getBoolean(ConfirmAuthActivity.RESULT_CONFIRMATION);

			onConfirmAuth(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private boolean onConfirmAuth(boolean confirm) {
		Log.v(TAG, "onConfirmAuth:" + confirm);
		
		if (confirm) {
			new Thread(new ConfirmAuthTask(HOME_ACTIVITY_HANDLER, clientId, accessToken, uid, session, timeStamp,
					signature)).start();
		}

		return true;
	}
	
	private String session = null;
	private Long timeStamp = null;
	private String signature = null;

	private boolean onCaptureQrCode(String code) {
		Log.v(TAG, "onCaptureQrCode:" + code);
		
		scanResult.setText(code);
		
		if (code == null || code.length() == 0) {
			return false;
		}
		
		code = code.trim();
		
		String[] data = code.split("\\|");
		
		if (data.length != 3) {
			return false;
		}
		
		session = data[0];
		timeStamp = Long.valueOf(data[1]);
		signature = data[2];

		new Thread(new AuthSessionTask(HOME_ACTIVITY_HANDLER, clientId, accessToken, uid,
				session, timeStamp, signature)).start();
		
		return true;
	}
}
