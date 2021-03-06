package com.stelladk.arclib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class ArcButton extends androidx.appcompat.widget.AppCompatButton {
    private final static String TAG = "ArcButton";

    private int layoutWidth, layoutHeight;

    private Bitmap maskBitmap;
    private Paint paint, maskPaint;

    private int arcType, topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int outerAxis, topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;
    private float arcRadius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;

    private boolean stroke;
    private int strokeColor;
    private float strokeWidth;

    public ArcButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ArcButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ArcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcButton, defStyleAttr, 0);
        try{
            //Default Values
            this.arcType = a.getInteger(R.styleable. ArcButton_ArcType, ArcShape.NONE);
            this.outerAxis = a.getInteger(R.styleable.ArcButton_OuterAxis, ArcShape.Y_AXIS);
            this.arcRadius = a.getDimension(R.styleable.ArcButton_ArcRadius, -1);

            this.topLeftArc = a.getInteger(R.styleable.ArcButton_TopLeftArc, arcType);
            this.topRightArc = a.getInteger(R.styleable.ArcButton_TopRightArc, arcType);
            this.bottomLeftArc = a.getInteger(R.styleable.ArcButton_BottomLeftArc, arcType);
            this.bottomRightArc = a.getInteger(R.styleable.ArcButton_BottomRightArc, arcType);

            this.topLeftOuterAxis = a.getInteger(R.styleable.ArcButton_TopLeftOuterAxis, outerAxis);
            this.topRightOuterAxis = a.getInteger(R.styleable.ArcButton_TopRightOuterAxis, outerAxis);
            this.bottomLeftOuterAxis = a.getInteger(R.styleable.ArcButton_BottomLeftOuterAxis, outerAxis);
            this.bottomRightOuterAxis = a.getInteger(R.styleable.ArcButton_BottomRightOuterAxis, outerAxis);

            this.topLeftRadius = a.getDimension(R.styleable.ArcButton_TopLeftRadius, arcRadius);
            this.topRightRadius = a.getDimension(R.styleable.ArcButton_TopRightRadius, arcRadius);
            this.bottomLeftRadius = a.getDimension(R.styleable.ArcButton_BottomLeftRadius, arcRadius);
            this.bottomRightRadius = a.getDimension(R.styleable.ArcButton_BottomRightRadius, arcRadius);

            this.stroke = a.getBoolean(R.styleable.ArcButton_Stroke, false);
            this.strokeColor = a.getColor(R.styleable.ArcButton_StrokeColor, Color.WHITE);
            this.strokeWidth = a.getDimension(R.styleable.ArcButton_StrokeWidth, 10);

            int resId = a.getResourceId(R.styleable.ArcButton_background, Color.GRAY);
            Drawable drawable = getBackgroundAttr(getContext(), a, R.styleable.ArcButton_background, resId);
            setBackgroundDrawable(drawable);
        }finally {
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);
    }

    private Drawable getBackgroundAttr(Context context, TypedArray typed, int index, int resId) {
        TypedValue colorValue = new TypedValue();
        if(!typed.getValue(index, colorValue)){
            return  new ColorDrawable(Color.GRAY);
        }else if(colorValue.type == TypedValue.TYPE_STRING){
            return ContextCompat.getDrawable(context, resId);
        }else{
            return new ColorDrawable(colorValue.data);
        }
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
     * Draws ArcButton on the canvas
     * @param canvas to draw the button into
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas){
        layoutWidth = getWidth();
        layoutHeight = getHeight();
        Bitmap offscreenBitmap = Bitmap.createBitmap(layoutWidth, layoutHeight, Bitmap.Config.ARGB_8888);
        Canvas offscreenCanvas = new Canvas(offscreenBitmap);
        super.draw(offscreenCanvas);

        if(maskBitmap == null){
            maskBitmap = createMask(layoutWidth, layoutHeight);
        }

        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);

        //Stroke
        if(stroke){
            paint.setXfermode(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(strokeColor);
            ArcShape shape = new ArcShape(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
                    topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis,
                    topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
            shape.draw(canvas, paint);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap createMask(int width, int height){
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
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
