package toms.lib.libtgviews;

import android.graphics.RectF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 23/07/14.
 * Classe astratta per GRect.
 */
public abstract class GRect extends GItem {
    protected GPoint mTopLeft = null;
    protected GPoint mBottomRight = null;


    protected float mRadiusX;
    protected float mRadiusY;
    protected boolean mbIsRound;

    GRect(GView view, String Id) {
        super(view, Id);

        mTopLeft = new GPoint(view, Id.concat(".$$TL"));
        mBottomRight = new GPoint(view, Id.concat(".$$BR"));

        mbIsRound = false;
        mRadiusX = 0;
        mRadiusY = 0;
    }

    public void setRoundRect(boolean bVal, float rx, float ry) {
        mbIsRound = bVal;
        mRadiusX = rx;
        mRadiusY = ry;
    }

    public void setRect(float l, float t, float r, float b) {
        mTopLeft.x = l;
        mTopLeft.y = t;
        mBottomRight.x = r;
        mBottomRight.y = b;
    }

    public void setRect(RectF r) {
        mTopLeft.x = r.left;
        mTopLeft.y = r.top;
        mBottomRight.x = r.right;
        mBottomRight.y = r.bottom;
    }

    public float width() {
        return mBottomRight.x - mTopLeft.x;
    }

    public float height() {
        return mBottomRight.y - mTopLeft.y;
    }

    public RectF getRectF() {
        return new RectF(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y);
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);

        mTopLeft.save(ds);
        mBottomRight.save(ds);

        ds.writeBoolean(mbIsRound);
        ds.writeFloat(mRadiusX);
        ds.writeFloat(mRadiusY);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        mTopLeft.load(ds);
        mBottomRight.load(ds);

        mbIsRound = ds.readBoolean();
        mRadiusX = ds.readFloat();
        mRadiusY = ds.readFloat();

    }
}
