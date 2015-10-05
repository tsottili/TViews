package toms.lib.libtgviews;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 15/06/14.
 */
public class GPath extends GItem {
    protected Path mPath;

    // Numero di elementi nel path
    protected int mCount;

    public GPath(GView view, String Id) {
        super(view, Id);
        mPath = new Path();
        mCount = 0;
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public GPath(GView view, String Id, int color) {
        this(view, Id);
        setColor(color);
    }

    public void draw(Canvas c) {
        c.drawPath(mPath, mPaint);
    }

    public int lineTo(GPoint p) {
        mPath.lineTo(p.x, p.y);
        mCount++;
        return mCount - 1;
    }

    public int moveTo(GPoint p) {
        mPath.moveTo(p.x, p.y);
        mCount++;
        return mCount - 1;
    }

    public boolean isEmpty() {
        return mPath.isEmpty();
    }

    public void reset() {
        mPath.reset();
        mCount = 0;
    }

    public void rewind()
    {
        mPath.rewind();
        mCount = 0;
    }

    public int count() {
        return mCount;
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);
    }


    public boolean isInside(float x, float y) {
        // GPath risponde sempre false.
        return false;
    }

    ;


}
