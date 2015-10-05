package toms.lib.libtgviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 15/06/14.
 */
public abstract class GItem {

    protected Paint mPaint;
    protected int mColor;
    protected String mId;

    protected GView mView;
    protected boolean mbFindByPos;

    public GItem(GView view, String Id) {
        mPaint = new Paint();
        setColor(Color.LTGRAY);
        mId = Id;
        mView = view;
        mbFindByPos = true;
    }

    public GItem(GView view, String Id, int color) {
        this(view, Id);
        setColor(color);
    }

    public Context getContext() {
        return mView.getContext();
    }

    public String getId() {
        return mId;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        mColor = color;
    }

    public void setFindByPos(boolean bVal) {
        mbFindByPos = bVal;
    }

    public boolean canBeFoundByPos() {
        return mbFindByPos;
    }

    abstract public void draw(Canvas c);

    abstract public boolean isInside(float x, float y);

    public void save(DataOutputStream ds) throws IOException {
        ds.writeUTF(mId);
        ds.writeInt(mColor);
        ds.writeBoolean(mbFindByPos);
    }

    public void load(DataInputStream ds) throws IOException {

        mId = ds.readUTF();
        int color = ds.readInt();
        setColor(color);
        setFindByPos(ds.readBoolean());
    }

    public GView getView() {
        return mView;
    }
}
