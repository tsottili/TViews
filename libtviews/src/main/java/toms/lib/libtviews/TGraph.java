package toms.lib.libtviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    // Stampa le coordinate degli assi
    protected boolean mbPrintOrigin;

    // -1 no label
    // 0 -> label a zero
    // 1 -> label a meta asse
    protected int mLabelPosition;

    // Paints
    protected Paint mAxisPaint;
    protected Paint mBGPaint;
    protected Paint mGraphPaint;
    protected Paint mLabelPaint;

    // Dimensioni della view in px
    protected int mWidth = 1;
    protected int mHeight = 1;

    // Sistema XY: sistema di riferimento del grafico in input.

    //Coordinate visibili
    protected float mXNegativeLimit = -1;
    protected float mYNegativeLimit = -1;
    protected float mXPositiveLimit = 1;
    protected float mYPositiveLimit = 1;

    // Coordinate del punto da cui si disegna il sistema di rifeirmento
    protected float mAxisX = 0;     // coordinata x per il disegno del sistema di riferimento
    protected float mAxisY = 0;     // coordinata y per il disegno del sistema di riferimento

    // Sistema UV: sistema di riferimeto del disegno del grafico
    protected float mAxisU = 0;
    protected float mAxisV = 0;
    protected float mScaleU = 1;
    protected float mScaleV = 1;


    // Vettore degli elementi grafici da disegnare
    protected ArrayList<TSignal> mList = new ArrayList<TSignal>();

    public TGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TGraph,
                0, 0);

        try {
            mbDrawLine = a.getBoolean(R.styleable.TGraph_Line, false);
            mLabelPosition = a.getInteger(R.styleable.TGraph_labelPosition, -1);

            mAxisX = a.getFloat(R.styleable.TGraph_XAxis, 0.0f);
            mAxisY = a.getFloat(R.styleable.TGraph_YAxis,0.0f);
            mXNegativeLimit = a.getFloat(R.styleable.TGraph_XNegativeLimit, -100f);
            mYNegativeLimit = a.getFloat(R.styleable.TGraph_YNegativeLimit, -100f);
            mXPositiveLimit = a.getFloat(R.styleable.TGraph_XPositiveLimit, 100f);
            mYPositiveLimit = a.getFloat(R.styleable.TGraph_YPositiveLimit, 100f);

            mbPrintOrigin = a.getBoolean(R.styleable.TGraph_PrintOrigin, false);

        } finally {
            a.recycle();
        }

        mList.clear();

        CalculateUV();

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

        // Disegna il background
        canvas.drawPaint(mBGPaint);

        // Disegna il sistema di riferimentp
        // Asse y
        canvas.drawLine(mAxisU, 0f, mAxisU, mHeight, mAxisPaint);
        // Asse x
        canvas.drawLine(0f, mAxisV, mWidth, mAxisV, mAxisPaint);

        if (mbPrintOrigin)
        {
            String sOrigin = "(" + mAxisX + "," +mAxisY+")";
            canvas.drawText(sOrigin, mAxisU + mLabelPaint.getTextSize(), mAxisV + mLabelPaint.getTextSize(), mLabelPaint);
        }

        for (TSignal signal:mList)
        {
            signal.onDraw(canvas);
        }

    }

    protected float X2U(float value)
    {
        return mWidth * (value - mXNegativeLimit)/(mXPositiveLimit - mXNegativeLimit);
    }

    protected float Y2V(float value)
    {
        return mHeight * (value - mYPositiveLimit)/(mYNegativeLimit - mYPositiveLimit);
    }

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

}
