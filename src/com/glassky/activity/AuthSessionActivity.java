package com.glassky.activity;

import com.glassky.main.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AuthSessionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_session);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.auth_session, menu);
		return true;
	}
}
