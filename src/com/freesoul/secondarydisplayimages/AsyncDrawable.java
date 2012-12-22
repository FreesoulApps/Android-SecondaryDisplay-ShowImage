package com.freesoul.secondarydisplayimages;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AsyncDrawable extends BitmapDrawable {
	private final WeakReference bitmapWorkerTaskReference;
	
	public AsyncDrawable(Resources res, Bitmap bitmap,
            BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference =
            new WeakReference(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
    }
}
