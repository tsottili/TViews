package toms.lib.libtgviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import toms.lib.libtcommon.TInternalFileReader;
import toms.lib.libtcommon.TInternalFileWriter;

/**
 * Created by toms on 15/06/14...
 */
public abstract class GScene {
    private static final String LOGCLASS = "GScene";

    // The element order is the z-order on screen
    protected ArrayList<GItem> mElements;

    // Current activity context
    protected Context mContext;

    protected GView mView;


    public GScene(Context context, GView view) {
        mElements = new ArrayList<GItem>();
        mContext = context;
        mView = view;
        mView.setScene(this);
    }

    // return the element index in the array
    public int add(GItem g) {
        mElements.add(g);
        return mElements.size() - 1;
    }

    public void insert(int index, GItem g) {
        mElements.add(index, g);
    }

    public Context getContext() {
        return mContext;
    }

    public GItem substitute(int index, GItem g) {
        GItem tmp = mElements.remove(index);
        mElements.add(index, g);
        return tmp;
    }

    public int getIndex(GItem g) {
        return mElements.indexOf(g);
    }

    public void swap(int i, int j) {
        GItem item_i = substitute(i, mElements.get(j));
        substitute(j, item_i);
    }

    public GItem get(int i) {
        if (mElements.size() > i) {
            return mElements.get(i);
        } else
            return null;
    }

    public void draw(Canvas c) {

        c.scale(1f, 1f);
        c.translate(0f, 0f);
        for (GItem item : mElements) {
            item.draw(c);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                Log.v(LOGCLASS, "Event action down");
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.v(LOGCLASS, "Event action pointer down");
            }
            break;
            case MotionEvent.ACTION_UP: {
                Log.v(LOGCLASS, "Event action up");
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {
                Log.v(LOGCLASS, "Event action pointer up");
            }
            break;
        }
        return true;
    }



    public abstract void build(RectF rect);

    public GItem findItemById(String Id) {
        for (GItem item : mElements) {
            if (item.getId().compareTo(Id) == 0) {
                return item;
            }
        }
        return null;
    }

    public GItem findItemByPos(float x, float y) {
        for (int i = mElements.size() - 1; i >= 0; i--) {
            GItem item = mElements.get(i);
            if (item.isInside(x, y)) {
                if (item.canBeFoundByPos())
                    return item;
            }
        }
        return null;
    }


    public boolean save(String sFileName) {
        TInternalFileWriter fw = new TInternalFileWriter(mContext);

        if (!fw.open(sFileName))
            return false;

        DataOutputStream ds = fw.getOutputStream();

        try {
            ds.writeInt(mElements.size());

            for (GItem item : mElements) {
                ds.writeUTF(item.getId());
                item.save(ds);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        fw.close();
        return true;
    }

    public boolean load(String sFileName) {

        TInternalFileReader fr = new TInternalFileReader(mContext);

        if (fr.FileExist(sFileName)) {

            fr.open(sFileName);

            DataInputStream ds = fr.getInputStream();

            try {
                int num_items = ds.readInt();

                for (int i = 0; i < num_items; i++) {
                    String item_id = ds.readUTF();

                    GItem g = findItemById(item_id);

                    if (g == null) {
                        return false;
                    } else {
                        g.load(ds);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            fr.close();

            return true;
        }

        return false;
    }

    public void resume() {
    }

    public void destroy() {
        mElements.clear();
    }
}
