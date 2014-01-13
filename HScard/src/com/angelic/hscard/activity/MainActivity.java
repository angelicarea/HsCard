package com.angelic.hscard.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.angelic.hscard.R;
import com.angelic.hscard.constants.Constants;
import com.angelic.hscard.db.MyDbTest;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
    	//全屏显示
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	MyDbTest dbTest = new MyDbTest(this);
    	SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			// Log.d("debug", "第一次运行");
			editor.putBoolean("isFirstRun", false);
			editor.commit();
//			if(FileUtils.sdCardIsExist()){
//				FileUtils fileUtils = new FileUtils();
//				File file = fileUtils.creatSDDir(Constants.SdCardPath);
//				if(file.exists()){
//					CopyAssets();
//				}
//			}
		} else {
			// Log.d("debug", "不是第一次运行");
		}
		
    	new Handler().postDelayed(new Runnable(){

    		@Override
            public void run(){
                Intent intent = new Intent ();
                intent.setClass(MainActivity.this,Welcome.class);
//                if(FileUtils.sdCardIsExist()){
//    				FileUtils fileUtils = new FileUtils();
//    				File file = fileUtils.creatSDDir(Constants.SdCardPath);
//    				if(file.exists()){
//    					CopyAssets();
//    				}
//    			}
                startActivity(intent);
                finish();//结束本Activity
            }
        }, 2000);
	}

	private void CopyAssets() {
		// TODO Auto-generated method stub
		AssetManager assetManager = this.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("card");
            Log.v("文件长度是：", "+++" + files.length);
        } catch (IOException e) {

        }
        // 按照文件个数来挨个copy
        for (int i = 0; i < files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            File f = new File(Constants.DATA_PATH + files[i]);
            Bitmap bitmap = null;
            try {
                if (f.exists()) {
                    in = assetManager.open(Constants.CardPath + files[i]);
                    bitmap = BitmapFactory.decodeStream(in);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 168, 236, true);
                    
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
                    in = new ByteArrayInputStream(baos.toByteArray());  
                    out = new FileOutputStream(f);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                Log.v("异常", "if语句出异常了。");
            }
        }
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		// TODO Auto-generated method stub
		byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
	}
}