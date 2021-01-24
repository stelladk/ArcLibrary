package com.stella.arclib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;

class ArcImageView extends ArcLayout {
    private final static String TAG = "ArcImageView";
    int deviceWidth;
    int src;

    public ArcImageView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ArcImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ArcImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.arc_image_view, this);
        }

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcImageView, defStyleAttr, 0);
        try{
            this.src = a.getResourceId(0, 0);
        }finally {
            a.recycle();
        }

        ImageView arcImage = findViewById(R.id.arc_image);
        Log.d(TAG, "init: arcImage " + arcImage);
        Drawable drawable = getResources().getDrawable(src);
        arcImage.setImageDrawable(drawable);
    }

}
