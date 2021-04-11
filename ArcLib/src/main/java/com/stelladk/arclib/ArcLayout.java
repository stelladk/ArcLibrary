package com.stelladk.arclib;

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
    private ArcShape shape;
    private Paint paint, maskPaint;

    private int arcType, topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int outerAxis, topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;
    private float arcRadius, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;

    private boolean stroke;
    private int strokeColor;
    private float strokeWidth;

    private boolean shadow;
    private int shadowColor;
    private final float shadowRadius = 20;
    private float elevation, shadowDx = 0;

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

            this.stroke = a.getBoolean(R.styleable.ArcLayout_Stroke, false);
            this.strokeColor = a.getColor(R.styleable.ArcLayout_StrokeColor, Color.WHITE);
            this.strokeWidth = a.getDimension(R.styleable.ArcLayout_StrokeWidth, 10);

            this.shadow = a.getBoolean(R.styleable.ArcLayout_Shadow, false);
            this.shadowColor = a.getColor(R.styleable.ArcLayout_ShadowColor, Color.GRAY);
            this.elevation = a.getDimension(R.styleable.ArcLayout_elevation, 0);
        }finally {
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setWillNotDraw(false);

        createShape();
    }

    /**
     * Set the top left corner arc type
     * @param arc value that determines arc type
     */
    public void setTopLeftArcType(int arc){
        if(arc == ArcShape.NONE) this.topLeftArc = arc;
        if(arc <= ArcShape.INNER) this.topLeftArc = ArcShape.INNER;
        if(arc >= ArcShape.OUTER) this.topLeftArc = ArcShape.OUTER;
    }

    /**
     * Set the top right corner arc type
     * @param arc value that determines arc type
     */
    public void setTopRightArc(int arc){
        if(arc == ArcShape.NONE) this.topRightArc = arc;
        if(arc <= ArcShape.INNER) this.topRightArc = ArcShape.INNER;
        if(arc >= ArcShape.OUTER) this.topRightArc = ArcShape.OUTER;
    }

    /**
     * Set the bottom left corner arc type
     * @param arc value that determines arc type
     */
    public void setBottomLeftArc(int arc){
        if(arc == ArcShape.NONE) this.bottomLeftArc = arc;
        if(arc <= ArcShape.INNER) this.bottomLeftArc = ArcShape.INNER;
        if(arc >= ArcShape.OUTER) this.bottomLeftArc = ArcShape.OUTER;
    }

    /**
     * Set the bottom right corner arc type
     * @param arc value that determines arc type
     */
    public void setBottomRightArc(int arc){
        if(arc == ArcShape.NONE) this.bottomRightArc = arc;
        if(arc <= ArcShape.INNER) this.bottomRightArc = ArcShape.INNER;
        if(arc >= ArcShape.OUTER) this.bottomRightArc = ArcShape.OUTER;
    }

    /**
     * Set the top left axis for outer arcs
     * @param axis outer arc axis
     */
    public void setTopLeftOuterAxis(int axis){
        if(axis <= ArcShape.X_AXIS) this.topLeftOuterAxis = ArcShape.X_AXIS;
        if(axis >= ArcShape.Y_AXIS) this.topLeftOuterAxis = ArcShape.Y_AXIS;
    }

    /**
     * Set the top right axis for outer arcs
     * @param axis outer arc axis
     */
    public void setTopRightOuterAxis(int axis){
        if(axis <= ArcShape.X_AXIS) this.topRightOuterAxis = ArcShape.X_AXIS;
        if(axis >= ArcShape.Y_AXIS) this.topRightOuterAxis = ArcShape.Y_AXIS;
    }

    /**
     * Set the bottom left axis for outer arcs
     * @param axis outer arc axis
     */
    public void setBottomLeftOuterAxis(int axis){
        if(axis <= ArcShape.X_AXIS) this.bottomLeftOuterAxis = ArcShape.X_AXIS;
        if(axis >= ArcShape.Y_AXIS) this.bottomLeftOuterAxis = ArcShape.Y_AXIS;
    }

    /**
     * Set the bottom right axis for outer arcs
     * @param axis outer arc axis
     */
    public void setBottomRightOuterAxis(int axis){
        if(axis <= ArcShape.X_AXIS) this.bottomRightOuterAxis = ArcShape.X_AXIS;
        if(axis >= ArcShape.Y_AXIS) this.bottomRightOuterAxis = ArcShape.Y_AXIS;
    }

    /**
     * Set the top left arc radius
     * @param radius arc radius
     */
    public void setTopLeftRadius(float radius){
        this.topLeftRadius = radius;
    }

    /**
     * Set the top right arc radius
     * @param radius arc radius
     */
    public void setTopRightRadius(float radius){
        this.topRightRadius = radius;
    }

    /**
     * Set the bottom left arc radius
     * @param radius arc radius
     */
    public void setBottomLeftRadius(float radius){
        this.bottomLeftRadius = radius;
    }

    /**
     * Set the bottom right arc radius
     * @param radius arc radius
     */
    public void setBottomRightRadius(float radius){
        this.bottomRightRadius = radius;
    }

    /**
     * Add stroke to layout
     * @param stroke if true add stroke
     */
    public void setStroke(boolean stroke){
        this.stroke = stroke;
    }

    /**
     * Set stroke color
     * @param color stroke color
     */
    public void setStrokeColor(int color){
        this.strokeColor = color;
    }

    /**
     * Set stroke width
     * @param width stroke width
     */
    public void setStrokeWidth(float width){
        this.strokeWidth = width;
    }

    /**
     * Add shadow to layout
     * @param shadow if true add shadow
     */
    public void setShadow(boolean shadow){
        this.shadow = shadow;
    }

    /**
     * Add elevation to layout
     * @param elevation shadow elevation
     */
    public void setElevation(float elevation){
        this.elevation = elevation;
    }

    /**
     * Set shadow color
     * @param color shadow color
     */
    public void setShadowColor(int color){
        this.shadowColor = color;
    }

    /**
     * Redraw the ArcLayout
     * Used to change the arcs in runtime
     */
    public void redraw(){
        maskBitmap = null;
        this.invalidate();
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
            createShape();
            maskBitmap = createMask(layoutWidth, layoutHeight);
        }

        if(shadow){
            Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            shadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            shadowPaint.setShadowLayer(shadowRadius, shadowDx, elevation, shadowColor);
            shape.draw(canvas, shadowPaint);
        }

        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint);
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint);

        //Stroke
        if(stroke){
            paint.setXfermode(null);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(strokeColor);

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

//        createShape();
//        ArcShape shape = new ArcShape(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
//                topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis,
//                topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
        shape.draw(canvas, paint);

        return mask;
    }

    private void createShape(){
        shape = new ArcShape(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
                topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis,
                topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius,
                shadowRadius, shadowDx, elevation);
    }

}

