/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.mangareader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import nl.reupload.animemanagermobile.R;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MangaView extends Activity {
	private MangaView act;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.manga_view);
        Intent intent = this.getIntent();
        String title = intent.getStringExtra("title");
        int chap = intent.getIntExtra("chapter", 0);
        final MangaReadManage mang = new MangaReadManage("/storage/sdcard0/.searchmanga/"+ title +"/" + chap + "/");
        
        Bitmap img = mang.loadImageFromExternal(getSearchManga(mang.getPoint())+".jpg", this);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mangaview);
        final ImageView iv = (ImageView) findViewById(R.id.mangaimage);
        iv.setImageBitmap(img);
        
        iv.setOnTouchListener(new ding());
        Button next = (Button) findViewById(R.id.nextbut);
        Button prev = (Button) findViewById(R.id.prevbut);
        next.setGravity(Gravity.TOP);
        prev.setGravity(Gravity.TOP);
        act = this;
        next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mang.nextImage();
		        Bitmap img = mang.loadImageFromExternal(getSearchManga(mang.getPoint())+".jpg", act);
		        if (img == null)
		        	mang.prevImage();
		        else {
			        iv.setImageBitmap(img);
			        iv.invalidate();
		        }
			}
		});
        prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mang.prevImage();
		        Bitmap img = mang.loadImageFromExternal(getSearchManga(mang.getPoint())+".jpg", act);
		        iv.setImageBitmap(img);
		        iv.invalidate();
			}
		});
    }
	
	public String getSearchManga(int i) {
		String file64 = null;
		if (i<10)
			file64 = new String(Base64.encode(("000" + i).getBytes(), Base64.NO_WRAP));
		else if (i>9 && i<100)
			file64 = new String(Base64.encode(("00" + i).getBytes(), Base64.NO_WRAP));
        Log.d("heh", file64);
        return file64;
	}
}
