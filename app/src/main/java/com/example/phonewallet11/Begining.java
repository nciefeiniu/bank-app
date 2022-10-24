package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class Begining extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begining);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 4000);

	      /* ImageView iv=(ImageView) findViewById(R.id.beginbg);


	         SharedPreferences sp=getSharedPreferences("beginbg",MODE_PRIVATE);
	         String pic=sp.getString("pic", "novalue");

	       InputStream is;

	       try {
			is = getAssets().open(pic);
			Bitmap bitmap=BitmapFactory.decodeStream(is);
		    iv.setImageBitmap(bitmap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/




    }



    class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(Begining.this,MainActivity.class));
            Begining.this.finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.begining, menu);
        return true;
    }
}
