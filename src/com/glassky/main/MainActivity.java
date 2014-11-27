package com.glassky.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassky.task.AuthSessionAsyncTask;
import com.google.zxing.client.android.CaptureActivity;

public class MainActivity extends Activity {
	private static final int REQUEST_CAPTURE_QRCODE = 0;

	private static final String LOG_TAG = "MainActivity";
	
	private static final String clientId = "test";
	private static final String accessToken = "306de38c34ce488ff87f519c095417c1";
	private static final Long uid = 1225645781l;

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
				Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, REQUEST_CAPTURE_QRCODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CAPTURE_QRCODE && resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			
			scanResult.setText(result);
			
			onCaptureQrCode(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private boolean onCaptureQrCode(String code) {
		Log.v(LOG_TAG, "Captured QrCode:" + code);
		
		if (code == null || code.length() == 0) {
			return false;
		}
		
		code = code.trim();
		
		String[] data = code.split("\\|");
		
		if (data.length != 3) {
			return false;
		}
		
		String session = data[0];
		Long timeStamp = Long.valueOf(data[1]);
		String signature = data[2];
		
		AuthSessionAsyncTask authAsyncTask = new AuthSessionAsyncTask(clientId, accessToken, uid, session, timeStamp,
				signature);
		authAsyncTask.execute();
		
		return true;
	}
}
