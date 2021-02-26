package com.stelladk.arclib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * ArcShape is used to give shape to ArcLayout based on custom parameters
 * @see ArcLayout
 */
public class ArcShape extends Shape {
    private final static String TAG = "ArcShape";

    /**
     * Static variable for outer arcs
     */
    protected final static int OUTER = 1;
    /**
     * Static variable for no arcs
     */
    protected final static int NONE = 0;
    /**
     * Static variable for inner arcs
     */
    protected final static int INNER = -1;

    /**
     * Static variable for x axis
     */
    protected final static int X_AXIS = 0;
    /**
     * Static variable for y axis
     */
    protected final static int Y_AXIS = 1;

    private int topLeftArc, topRightArc, bottomLeftArc, bottomRightArc;
    private int topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis;
    private float topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius;

    private int left, top, right, bottom;
    private int width, height;
    private int xCenter, yCenter;
    private float xRadius, yRadius;
    private float topLeftRadiusX, topRightRadiusX, bottomLeftRadiusX, bottomRightRadiusX;
    private float topLeftRadiusY, topRightRadiusY, bottomLeftRadiusY, bottomRightRadiusY;

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc) {
        this(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
                X_AXIS, Y_AXIS, Y_AXIS, X_AXIS);
    }

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc, int topLeftOuterAxis, int topRightOuterAxis, int bottomLeftOuterAxis, int bottomRightOuterAxis) {
        this(topLeftArc, topRightArc, bottomLeftArc, bottomRightArc,
                topLeftOuterAxis, topRightOuterAxis, bottomLeftOuterAxis, bottomRightOuterAxis,
                -1, -1, -1, -1);
    }

    public ArcShape(int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc, int topLeftOuterAxis, int topRightOuterAxis, int bottomLeftOuterAxis, int bottomRightOuterAxis, float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius) {
        this.topLeftArc = topLeftArc;
        this.topRightArc = topRightArc;
        this.bottomLeftArc = bottomLeftArc;
        this.bottomRightArc = bottomRightArc;
        this.topLeftOuterAxis = topLeftOuterAxis;
        this.topRightOuterAxis = topRightOuterAxis;
        this.bottomLeftOuterAxis = bottomLeftOuterAxis;
        this.bottomRightOuterAxis = bottomRightOuterAxis;
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.bottomRightRadius = bottomRightRadius;
    }

    /**
     * Draws custom shape on the canvas
     * @param canvas to draw the shape into
     * @param paint used to paint the shape
     */
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
            path.arcTo(new RectF(left, top, left+topLeftRadiusX*2, top+topLeftRadiusY*2), -90, -90);
        }else if(topLeftArc == OUTER){
            if(topLeftOuterAxis == X_AXIS){
                path.lineTo(left-topLeftRadiusX*2, top);
                path.arcTo(new RectF(left-topLeftRadiusX*2, top, left, top+topLeftRadiusY*2), -90, 90);
            }else if(topLeftOuterAxis == Y_AXIS){
                path.arcTo(new RectF(left, top-topLeftRadiusY*2, left+topLeftRadiusX*2, top), 90, 90);
                path.lineTo(left, yCenter);
            }
        } //end at (left, yCenter)

        //Bottom Left Corner
        if(bottomLeftArc == NONE){
            path.lineTo(left, bottom);
            path.lineTo(xCenter, bottom);
        }else if(bottomLeftArc == INNER){
            path.arcTo(new RectF(left, bottom-bottomLeftRadiusY*2, left+bottomLeftRadiusX*2, bottom), 180, -90);
        }else if(bottomLeftArc == OUTER){
            if(bottomLeftOuterAxis == Y_AXIS){
                path.lineTo(left, bottom+bottomLeftRadiusY*2);
                path.arcTo(new RectF(left, bottom, left+bottomLeftRadiusX*2, bottom+bottomLeftRadiusY*2), 180, 90);
            }else if(bottomLeftOuterAxis == X_AXIS){
                path.arcTo(new RectF(left-bottomLeftRadiusX*2, bottom-bottomLeftRadiusY*2, left, bottom), 0, 90);
                path.lineTo(xCenter, bottom);
            }
        }//end at (xCenter, bottom)

        //Bottom Right Corner
        if(bottomRightArc == NONE){
            path.lineTo(right, bottom);
            path.lineTo(right, yCenter);
        }else if(bottomRightArc == INNER){
            path.arcTo(new RectF(right-bottomRightRadiusX*2, bottom-bottomRightRadiusY*2, right, bottom), 90, -90);
        }else if(bottomRightArc == OUTER){
            if(bottomRightOuterAxis == X_AXIS){
                path.lineTo(right+bottomRightRadiusX*2, bottom);
                path.arcTo(new RectF(right, bottom-bottomRightRadiusY*2, right+bottomRightRadiusX*2, bottom), 90, 90);
            }else if(bottomRightOuterAxis == Y_AXIS){
                path.arcTo(new RectF(right-bottomRightRadiusX*2, bottom, right, bottom+bottomRightRadiusY*2), -90, 90);
                path.lineTo(right, yCenter);
            }
        }//end at (right, yCenter)

        //Top Right Corner
        if(topRightArc == NONE){
            path.lineTo(right, top);
            path.lineTo(xCenter, top);
        }else if(topRightArc == INNER){
            path.arcTo(new RectF(right-topRightRadiusX*2, top, right, top+topRightRadiusY*2), 0, -90);
        }else if(topRightArc == OUTER){
            if(topRightOuterAxis == Y_AXIS){
                path.lineTo(right, top-topRightRadiusY*2);
                path.arcTo(new RectF(right-topRightRadiusX*2, top-topRightRadiusY*2, right, top), 0, 90);
            }else if(topRightOuterAxis == X_AXIS){
                path.arcTo(new RectF(right, top, right+topRightRadiusX*2, top+topRightRadiusY*2), 180, 90);
                path.lineTo(xCenter, top);
            }
        }//end at (xCenter, top)

        path.close();

        canvas.drawPath(path, paint);
    }

    private void factorizeSize(Canvas canvas){
        int viewWidth = canvas.getWidth();
        int viewHeight = canvas.getHeight();

//        left = viewWidth/4;
//        top = viewHeight/4;
//        right = viewWidth * 3/4;
//        bottom = viewHeight * 3/4;

        left = calcMargin(viewWidth, X_AXIS, topLeftArc, topLeftOuterAxis, topLeftRadius, bottomLeftArc, bottomLeftOuterAxis, bottomLeftRadius);
        top = calcMargin(viewHeight, Y_AXIS, topLeftArc, topLeftOuterAxis, topLeftRadius, topRightArc, topRightOuterAxis, topRightRadius);
        bottom = viewHeight - calcMargin(viewHeight, Y_AXIS, bottomLeftArc, bottomLeftOuterAxis, bottomLeftRadius, bottomRightArc, bottomRightOuterAxis, bottomRightRadius);
        right = viewWidth - calcMargin(viewWidth, X_AXIS, topRightArc, topRightOuterAxis, topRightRadius, bottomRightArc, bottomRightOuterAxis, bottomRightRadius);

        width = right - left;
        height = bottom - top;

        xCenter = left+width/2;
        yCenter = top+height/2;

        xRadius = width *3/8F;
        yRadius = height*3/8F;

        topLeftRadiusX=topLeftRadiusY = topLeftRadius;
        topRightRadiusX=topRightRadiusY = topRightRadius;
        bottomLeftRadiusX=bottomLeftRadiusY = bottomLeftRadius;
        bottomRightRadiusX=bottomRightRadiusY = bottomRightRadius;

        if(topLeftRadius == -1) {
            topLeftRadiusX = xRadius;
            topLeftRadiusY = yRadius;
        }
        if(topRightRadius == -1) {
            topRightRadiusX = xRadius;
            topRightRadiusY = yRadius;
        }
        if(bottomLeftRadius == -1){
            bottomLeftRadiusX = xRadius;
            bottomLeftRadiusY = yRadius;
        }
        if(bottomRightRadius == -1) {
            bottomRightRadiusX = xRadius;
            bottomRightRadiusY = yRadius;
        }
    }

    //TODO CHECK DEFAULT RADIUS
    private int calcMargin(int dim, int axis, int arc1, int arcAxis1, float arcRadius1, int arc2, int arcAxis2, float arcRadius2){
        int margin = 0;
        if((arc1 == OUTER && arcAxis1 == axis) && (arc2 == OUTER && arcAxis2 == axis)){
            int maximum = (int)Math.max(arcRadius1, arcRadius2);
            margin = maximum == -1? dim/4 : maximum;
        }else if(arc1 == OUTER && arcAxis1 == axis){
            margin = arcRadius1 == -1? dim/4 : (int)arcRadius1;
        }else if(arc2 == OUTER && arcAxis2 == axis){
            margin = arcRadius2 == -1? dim/4 : (int)arcRadius2;
        }
        return margin;
    }

}

