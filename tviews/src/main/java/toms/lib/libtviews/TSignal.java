package toms.lib.libtviews;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by toms on 06/04/15.
 */
public class TSignal {

    protected Paint mPaint;
    protected Path mPath = new Path();
    protected TGraph mOwnerView = null;
    protected Matrix mXY2UV = new Matrix();
    protected float mLastX = 0;
    protected float mLastY = 0;

    protected int counter = 0;

    protected boolean mbDrawThis = true;

    public TSignal(Paint paint)
    {
        setPaint(paint);
    }

    protected void setPaint(Paint p)
    {
        mPaint = p;
    }

    protected void setXY2UVMatrix(Matrix m)
    {
        mXY2UV = m;
    }

    public void enableDraw(boolean bVal)
    {
        mbDrawThis = bVal;
    }

    public boolean drawing()
    {
        return mbDrawThis;
    }

    protected void onDraw (Canvas canvas)
    {
        if (!mbDrawThis)
            return;

        Path TmpPath = new Path(mPath);
        TmpPath.setLastPoint(mLastX, mLastY);
        TmpPath.transform(mXY2UV);

        canvas.drawPath(TmpPath, mPaint);
    }

    public void addPoint(float x, float y)
    {
        addPoint(x, y, false);
    }

    public void addPoint(float x, float y, boolean bDiscontinuity)
    {
        if ( (mPath.isEmpty()) || bDiscontinuity)
        {
            mPath.moveTo(x,y);
        }
        else
        {
            mPath.lineTo(x,y);
        }

        if (mOwnerView != null) {
            mOwnerView.addDirty(x, y, mLastX,mLastY);
        }
        counter++;
        mLastX = x;
        mLastY = y;
    }

    public void clear()
    {
        mPath.rewind();
        counter = 0;
    }

    void setOwnerView(TGraph v)
    {
        mOwnerView = v;
    }

}

