package com.stella.arclib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

class ArcShape extends Shape {
    private final static String TAG = "ArcShape";
    protected final static int OUTER = 1;
    protected final static int NONE = 0;
    protected final static int INNER = -1;

    protected final static int X_AXIS = 0;
    protected final static int Y_AXIS = 1;

    private int topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;

    private int left, top, right, bottom;
    private int width, height;
    private int xCenter, yCenter;

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc) {
        this.topLeftArc = topLeftArc;
        this.topRightArc = topRightArc;
        this.bottomLeftArc = bottomLeftArc;
        this.bottomRightArc = bottomRightArc;
        this.topLeftOuterAxis = X_AXIS;
        this.topRightOuterAxis = Y_AXIS;
        this.bottomLeftOuterAxis = Y_AXIS;
        this.bottomRightOuterAxis = X_AXIS;
    }

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc, int topLeftOuterAxis, int topRightOuterAxis, int bottomLeftOuterAxis, int bottomRightOuterAxis) {
        this.topLeftArc = topLeftArc;
        this.topRightArc = topRightArc;
        this.bottomLeftArc = bottomLeftArc;
        this.bottomRightArc = bottomRightArc;
        this.topLeftOuterAxis = topLeftOuterAxis;
        this.topRightOuterAxis = topRightOuterAxis;
        this.bottomLeftOuterAxis = bottomLeftOuterAxis;
        this.bottomRightOuterAxis = bottomRightOuterAxis;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //Fix sizes
        factorizeSize(canvas);

        Log.d(TAG, "draw: topLeftOuterAxis " + topLeftOuterAxis);
        Log.d(TAG, "draw: topRightOuterAxis " + topRightOuterAxis);
        Log.d(TAG, "draw: bottomLeftOuterAxis " + bottomLeftOuterAxis);
        Log.d(TAG, "draw: bottomRightOuterAxis " + bottomRightOuterAxis);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        //Begin shape at (xCenter,top)
        path.moveTo(xCenter,top);

        //Top Left Corner
        if(topLeftArc == NONE){
            path.lineTo(left, top);
            path.lineTo(left, yCenter);
        }else if(topLeftArc == INNER){
            path.arcTo(new RectF(left, top, xCenter, yCenter), -90, -90);
        }else if(topLeftArc == OUTER){
            if(topLeftOuterAxis == X_AXIS){
                path.lineTo(left-width/2, top);
                path.arcTo(new RectF(left-width/2, top, left, yCenter), -90, 90);
            }else if(topLeftOuterAxis == Y_AXIS){
                path.arcTo(new RectF(left, top-height/2, xCenter, top), 90, 90);
                path.lineTo(left, yCenter);
            }
        } //end at (left, yCenter)

        //Bottom Left Corner
        if(bottomLeftArc == NONE){
            path.lineTo(left, bottom);
            path.lineTo(xCenter, bottom);
        }else if(bottomLeftArc == INNER){
            path.arcTo(new RectF(left, yCenter, xCenter, bottom), 180, -90);
        }else if(bottomLeftArc == OUTER){
            if(bottomLeftOuterAxis == Y_AXIS){
                path.lineTo(left, bottom+height/2);
                path.arcTo(new RectF(left, bottom, xCenter, bottom+height/2), 180, 90);
            }else if(bottomLeftOuterAxis == X_AXIS){
                path.arcTo(new RectF(left-width/2, yCenter, left, bottom), 0, 90);
                path.lineTo(xCenter, bottom);
            }
        }//end at (xCenter, bottom)

        //Bottom Right Corner
        if(bottomRightArc == NONE){
            path.lineTo(right, bottom);
            path.lineTo(right, yCenter);
        }else if(bottomRightArc == INNER){
            path.arcTo(new RectF(xCenter, yCenter, right, bottom), 90, -90);
        }else if(bottomRightArc == OUTER){
            if(bottomRightOuterAxis == X_AXIS){
                path.lineTo(right+width/2, bottom);
                path.arcTo(new RectF(right, yCenter, right+width/2, bottom), 90, 90);
            }else if(bottomRightOuterAxis == Y_AXIS){
                path.arcTo(new RectF(xCenter, bottom, right, bottom+height/2), -90, 90);
                path.lineTo(right, yCenter);
            }
        }//end at (right, yCenter)

        //Top Right Corner
        if(topRightArc == NONE){
            path.lineTo(right, top);
            path.lineTo(xCenter, top);
        }else if(topRightArc == INNER){
            path.arcTo(new RectF(xCenter, top, right, yCenter), 0, -90);
        }else if(topRightArc == OUTER){
            if(topRightOuterAxis == Y_AXIS){
                path.lineTo(right, top-height/2);
                path.arcTo(new RectF(xCenter, top-height/2, right, top), 0, 90);
            }else if(topRightOuterAxis == X_AXIS){
                path.arcTo(new RectF(right, top, right+width/2, yCenter), 180, 90);
                path.lineTo(xCenter, top);
            }
        }//end at (xCenter, top)

        path.close();

        canvas.drawPath(path, paint);
    }

    private void factorizeSize(Canvas canvas){
        int viewWidth = canvas.getWidth();
        int viewHeight = canvas.getHeight();

        left = viewWidth/4;
        top = viewHeight/4;
        right = viewWidth * 3/4;
        bottom = viewHeight * 3/4;

        if((topLeftArc != OUTER || topLeftOuterAxis != X_AXIS) && (bottomLeftArc != OUTER || bottomLeftOuterAxis != X_AXIS)){ left = 0;}
        if((topRightArc != OUTER || topRightOuterAxis != Y_AXIS) && (topLeftArc != OUTER || topLeftOuterAxis != Y_AXIS)) top = 0;
        if((bottomLeftArc != OUTER || bottomLeftOuterAxis != Y_AXIS) && (bottomRightArc != OUTER || bottomRightOuterAxis != Y_AXIS)) bottom = viewHeight;
        if((bottomRightArc != OUTER || bottomRightOuterAxis != X_AXIS) && (topRightArc != OUTER || topRightOuterAxis != X_AXIS)) right = viewWidth;

        width = right - left;
        height = bottom - top;

        xCenter = left+width/2;
        yCenter = top+height/2;
    }

}

