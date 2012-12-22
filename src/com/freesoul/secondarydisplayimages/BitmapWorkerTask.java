package com.freesoul.secondarydisplayimages;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	private final WeakReference imageViewReference;
	public String path;
	
	public BitmapWorkerTask(ImageView imageView)
	{
		imageViewReference = new WeakReference(imageView);
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap=null;
		path=params[0];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds=true;
		int reqHeight=125;
		int reqWidth=100;
		BitmapFactory.decodeFile(path,options);
		
		options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
		options.inJustDecodeBounds = false;
		bitmap=BitmapFactory.decodeFile(path,options);
		return bitmap;
	}
	
	@Override
    protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = (ImageView) imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask =
                    ImageAdapter.getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
	
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

}
