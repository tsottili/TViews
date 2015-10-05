package toms.lib.libtgviews;

import android.graphics.Canvas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 02/06/14.
 * Definizione della classe GPoint
 */
public class GPoint extends GItem {
    public float x;
    public float y;

    public GPoint(GView view, String Id, float x, float y) {
        super(view, Id);

        this.x = x;
        this.y = y;
    }

    public GPoint(GView view, String Id) {
        super(view, Id);
        x = 0;
        y = 0;
    }

    public void draw(Canvas c) {
        c.drawPoint(x, y, mPaint);
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);
        ds.writeFloat(x);
        ds.writeFloat(y);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        x = ds.readFloat();
        y = ds.readFloat();

    }


    public boolean isInside(float x, float y) {
        return (this.x == x) && (this.y == y);
    }

    public float distanceFrom(GPoint g) {
        return (float) Math.sqrt((x - g.x) * (x - g.x) + (y - g.y) * (y - g.y));
    }

    public float distanceFrom(float cx, float cy) {
        return (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
    }
}

