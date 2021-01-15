package com.stella.arclib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;

import androidx.annotation.RequiresApi;

class ArcShape extends Shape {
    private final static String TAG = "ArcShape";

    private final static int OUTER = 1;
    private final static int NONE = 0;
    private final static int INNER = -1;

    private int topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;

    private int left, top, right, bottom;
    private int width, height;
    private int xCenter, yCenter;

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc) {
        this.topLeftArc = topLeftArc;
        this.topRightArc = topRightArc;
        this.bottomLeftArc = bottomLeftArc;
        this.bottomRightArc = bottomRightArc;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas, Paint paint) {
        //Fix sizes
        factorizeSize(canvas);

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
            path.lineTo(left-width/2, top);
            path.arcTo(new RectF(left-width/2, top, left, yCenter), -90, 90);
        } //end at (left, yCenter)

        //Bottom Left Corner
        if(bottomLeftArc == NONE){
            path.lineTo(left, bottom);
            path.lineTo(xCenter, bottom);
        }else if(bottomLeftArc == INNER){
            path.arcTo(new RectF(left, yCenter, xCenter, bottom), 180, -90);
        }else if(bottomLeftArc == OUTER){
            path.lineTo(left, bottom+height/2);
            path.arcTo(new RectF(left, bottom, xCenter, bottom+height/2), 180, 90);
        }//end at (xCenter, bottom)

        //Bottom Right Corner
        if(bottomRightArc == NONE){
            path.lineTo(right, bottom);
            path.lineTo(right, yCenter);
        }else if(bottomRightArc == INNER){
            path.arcTo(new RectF(xCenter, yCenter, right, bottom), 90, -90);
        }else if(bottomRightArc == OUTER){
            path.lineTo(right+width/2, bottom);
            path.arcTo(new RectF(right, yCenter, right+width/2, bottom), 90, 90);
        }//end at (right, yCenter)

        //Top Right Corner
        if(topRightArc == NONE){
            path.lineTo(right, top);
            path.lineTo(xCenter, top);
        }else if(topRightArc == INNER){
            path.arcTo(new RectF(xCenter, top, right, yCenter), 0, -90);
        }else if(topRightArc == OUTER){
            path.lineTo(right, top-height/2);
            path.arcTo(new RectF(xCenter, top-height/2, right, top), 0, 90);
        }//end at (xCenter, top)

        path.close();

        canvas.drawPath(path, paint);
    }

    private void factorizeSize(Canvas canvas){
        int viewWidth = canvas.getWidth();
        int viewHeight = canvas.getHeight();

        int side = (Math.min(viewWidth, viewHeight)) * 2/4;

        left = viewWidth/2 - side/2;
        top = viewHeight/2 - side/2;
        right = viewWidth/2 + side/2;
        bottom = viewHeight/2 + side/2;

        if(topLeftArc != OUTER) left = 0;
        if(topRightArc != OUTER) top = 0;
        if(bottomLeftArc != OUTER) bottom = viewHeight;
        if(bottomRightArc != OUTER) right = viewWidth;

        width = right - left;
        height = bottom - top;

        xCenter = left+width/2;
        yCenter = top+height/2;
    }

}

