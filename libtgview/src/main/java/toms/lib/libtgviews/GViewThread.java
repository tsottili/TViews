package toms.lib.libtgviews;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by toms on 13/07/14...
 */
public class GViewThread extends GThread {
    private static final String LOGCLASS = "GViewThread";

    protected GScene mScene = null;
    protected SurfaceHolder mHolder;


    public GViewThread(SurfaceHolder holder) {
        super();
        mHolder = holder;
    }


    public boolean onTouchEvent(MotionEvent event)
    {
        if (mScene != null)
        {
            return mScene.onTouchEvent(event);
        }
        else {
            Log.v(LOGCLASS, "No scene found.");
            return true;
        }
    }


    public void run()
    {
        mbRunning = true;

        while (mbRunning)
        {
            if ( (mHolder.getSurface().isValid()) &&
                 (mScene != null) )
            {
                mHolder.setKeepScreenOn(true);

                Canvas c = null;
                try
                {
                    synchronized (mHolder)
                    {
                        c = mHolder.lockCanvas();

                        mScene.draw(c);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    if (c != null)
                    {
                        synchronized (mHolder)
                        {
                            mHolder.unlockCanvasAndPost(c);
                        }
                    }
                }
            }
        }
    }

    public GScene getScene()
    {
        return mScene;
    }

    public void setScene(GScene scene) {
        mScene = scene;
    }

}
