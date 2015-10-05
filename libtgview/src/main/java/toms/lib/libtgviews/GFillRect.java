package toms.lib.libtgviews;

/**
 * Created by toms on 23/07/14.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GFillRect extends GRect {

    public GFillRect(GView view, String Id) {
        super(view, Id);

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public GFillRect(GView view, String Id, int color) {
        this(view, Id);
        setColor(color);
    }

    public GFillRect(GView view, String Id, RectF r, int color) {
        this(view, Id, color);

        mTopLeft = new GPoint(view, Id.concat(".$$TL"), r.left, r.top);
        mBottomRight = new GPoint(view, Id.concat(".$$BR"), r.right, r.bottom);
    }

    public void draw(Canvas c) {
        if (mbIsRound) {
            c.drawRoundRect(new RectF(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y), mRadiusX, mRadiusY, mPaint);
        } else {
            c.drawRect(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y, mPaint);
        }
    }

    public boolean isInside(float x, float y) {
        boolean bVal =
                ((mTopLeft.x <= x) &&
                        (mBottomRight.x >= x) &&
                        (mTopLeft.y <= y) &&
                        (mBottomRight.y >= y));

        return bVal;
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);


    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);


    }


}
