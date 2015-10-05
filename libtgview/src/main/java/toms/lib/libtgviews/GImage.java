package toms.lib.libtgviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 19/08/14...
 */
public class GImage extends GItem {

    protected GPoint mPosition = null;

    protected int mBitmapResourceId = -1;
    protected Bitmap mBitmap = null;
    protected Paint mBitmapPaint = null;
    protected Matrix mMatrix = null;

    protected float mRotationAngle;
    protected GPoint mScale;


    public GImage(GView view, String Id) {
        super(view, Id);

        mPosition = new GPoint(view, Id.concat(".$$POS"), 0, 0);

        mBitmapPaint = new Paint();
        mMatrix = new Matrix();
        mScale = new GPoint(view, Id.concat(".$$SCL"));
        mRotationAngle = 0;
    }

    public void setPosition(float x, float y) {
        mPosition.x = x;
        mPosition.y = y;
        updateMatrix();
    }

    public GPoint getPosition() {
        return mPosition;
    }

    public void setPosition(GPoint p) {
        mPosition = p;
        updateMatrix();
    }

    public Bitmap getImage() {
        return mBitmap;
    }

    public void setImage(int resource) {
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), resource);
        mBitmapResourceId = resource;
    }

    public void setImage(Bitmap b) {
        mBitmap = b;
    }

    public int getBitmapResourceId() {
        return mBitmapResourceId;
    }

    public float getRotationAngle() {
        return mRotationAngle;
    }

    public void setRotationAngle(float angle) {
        mRotationAngle = angle;
        updateMatrix();
    }

    private void updateMatrix() {
        float px = mPosition.x + mBitmap.getWidth() / 2;
        float py = mPosition.y + mBitmap.getHeight() / 2;

        mMatrix.setTranslate(mPosition.x, mPosition.y);
        mMatrix.postRotate(mRotationAngle, px, py);
        mMatrix.postScale(mScale.x, mScale.y, px, py);

    }

    public void setScale(float scale) {
        mScale.x = scale;
        mScale.y = scale;
        updateMatrix();
    }

    public void draw(Canvas c) {
        c.drawBitmap(mBitmap, mMatrix, mBitmapPaint);
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);

        mPosition.save(ds);
        mScale.save(ds);

        ds.writeFloat(mRotationAngle);

    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        mPosition.load(ds);
        mScale.load(ds);

        mRotationAngle = ds.readFloat();

        updateMatrix();

    }

    public boolean isInside(float x, float y) {
        // simple...
        RectF boundingRect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mMatrix.mapRect(boundingRect);

        return (boundingRect.contains(x, y));

    }
}

