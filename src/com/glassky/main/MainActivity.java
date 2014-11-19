package com.glassky.main;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button openCamera;
	private TextView scanResult;
	private EditText inputContent;
	private Button genCode;
	private ImageView codeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i("MainActivity.onCreate", "onCreate event");

		openCamera = (Button) findViewById(R.id.openCamera);
		scanResult = (TextView) findViewById(R.id.scanResult);
		inputContent = (EditText) findViewById(R.id.inputContent);
		genCode = (Button) findViewById(R.id.genCode);
		codeImg = (ImageView) findViewById(R.id.codeImg);

		openCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});

		genCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = inputContent.getText().toString();
				if (null != text && !text.equals("")) {
					try {
						QRCodeWriter writer = new QRCodeWriter();
						Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
						hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
						BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
						int[] pixels = new int[300 * 300];
						for (int y = 0; y < 300; y++) {
							for (int x = 0; x < 300; x++) {
								if (bitMatrix.get(x, y)) {
									pixels[y * 300 + x] = 0xff000000;
								} else {
									pixels[y * 300 + x] = 0xffffffff;
								}

							}
						}
						Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
						bitmap.setPixels(pixels, 0, 300, 0, 0, 300, 300);

						codeImg.setImageBitmap(bitmap);

					} catch (WriterException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			scanResult.setText(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
