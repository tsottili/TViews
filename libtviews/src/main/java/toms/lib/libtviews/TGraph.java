package toms.lib.libtviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import toms.lib.tviews.R;

/**
 * Created by toms on 29/03/15.
 */
public class TGraph extends View {

    protected boolean mbDrawLine;

    // Print axes origin
    protected boolean mbPrintOrigin;

    // Paints
    protected Paint mAxisPaint;
    protected Paint mBGPaint;
    protected Paint mGraphPaint;
    protected Paint mLabelPaint;
    protected Paint mGridPaint;

    // View size in pixels
    protected int mWidth = 1;
    protected int mHeight = 1;

    // XY are the input coordinates for the graph, UV are the drawing coordinate.

    // XY visible limit
    protected float mXNegativeLimit = -1;
    protected float mYNegativeLimit = -1;
    protected float mXPositiveLimit = 1;
    protected float mYPositiveLimit = 1;

    // origin coordinate
    protected float mAxisX = 0;     // x value for drawing axis
    protected float mAxisY = 0;     // y value for drawing axis

    // UV coodidate value for drawing axis.
    protected float mAxisU = 0;
    protected float mAxisV = 0;

    // Scaling X U and Y V
    protected float mScaleU = 1;
    protected float mScaleV = 1;

    // Space between grid lines. When 0 grid is not visible
    private float mGridXWidth = 0;
    private float mGridYWidth = 0;

    // Array of the existing signal
    protected ArrayList<TSignal> mList = new ArrayList<TSignal>();

    protected Path mGridPath = null;


    public TGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TGraph,
                0, 0);

        try {
            mbDrawLine = a.getBoolean(R.styleable.TGraph_Line, false);

            mAxisX = a.getFloat(R.styleable.TGraph_XAxis, 0.0f);
            mAxisY = a.getFloat(R.styleable.TGraph_YAxis,0.0f);
            mXNegativeLimit = a.getFloat(R.styleable.TGraph_XNegativeLimit, -100f);
            mYNegativeLimit = a.getFloat(R.styleable.TGraph_YNegativeLimit, -100f);
            mXPositiveLimit = a.getFloat(R.styleable.TGraph_XPositiveLimit, 100f);
            mYPositiveLimit = a.getFloat(R.styleable.TGraph_YPositiveLimit, 100f);

            mbPrintOrigin = a.getBoolean(R.styleable.TGraph_PrintOrigin, false);
            mGridXWidth = a.getFloat(R.styleable.TGraph_GridXInterval, 0);
            mGridYWidth = a.getFloat(R.styleable.TGraph_GridYInterval, 0);

        } finally {
            a.recycle();
        }

        mList.clear();

        // Adapt values to the view
        CalculateUV();

        //  Create all the necessary Paints
        initPaints();

    }

    public float getXNegativeLimit()
    {
        return mXNegativeLimit;
    }

    public float getYNegativeLimit()
    {
        return mYNegativeLimit;
    };

    public float getXPositiveLimit()
    {
        return mXPositiveLimit;
    }

    public float getYPositiveLimit()
    {
        return mYPositiveLimit;
    }

    public void setXNegativeLimit(float fval)
    {
        mXNegativeLimit = fval;
        CalculateUV();
    }

    public void setYNegativeLimit(float fval)
    {
        mYNegativeLimit = fval;
        CalculateUV();
    };

    public void setXPositiveLimit(float fval)
    {
        mXPositiveLimit = fval;
        CalculateUV();
    }

    public void setYPositiveLimit(float fval)
    {
        mYPositiveLimit = fval;
        CalculateUV();
    }

    public void setLimits(float minX, float minY, float maxX, float maxY)
    {
        mXNegativeLimit = minX;
        mXPositiveLimit = maxX;
        mYNegativeLimit = minY;
        mYPositiveLimit = maxY;
        CalculateUV();
    }

    protected void initPaints() {
        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setColor(Color.BLACK);

        mGraphPaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mGraphPaint.setColor(Color.BLUE);

        mBGPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setColor(Color.LTGRAY);

        mLabelPaint = new Paint();
        mLabelPaint.setTextSize(10);
        mLabelPaint.setColor(Color.RED);

        mGridPaint = new Paint();
        mGridPaint.setColor(Color.BLACK);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setPathEffect(new DashPathEffect(new float[]{5, 2}, 0));
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        mWidth = w;
        mHeight = h;

        CalculateUV();
    }

    @Override
    protected void onDraw (Canvas canvas) {

        // DrawPaint not supported in edit mode
        if (isInEditMode() == false) {
            // Draw background
            canvas.drawPaint(mBGPaint);
        }

        // Draw the grid if requsted
        if (mGridPath != null)
        {
            canvas.drawPath(mGridPath, mGridPaint);
        }

        // Axis:
        // y
        canvas.drawLine(mAxisU, 0f, mAxisU, mHeight, mAxisPaint);
        // x
        canvas.drawLine(0f, mAxisV, mWidth, mAxisV, mAxisPaint);

        // Print the orgin values?
        if (mbPrintOrigin)
        {
            String sOrigin = "(" + mAxisX + "," +mAxisY+")";
            canvas.drawText(sOrigin, mAxisU + mLabelPaint.getTextSize(), mAxisV + mLabelPaint.getTextSize(), mLabelPaint);
        }

        // Finally draw the signals!
        for (TSignal signal:mList)
        {
            signal.onDraw(canvas);
        }

    }

    // Conversion from X to U
    protected float X2U(float value)
    {
        return mWidth * (value - mXNegativeLimit)/(mXPositiveLimit - mXNegativeLimit);
    }

    // Conversion from Y to V
    protected float Y2V(float value)
    {
        return mHeight * (value - mYPositiveLimit)/(mYNegativeLimit - mYPositiveLimit);
    }

    // Calculate all the conversion for drawing on UV.
    protected void CalculateUV()
    {
        mAxisU = X2U(mAxisX);
        mAxisV = Y2V(mAxisY);

        mScaleU = (mWidth)/(mXPositiveLimit - mXNegativeLimit );
        mScaleV = (mHeight)/(mYNegativeLimit - mYPositiveLimit );

        for (TSignal signal:mList)
        {
            Matrix m = new Matrix();
            m.preTranslate(mAxisU, mAxisV);
            m.preScale(mScaleU, mScaleV);
            signal.setXY2UVMatrix(m);
        }


        if ( (mGridXWidth > 0) || (mGridYWidth > 0) )
        {
            mGridPath = new Path();

            // Draw vertical grid lines, if requested
            if (mGridXWidth > 0) {

                for (float dx = 0; dx > mXNegativeLimit; dx = dx - mGridXWidth) {
                    float du = X2U(dx);
                    mGridPath.moveTo(du, 0f);
                    mGridPath.lineTo(du, mHeight);
                }

                for (float dx = 0; dx < mXPositiveLimit; dx = dx + mGridXWidth) {
                    float du = X2U(dx);
                    mGridPath.moveTo(du, 0f);
                    mGridPath.lineTo(du, mHeight);
                }

            }

            // Draw horizontal grid lines, if requested
            if (mGridYWidth > 0) {

                for (float dy = 0; dy > mYNegativeLimit; dy = dy - mGridYWidth) {
                    float dv = Y2V(dy);
                    mGridPath.moveTo(0f, dv);
                    mGridPath.lineTo(mWidth, dv);
                }

                for (float dy = 0; dy < mXPositiveLimit; dy = dy + mGridYWidth) {
                    float dv = Y2V(dy);
                    mGridPath.moveTo(0f, dv);
                    mGridPath.lineTo(mWidth, dv);
                }
            }
        }
        else
        {
            mGridPath = null;
        }
        invalidate();
        requestLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void AddSignal(TSignal signal)
    {
        mList.add(signal);

        Matrix m = new Matrix();
        m.preTranslate(mAxisU, mAxisV);
        m.preScale(mScaleU, mScaleV);

        signal.setXY2UVMatrix(m);
        signal.setOwnerView(this);
    }

    public void DeleteSignal(TSignal signal)
    {
        mList.remove(signal);
    }


    public void addDirty(float x1, float y1, float x2, float y2)
    {
        int ix1 = (int)(x1*mScaleU-mAxisU);
        int iy1 = (int)(y1*mScaleV-mAxisV);
        int ix2 = (int)(x2*mScaleU-mAxisU);
        int iy2 = (int)(y2*mScaleV-mAxisV);

        invalidate();
        requestLayout();
    }

    public float getGridXWidth() {
        return mGridXWidth;
    }

    public void setGridXWidth(float GridXWidth) {
        mGridXWidth = GridXWidth;
    }

    public float getGridYWidth() {
        return mGridYWidth;
    }

    public void setGridYWidth(float GridYWidth) {
        mGridYWidth = GridYWidth;
    }

}
