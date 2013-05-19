package jim.reupload.nl.animemanagermobile.mangareader;

import jim.reupload.nl.animemanagermobile.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
        final MangaReadManage mang = new MangaReadManage("/storage/sdcard0/.searchmanga/Waratte Sotomura San/1/");
        String file64 = new String(Base64.encode(("000" + mang.getPoint()).getBytes(), Base64.NO_WRAP));
        Log.d("heh", file64);
        Bitmap img = mang.loadImageFromExternal(file64+".jpg", this);
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
				String file64 = new String(Base64.encode(("000" + mang.getPoint()).getBytes(), Base64.NO_WRAP));
		        Bitmap img = mang.loadImageFromExternal(file64+".jpg", act);
		        iv.setImageBitmap(img);
		        iv.invalidate();
			}
		});
        prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mang.prevImage();
				String file64 = new String(Base64.encode(("000" + mang.getPoint()).getBytes(), Base64.NO_WRAP));
		        Bitmap img = mang.loadImageFromExternal(file64+".jpg", act);
		        iv.setImageBitmap(img);
		        iv.invalidate();
			}
		});
    }
}
