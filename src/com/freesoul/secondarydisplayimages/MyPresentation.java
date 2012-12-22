package com.freesoul.secondarydisplayimages;


import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

public class MyPresentation extends Presentation{

	public MyPresentation(Context outerContext, Display display) {
		super(outerContext, display);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary_layout);
    }
	
	public void ShowImage(Bitmap bmp)
	{
		ImageView mImageView=(ImageView)findViewById(R.id.imageView1);
		mImageView.setImageBitmap(bmp);
	}

}
