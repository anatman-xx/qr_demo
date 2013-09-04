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
		
		openCamera=(Button)findViewById(R.id.openCamera);
		scanResult=(TextView)findViewById(R.id.scanResult);
		inputContent=(EditText)findViewById(R.id.inputContent);
		genCode=(Button)findViewById(R.id.genCode);
		codeImg=(ImageView)findViewById(R.id.codeImg);
		
		
		//������ͷ��ɨ���ά��
		openCamera.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//��ɨ�����ɨ����������ά��  
                Intent openCameraIntent = new Intent(MainActivity.this,CaptureActivity.class);  
                startActivityForResult(openCameraIntent, 0); 
			}			
		});
		
		//���ɶ�ά��
		genCode.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String text=inputContent.getText().toString();
				if(null!=text && !text.equals("")){
					try {
						// ʵ������ά�����  
			            QRCodeWriter writer = new QRCodeWriter();  
			            // ��һ��map�����������  
			            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
			            // �����ַ���Ϊ��utf��8��  
			            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
			            /*  
			             * ��һ��������������ı� 
			             * �ڶ�����������������ʽ������ά�� 
			             * ��������������� 
			             * ���ĸ��������߶� 
			             * �����������map����������� 
			             */			            
						BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,  
						        300, 300, hints);
						// �����ر�����������  
			            int[] pixels = new int[300 * 300];  
			            for (int y = 0; y < 300; y++) {  
			                for (int x = 0; x < 300; x++) {  
			                    if (bitMatrix.get(x, y)) {// ��ά��ڵ�  
			                        pixels[y * 300 + x] = 0xff000000;  
			                    } else {// ��ά�뱳����ɫ  
			                        pixels[y * 300 + x] = 0xffffffff;  
			                    }  
			  
			                }  
			            } 
			            // ����λͼ  
			            Bitmap bitmap = Bitmap.createBitmap(300, 300,  
			                    Bitmap.Config.ARGB_8888); 
			            /*  
			             * ��һ�����������λͼ���������� 
			             * �ڶ�����������һ����ɫ�����������ض�ȡ 
			             * ���������������صķ��� 
			             * ���ĸ����������x���� 
			             * ��������������y���� 
			             * �������������� 
			             * ���߸��������� 
			             */  
			            bitmap.setPixels(pixels, 0, 300, 0, 0, 300, 300);
			            
			            //����ͼ��
			            codeImg.setImageBitmap(bitmap);
			            
					} catch (WriterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}			
		});
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        //����ɨ�������ڽ�������ʾ��  
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
