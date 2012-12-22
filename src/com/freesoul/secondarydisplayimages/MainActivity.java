package com.freesoul.secondarydisplayimages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener {

	//adapter for gallery view
	private ImageAdapter mImageAdapter;
	private static Handler handler;
	private List<String> mImagesPath;
	MainActivity screen;
	private ProgressDialog m_ProgressDialog = null;
	private MyPresentation mPresentation = null;
	
	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		screen=this;
		m_ProgressDialog = ProgressDialog.show(MainActivity.this,    
    			"Please Wait", "Loading Images", true);
		DisplayManager dm = 
            (DisplayManager) getSystemService(DISPLAY_SERVICE);
        if (dm != null)
        {
            Display[] displays = 
                dm.getDisplays(
                    DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if(displays.length>0)
            {
                mPresentation = new MyPresentation(this, displays[0]);
                mPresentation.show();
            }
        }
		handler = new Handler()
        {
            @Override
            public void handleMessage( Message message )
            {

            	mImageAdapter = new ImageAdapter(screen,mImagesPath);
        		//set the gallery adapter
        		
        		mGridView.setAdapter(mImageAdapter);
        		mGridView.setOnItemClickListener(screen);
        		m_ProgressDialog.dismiss();

        		m_ProgressDialog = null;
            }
	            	
        };
		
		LoadFileNames();
		
		
		
		mGridView=(GridView)findViewById(R.id.gridViewGallery);
		//create a new adapter
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	System.gc();
	    	this.finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
    
    public void LoadFileNames()
	{
		new Thread( new Runnable()
		   {
		       public void run()
		       {
		    	   if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			   		{
			           	Log.v("MainActivity","MEDIA_MOUNTED");
			           	mImagesPath=new ArrayList<String>();
			           	File sdCardRoot = Environment.getExternalStorageDirectory();
			           	Log.v("MainActivity",sdCardRoot.getPath());
		               File yourDir = new File(sdCardRoot, "/DCIM/Camera");
		               Log.v("MainActivity",yourDir.getPath());
		               String name;
		               for (File f : yourDir.listFiles()) {
		                   if (f.isFile())
		                   {
		                       	name = f.getName();
		                       	if(name.endsWith(".jpg"))
		                       	{
			                       	File folder=new File(yourDir,name);
		           					String path=Uri.fromFile(folder).getPath();
		           					Log.v("MainActivity",path);
		           					mImagesPath.add(path);
		                       	}
		                   }              
	               }
	               Log.v("MainActivity","Images Count: "+mImagesPath.size());
		           Message message = handler.obtainMessage( 1, "Done" );
		           handler.sendMessage( message );
		       }
		     }}).start();
		}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		String imagePath=mImagesPath.get(position);
		if(mPresentation!=null)
		{
			Bitmap bmp=BitmapFactory.decodeFile(imagePath);
			if(bmp!=null)
				mPresentation.ShowImage(bmp);
		}
	}

}
