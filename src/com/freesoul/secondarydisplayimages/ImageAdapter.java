package com.freesoul.secondarydisplayimages;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mImagesPath;

	
	public ImageAdapter(Context c,List<String> images) {
	    //instantiate context
		mContext = c;
		mImagesPath=images;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImagesPath.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		
		if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }
		loadBitmap(mImagesPath.get(position),imageView);
		return imageView;
	}
	
	public void loadBitmap(String path, ImageView imageView) {
	    if (cancelPotentialWork(path, imageView)) {
	        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        Bitmap b=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.loading);
	        final AsyncDrawable asyncDrawable =
	                new AsyncDrawable(mContext.getResources(), b, task);
	        imageView.setImageDrawable(asyncDrawable);
	        task.execute(path);
	    }
	}
	
	public static boolean cancelPotentialWork(String path, ImageView imageView) {
	    final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

	    if (bitmapWorkerTask != null) {
	        final String bitmapPath = bitmapWorkerTask.path;
	        if (bitmapPath != path) {
	            // Cancel previous task
	            bitmapWorkerTask.cancel(true);
	        } else {
	            // The same work is already in progress
	            return false;
	        }
	    }
	    // No task associated with the ImageView, or an existing task was cancelled
	    return true;
	}
	
	public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		   if (imageView != null) {
		       final Drawable drawable = imageView.getDrawable();
		       if (drawable instanceof AsyncDrawable) {
		           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
		           return asyncDrawable.getBitmapWorkerTask();
		       }
		    }
		    return null;
	}

}
