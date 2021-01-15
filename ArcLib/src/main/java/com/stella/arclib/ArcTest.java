package com.stella.arclib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class ArcTest extends View {
    int viewWidth = 0;
    int viewHeight = 0;
    int side = 200;

    public ArcTest(Context context) {
        super(context);
    }

    public ArcTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void draw(Canvas canvas) {
        viewWidth = this.getMeasuredWidth();
        viewHeight = this.getMeasuredHeight();
//        canvas.drawColor(Color.WHITE);

        side = (Math.min(viewWidth, viewHeight)) * 2/4;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);

        int left = viewWidth/2 - side/2;
        int top = viewHeight/2 - side/2;
        int right = viewWidth/2 + side/2;
        int bottom = viewHeight/2 + side/2;

        int width = right - left;
        int height = bottom - top;

        //Original Rect
        canvas.drawRect(left, top, right, bottom, paint);

        //Outer curve
        canvas.drawRect(left, bottom, left+width/2, bottom+height/2, paint);
//        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawArc(left, bottom, right, bottom*2,
                180, 90, true, paint);

        //Inner curve
//        paint.setColor(Color.WHITE);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawRect(left+width/2, top+height/2, right, bottom, paint);
        paint.setColor(Color.RED);
        paint.setXfermode(null);
        canvas.drawArc(left, top, right, bottom,
                0, 90, true, paint);


//        canvas.drawArc(viewWidth/2 - side/2, viewHeight/2 - side/2,
//                viewWidth/2 + side/2, viewHeight/2 + side/2,
//                45, 135,
//                true, paint);

//        paint.setColor(Color.BLUE);
//
//        canvas.drawArc(viewWidth/2 - side/2, viewHeight/2 - side/2,
//                viewWidth/2 + side/2, viewHeight/2 + side/2,
//                45, 135,
//                false, paint);



        //Testing
        paint.setColor(Color.BLUE);
//        canvas.drawRect(0, 0, width*2, height*2, paint);
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(0,0); //Begin (0,0)
        path.lineTo(0, height*3/2); //Line to (0,height*2/3)
        path.arcTo(new RectF(0, height, width/2, height+height/2), 180, 90);
//        path.moveTo(width/2, height);
//        path.lineTo(width/2, height*3/2);
        path.lineTo(width/2, height);
        path.lineTo(width, height);
        path.lineTo(width, 0);
        path.lineTo(0,0);
        path.close();

        canvas.drawPath(path,paint);


        super.draw(canvas);
    }
}
