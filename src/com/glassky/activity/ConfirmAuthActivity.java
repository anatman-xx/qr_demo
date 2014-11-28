package com.glassky.activity;

import com.glassky.main.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfirmAuthActivity extends Activity {
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
				Intent data = new Intent();
				data.putExtra("confirmation", true);

				ConfirmAuthActivity.this.setResult(RESULT_OK, data);
				ConfirmAuthActivity.this.finish();
			}
		});
		
		rejectAuth.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent data = new Intent();
				data.putExtra("confirmation", false);

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
