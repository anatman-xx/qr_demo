package com.glassky.ui;

import com.glassky.main.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfirmAuthActivity extends Activity {
	private static final String TAG = "ConfirmAuthActivity";

	public static final String RESULT_CONFIRMATION = "confirmation";

	private Button confirmAuth;
	private Button rejectAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_auth);
		
		confirmAuth = (Button) findViewById(R.id.confirm_auth);
		rejectAuth = (Button) findViewById(R.id.reject_auth);
		
		confirmAuth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.v(TAG, "Confirm auth");

				Intent data = new Intent();
				data.putExtra(RESULT_CONFIRMATION, true);

				ConfirmAuthActivity.this.setResult(RESULT_OK, data);
				ConfirmAuthActivity.this.finish();
			}
		});
		
		rejectAuth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.v(TAG, "Reject auth");

				Intent data = new Intent();
				data.putExtra(RESULT_CONFIRMATION, false);

				ConfirmAuthActivity.this.setResult(RESULT_OK, data);
				ConfirmAuthActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.confirm_auth, menu);
		return true;
	}
}
