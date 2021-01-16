package com.stella.arclib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class ArcLayout extends FrameLayout {
    private final static String TAG = "ArcLayout";

    private int layoutWidth, layoutHeight;

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;

    private int topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;

    public ArcLayout(@NonNull Context context) {
        super(context);
        init(context,null, 0);
    }

    public ArcLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs, 0);
    }

    public ArcLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcLayout, defStyleAttr, 0);
        try{
            this.topLeftArc = a.getInteger(R.styleable.ArcLayout_TopLeftArc, ArcShape.NONE);
            this.topRightArc = a.getInteger(R.styleable.ArcLayout_TopRightArc, ArcShape.NONE);
            this.bottomLeftArc = a.getInteger(R.styleable.ArcLayout_BottomLeftArc, ArcShape.NONE);
            this.bottomRightArc = a.getInteger(R.styleable.ArcLayout_BottomRightArc, ArcShape.NONE);

            this.topLeftOuterAxis = a.getInteger(R.styleable.ArcLayout_TopLeftOuterAxis, ArcShape.X_AXIS);
            this.topRightOuterAxis = a.getInteger(R.styleable.ArcLayout_TopRightOuterAxis, ArcShape.Y_AXIS);
            this.bottomLeftOuterAxis = a.getInteger(R.styleable.ArcLayout_BottomLeftOuterAxis, ArcShape.Y_AXIS);
            this.bottomRightOuterAxis = a.getInteger(R.styleable.ArcLayout_BottomRightOuterAxis, ArcShape.X_AXIS);
        }finally {
            a.recycle();
        }
        Log.d(TAG, "init: topLeftOuterAxis " + topLeftOuterAxis);
        Log.d(TAG, "init: topRightOuterAxis " + topRightOuterAxis);
        Log.d(TAG, "init: bottomLeftOuterAxis " + bottomLeftOuterAxis);
        Log.d(TAG, "init: bottomRightOuterAxis " + bottomRightOuterAxis);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);

        int xpad = getPaddingLeft() + getPaddingRight();
        int ypad = getPaddingTop() + getPaddingBottom();
        layoutWidth = w - xpad;
        layoutHeight = h - ypad;

        super.onSizeChanged(layoutWidth, layoutHeight, oldw, oldh);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas){
        Bitmap offscreenBitmap = Bitmap.createBitmap(layoutWidth, layoutHeight, Bitmap.Config.ARGB_8888);
        Canvas offscreenCanvas = new Canvas(offscreenBitmap);

        super.draw(offscreenCanvas);

        if(maskBitmap == null){
            maskBitmap = createMask(layoutWidth, layoutHeight);
        }

        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap createMask(int width, int height){
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setXfermode(null);
        paint.setColor(Color.WHITE);

        canvas.drawRect(0, 0, width, height, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        ArcShape shape = new ArcShape(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
                topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis);
        shape.draw(canvas, paint);

        return mask;
    }

}

