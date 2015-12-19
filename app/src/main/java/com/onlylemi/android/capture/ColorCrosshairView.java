package com.onlylemi.android.capture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ColorCrosshairView extends View {

    private final static String TAG = "ColorCrosshairView:";

    private Paint paint;
    private Path path;

    public ColorCrosshairView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2.0F);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setDither(false);
    }

    private void updatePath(float paramFloat1, float paramFloat2) {
        float f1 = paramFloat1 / 2.0F;
        float f2 = paramFloat2 / 2.0F;
        float f3 = paramFloat1 / 2.0F - 20.0F - 4.0F;
        float f4 = paramFloat1 / 2.0F - 4.0F;
        path = new Path();
        path.addCircle(f1, f2, f3, Path.Direction.CCW);
        path.addCircle(f1, f2, f4, Path.Direction.CW);
        path.moveTo(f1, 2.0F * 4.0F);
        path.lineTo(f1, f2 - 1.0F);
        path.moveTo(paramFloat1 - 2.0F * 4.0F, f2);
        path.lineTo(f1 + 1.0F, f2);
    }

    public void draw(Canvas paramCanvas) {
        super.draw(paramCanvas);
        paramCanvas.drawPath(path, paint);
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        updatePath(paramInt1, paramInt2);
    }

    public void setColor(int paramInt) {
        paint.setColor(paramInt);
        invalidate();
    }
}
