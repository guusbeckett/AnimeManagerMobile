/*AnimeManagerMobile is an android app developed to make the organisation of animelist easier to do on mobile devices Copyright (C) 2013 Jim van Abkoude



This program is free software; you can redistribute it and/or modify it under the terms of the Reupload Open Source Licence as punlished by Reupload; either version 0.1 of the License, or (at your option) any later version.



This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.



You should have received a copy of the Reupload Open Source Licence along with this library; if not, contact the distrubutor of this program*/
package nl.reupload.animemanagermobile.releasetracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class ReleaseTrackingService extends Service {
  private Looper mServiceLooper;
  private ServiceHandler mServiceHandler;
  private static Context ctx;

  // Handler that receives messages from the thread
  private final class ServiceHandler extends Handler {
      public ServiceHandler(Looper looper) {
          super(looper);
      }
      @Override
      public void handleMessage(Message msg) {
          // Normally we would do some work here, like download a file.
          // For our sample, we just sleep for 5 seconds.
    	  FeedParser parse = new FeedParser();
    	  while (true) {
	          long endTime = System.currentTimeMillis() + 10*60*1000;
	          while (System.currentTimeMillis() < endTime) {
	              synchronized (this) {
	                  try {
	                	  parse.test(ctx);
	                	  Log.d("lle", "service");
	                	  Thread.sleep(endTime - System.currentTimeMillis());
	                  } catch (Exception e) {
	                	  e.printStackTrace();
	                	  Log.e("AMM service", "the service reported failure");
	                  }
	              }
	          }
    	  }
          // Stop the service using the startId, so that we don't stop
          // the service in the middle of handling another job
          //stopSelf(msg.arg1);
    	 
      }
  }
  
  
  
  public void toastDone() {
	  Toast.makeText(this, "service looped", Toast.LENGTH_SHORT).show();
  }
  
  @Override
  public void onCreate() {
	  ctx = this;
	Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
    // Start up the thread running the service.  Note that we create a
    // separate thread because the service normally runs in the process's
    // main thread, which we don't want to block.  We also make it
    // background priority so CPU-intensive work will not disrupt our UI.
    HandlerThread thread = new HandlerThread("ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND);
    thread.start();
    
    // Get the HandlerThread's Looper and use it for our Handler 
    mServiceLooper = thread.getLooper();
    mServiceHandler = new ServiceHandler(mServiceLooper);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

      // For each start request, send a message to start a job and deliver the
      // start ID so we know which request we're stopping when we finish the job
      Message msg = mServiceHandler.obtainMessage();
      msg.arg1 = startId;
      mServiceHandler.sendMessage(msg);
      
      // If we get killed, after returning from here, restart
      return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
      // We don't provide binding, so return null
      return null;
  }
  
  @Override
  public void onDestroy() {
    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
  }
}