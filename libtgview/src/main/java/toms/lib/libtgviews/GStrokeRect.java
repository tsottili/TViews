package toms.lib.libtgviews;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 15/06/14.
 * Definizione della classe GStrokeRect
 */
public class GStrokeRect extends GRect {
    float mStrokeSize;

    public GStrokeRect(GView view, String Id) {
        super(view, Id);

        // Lo stile di default è: STROKE di size 1.
        mStrokeSize = 1;
        mPaint.setStrokeWidth(mStrokeSize);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public GStrokeRect(GView view, String Id, int color) {
        this(view, Id);
        setColor(color);
    }

    public GStrokeRect(GView view, String Id, RectF r, int color) {
        this(view, Id, color);

        mTopLeft.x = r.left;
        mTopLeft.y = r.top;
        mBottomRight.x = r.right;
        mBottomRight.y = r.bottom;
    }


    public float getStrokeSize() {
        return mStrokeSize;
    }


    public void setStrokeSize(float size) {
        mStrokeSize = size;
        mPaint.setStrokeWidth(mStrokeSize);
    }

    public void draw(Canvas c) {
        if (mbIsRound) {
            c.drawRoundRect(new RectF(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y), mRadiusX, mRadiusY, mPaint);
        } else {
            c.drawRect(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y, mPaint);
        }
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);

        ds.writeFloat(mStrokeSize);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        mStrokeSize = ds.readFloat();
        mPaint.setStrokeWidth(mStrokeSize);

    }

    public boolean isInside(float x, float y) {
        // bordo superiore o bordo inferiore
        if (((mTopLeft.x <= x) && (mTopLeft.x + mStrokeSize >= x)) ||
                ((mBottomRight.x <= x) && (mBottomRight.x + mStrokeSize >= x))) {
            if ((mTopLeft.y <= y) && (mBottomRight.y >= y)) {
                return true;
            }
        }
        // bordo sinistro o bordo destro
        else if (((mTopLeft.y <= y) && (mTopLeft.y + mStrokeSize >= y)) ||
                ((mBottomRight.y <= y) && (mBottomRight.y + mStrokeSize >= y))) {
            if ((mTopLeft.x <= x) && (mBottomRight.x >= x)) {
                return true;
            }
        }

        return false;

    }

    ;


}
