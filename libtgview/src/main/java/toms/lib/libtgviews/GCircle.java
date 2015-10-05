package toms.lib.libtgviews;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 28/08/14.
 */
public abstract class GCircle extends GItem {

    protected GPoint mCenter;
    protected float mRadius;

    public GCircle(GView view, String Id, float cx, float cy, float radius) {
        super(view, Id);
        mCenter = new GPoint(view, Id.concat(".$$CEN"), cx, cy);
        mRadius = radius;
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);

        mCenter.save(ds);
        ds.writeFloat(mRadius);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        mCenter.load(ds);
        mRadius = ds.readFloat();

    }

    public GPoint getCenter() {
        return mCenter;
    }

    public void setCenter(GPoint g) {
        mCenter = g;
    }

    public float getRadius() {
        return mRadius;
    }

    void setRadius(float r) {
        mRadius = r;
    }

    public void setCenter(float x, float y) {
        mCenter.x = x;
        mCenter.y = y;
    }
}
