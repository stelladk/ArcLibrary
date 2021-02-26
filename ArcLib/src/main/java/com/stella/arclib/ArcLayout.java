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

/**
 * Layout with customisable arc corners
 *
 * @see ArcShape
 *
 * @attr ref R.styleable#ArcLayout
 *
 * @attr ref R.styleable#ArcLayout_ArcType
 * @attr ref R.styleable#ArcLayout_TopLeftArc
 * @attr ref R.styleable#ArcLayout_TopRightArc
 * @attr ref R.styleable#ArcLayout_BottomLeftArc
 * @attr ref R.styleable#ArcLayout_BottomRightArc
 * @attr ref R.styleable#ArcLayout_OuterAxis
 * @attr ref R.styleable#ArcLayout_TopLeftOuterAxis
 * @attr ref R.styleable#ArcLayout_TopRightOuterAxis
 * @attr ref R.styleable#ArcLayout_BottomLeftOuterAxis
 * @attr ref R.styleable#ArcLayout_BottomRightOuterAxis
 * @attr ref R.styleable#ArcLayout_ArcRadius
 * @attr ref R.styleable#ArcLayout_TopLeftRadius
 * @attr ref R.styleable#ArcLayout_TopRightRadius
 * @attr ref R.styleable#ArcLayout_BottomLeftRadius
 * @attr ref R.styleable#ArcLayout_BottomRightRadius
 */
public class ArcLayout extends FrameLayout {
    private final static String TAG = "ArcLayout";

    private int layoutWidth, layoutHeight;

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;

    private int arcType, topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int outerAxis, topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;
    private float arcRadius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;

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
            //Default Values
            this.arcType = a.getInteger(R.styleable. ArcLayout_ArcType, ArcShape.NONE);
            this.outerAxis = a.getInteger(R.styleable.ArcLayout_OuterAxis, ArcShape.Y_AXIS);
            this.arcRadius = a.getDimension(R.styleable.ArcLayout_ArcRadius, -1);

            this.topLeftArc = a.getInteger(R.styleable.ArcLayout_TopLeftArc, arcType);
            this.topRightArc = a.getInteger(R.styleable.ArcLayout_TopRightArc, arcType);
            this.bottomLeftArc = a.getInteger(R.styleable.ArcLayout_BottomLeftArc, arcType);
            this.bottomRightArc = a.getInteger(R.styleable.ArcLayout_BottomRightArc, arcType);

            this.topLeftOuterAxis = a.getInteger(R.styleable.ArcLayout_TopLeftOuterAxis, outerAxis);
            this.topRightOuterAxis = a.getInteger(R.styleable.ArcLayout_TopRightOuterAxis, outerAxis);
            this.bottomLeftOuterAxis = a.getInteger(R.styleable.ArcLayout_BottomLeftOuterAxis, outerAxis);
            this.bottomRightOuterAxis = a.getInteger(R.styleable.ArcLayout_BottomRightOuterAxis, outerAxis);

            this.topLeftRadius = a.getDimension(R.styleable.ArcLayout_TopLeftRadius, arcRadius);
            this.topRightRadius = a.getDimension(R.styleable.ArcLayout_TopRightRadius, arcRadius);
            this.bottomLeftRadius = a.getDimension(R.styleable.ArcLayout_BottomLeftRadius, arcRadius);
            this.bottomRightRadius = a.getDimension(R.styleable.ArcLayout_BottomRightRadius, arcRadius);
        }finally {
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }

    /**
     * Called when the size of this view has changed.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        maskBitmap = null;

        int xpad = getPaddingLeft() + getPaddingRight();
        int ypad = getPaddingTop() + getPaddingBottom();
        layoutWidth = w - xpad;
        layoutHeight = h - ypad;

        super.onSizeChanged(layoutWidth, layoutHeight, oldw, oldh);
    }

    /**
     * Draws ArcLayout on the canvas
     * @param canvas to draw the layout into
     */
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
                topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis,
                topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        shape.draw(canvas, paint);

        return mask;
    }

}

