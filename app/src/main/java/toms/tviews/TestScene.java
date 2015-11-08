package toms.tviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import toms.lib.libtgview.GBackground;
import toms.lib.libtgview.GFillCircle;
import toms.lib.libtgview.GImage;
import toms.lib.libtgview.GItem;
import toms.lib.libtgview.GPath;
import toms.lib.libtgview.GPoint;
import toms.lib.libtgview.GScene;
import toms.lib.libtgview.GStrokeCircle;
import toms.lib.libtgview.GStrokeRect;
import toms.lib.libtgview.GText;
import toms.lib.libtgview.GView;


/**
 * Created by toms on 19/08/14...
 * TestScene: scena del contapassi.
 */
public class TestScene extends GScene {

    private static final String LOGCLASS = "GScene";
    protected GImage i;
    protected GImage i2;
    protected float mMaxValue = -1;
    protected float mMinValue = -1;
    protected int mLastIndex = 0;
    protected float mMaxIndex = 0;
    protected float mMaxHeight = 0;

    //Distanza in pixel tra due elementi successivi
    protected int mSpacing = 1;

    // indici di riferimento degli oggetti.
    protected int mIndexBG= -1;
    protected int mIndexRect= -1;
    protected int mIndexPath = -1;

    protected int mIndexJerk = -1;
    protected GPoint mLastPoint = null;

    public TestScene(Context context, GView view) {
        super(context, view);
    }

    public void build(RectF rect) {
        mMaxIndex = rect.width() - 2f;
        mMaxHeight = rect.height() - 2f;

        GBackground bg = new GBackground(mView, "BackGround", Color.LTGRAY);

        //bg.setImage(R.drawable.constructor);

        mIndexBG = add(bg);
        mIndexRect = add(new GStrokeRect(mView, "Border", rect, Color.BLUE));

        GText f1 = new GText(mView, "PippoText", "Pippo", new RectF(20, 20, 100, 80), Color.WHITE);
        f1.setRoundRect(true, 30, 30);
        f1.setBackground(true, Color.GRAY);
        f1.setVTextAlignment(Paint.Align.CENTER);
        f1.setHTextAlignment(Paint.Align.CENTER);
        f1.setFindByPos(false);
        add(f1);

        GText f2 = new GText(mView, "PlutoText", "Pluto", new RectF(120, 20, 200, 80), Color.WHITE);
        f2.setRoundRect(true, 5, 5);
        f2.setBorder(true, 2, Color.RED);
        f2.setBackground(true, Color.WHITE);
        f2.setVTextAlignment(Paint.Align.LEFT);
        f2.setHTextAlignment(Paint.Align.LEFT);
        add(f2);

        GText f3 = new GText(mView, "PaperinoText", "Paperino", new RectF(230, 20, 300, 80), Color.WHITE);
        f3.setBorder(true, 3, Color.BLUE);
        f3.setRoundRect(false, 5, 5);
        f3.setVTextAlignment(Paint.Align.RIGHT);
        f3.setHTextAlignment(Paint.Align.RIGHT);
        add(f3);

        i = new GImage(mView, "Under");
        i.setImage(R.mipmap.constructor);
        i.setPosition(50, 50);
        i.setScale(1f);

        i2 = new GImage(mView, "Upper");
        i2.setImage(R.mipmap.constructor);
        i2.setPosition(200, 200);
        i2.setRotationAngle(45);
        i2.setScale(1f);

        add(i);
        add(i2);

        GFillCircle c = new GFillCircle(mView, "CircoloF", 100, 200, 50, Color.CYAN);

        add(c);

        GStrokeCircle c2 = new GStrokeCircle(mView, "CircoloB", 140, 200, 50, Color.MAGENTA);

        add(c2);

        mIndexPath = add(new GPath(mView, "AccGraph", Color.RED));

    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.v(LOGCLASS, "Event action down");

                i2.setRotationAngle(i2.getRotationAngle() + 5);

            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.v(LOGCLASS, "Event action pointer down");
            }
            break;
            case MotionEvent.ACTION_UP: {
                Log.v(LOGCLASS, "Event action up");
                float x = event.getX();
                float y = event.getY();

                GItem item = findItemByPos(event.getX(), event.getY());

                if (item != null) {
                    if (item.getId().compareTo("PaperinoText") == 0) {
                        GText t = (GText) item;

                        t.setTextSize(t.getTextSize() + 0.1f);
                    } else if (item.getId().compareTo("PlutoText") == 0) {
                        GText t = (GText) item;
                        t.openInputSource(true);
                    }
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {
                Log.v(LOGCLASS, "Event action pointer up");
            }
            break;
        }

        return super.onTouchEvent(event);
    }

    public void setSpacing(int iVal)
    {
        if (iVal > 0) {
            mSpacing = iVal;
        }
    }

    public boolean drawPoint(float value) {
        mLastIndex+=mSpacing;
        if (mLastIndex >= mMaxIndex)
        {
            mLastIndex = 1;
        }

        if (value < mMinValue)
            value = mMinValue;

        if (value > mMaxValue)
            value = mMaxValue;

        float k = ((mMaxValue - value) / (mMaxValue - mMinValue));
        int iDrawPixel = (int) (k* mMaxHeight);


        if (mIndexPath < 0) return false;

        GPath gp = (GPath) get(mIndexPath);

        if (gp == null) return false;

        GPoint g = new GPoint(gp.getView(), "P" + iDrawPixel, mLastIndex, iDrawPixel);

        if (g.x == 1)
        {
            gp.rewind();
        }

        if (gp.isEmpty())
        {
            gp.moveTo(g);
        }
        else
        {
            gp.lineTo(g);
        }

        // se ultimo punto non è null, si calcola anche il jerk
        if ( (mLastPoint != null) && (mIndexJerk != -1) )
        {
            float jerk = g.y - mLastPoint.y;

            if (jerk < mMinValue)
                jerk = mMinValue;

            if (jerk > mMaxValue)
                jerk = mMaxValue;

            float kj = ((mMaxValue - jerk) / (mMaxValue - mMinValue));
            int ijerk = (int) (kj* mMaxHeight) - 25;

            GPath gpj = (GPath) get(mIndexJerk);
            GPoint gj = new GPoint(gpj.getView(), "P" + iDrawPixel, mLastIndex, ijerk);

            if (gj.x == 1)
            {
                gpj.reset();
            }

            if (gpj.isEmpty())
            {
                gpj.moveTo(gj);
            }
            else
            {
                gpj.lineTo(gj);
            }

        }

        mLastPoint = g;

        return true;
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public boolean setMaxValue(float MaxValue)
    {
        if (MaxValue > mMinValue) {
            mMaxValue = MaxValue;
            return true;
        }
        else
        {
            return false;
        }
    }

    public float getMinValue() {
        return mMinValue;
    }

    public boolean setMinValue(float MinValue) {
        if (MinValue < mMaxValue) {
            mMinValue = MinValue;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void resume() {
        load("ContapassiScene");
        super.resume();
    }

    public void destroy() {
        save("ContapassiScene");

        // richiama la destroy della classe parent
        super.destroy();
    }
}
