package toms.lib.libtgviews;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 29/08/14.
 */
public class GFillCircle extends GCircle {

    public GFillCircle(GView view, String Id, float cx, float cy, float radius) {
        super(view, Id, cx, cy, radius);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public GFillCircle(GView view, String Id, float cx, float cy, float radius, int color) {
        this(view, Id, cx, cy, radius);
        setColor(color);
    }

    @Override
    public void draw(Canvas c) {
        c.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
    }

    @Override
    public boolean isInside(float x, float y) {
        return (mCenter.distanceFrom(x, y) <= mRadius);
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);


    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);


    }
}