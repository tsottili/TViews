package toms.lib.libtgviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 15/06/14...
 */
public class GBackground extends GItem {

    int mBitmapResourceId = -1;
    Bitmap mBitmap = null;
    Paint mBitmapPaint = null;

    boolean mbGradient = false;


    public GBackground(GView view, String Id) {
        super(view, Id);
        setColor(Color.LTGRAY);
        mbFindByPos = false;
    }

    public GBackground(GView view, String Id, int color) {
        this(view, Id);

        setColor(color);

        mBitmapPaint = new Paint();
    }

    public boolean isInside(float x, float y) {
        // background risponde sempre con lo stato del flag findbypos.
        return mbFindByPos;
    }

    public void setImage(Bitmap b) {
        mBitmap = b;
    }

    public void setImage(int resource) {
        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), resource);
        mBitmapResourceId = resource;
    }

    public int getImageResourceId() {
        return mBitmapResourceId;
    }


    public void draw(Canvas c) {
        c.drawColor(getColor());
        //Paint p = new Paint();
        // start at 0,0 and go to 0,max to use a vertical
        // gradient the full height of the screen.
        //p.setShader(new LinearGradient(0, 0, 0, c.getHeight(), Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));
        //c.drawPaint(p);

        if (mBitmap != null) {

            Rect src = new Rect();
            src.left = 0;
            src.top = 0;
            src.right = mBitmap.getWidth();
            src.bottom = mBitmap.getHeight();

            c.drawBitmap(mBitmap, src, c.getClipBounds(), mBitmapPaint);

        }
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

    }


}

