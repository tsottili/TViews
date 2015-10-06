package toms.lib.libtgviews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Created by toms on 13/07/14...
 */
public class GView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String LOGCLASS = "GView";

    protected Context mContext;

    protected SurfaceHolder mHolder;
    // Oggetto che riceverà l'input da tastiera
    protected GText mReceiver;
    // Thread per la visualizzazione
    private GViewThread mGViewThread;
    // Oggetto scena da visualizzare
    private GScene mScene;

    public GView(Context context) {
        super(context);
        mContext = context;
        mScene = null;
    }

    public GView(Context context, AttributeSet attrSet) {
        super(context, attrSet);

        mContext = context;
        mScene = null;

    }

    public void setScene(GScene scene) {
        mScene = scene;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.v(LOGCLASS, "surfaceCreated");

        Rect r = holder.getSurfaceFrame();

        RectF rf = new RectF();
        rf.set(r);

        // richiama la funzione per la creazione della scena
        mScene.build(rf);

        // richiama il resume della scena
        mScene.resume();

        // Lega la scena al thread
        mGViewThread.setScene(mScene);


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.v(LOGCLASS, "surfaceDestroyed");

        mGViewThread.exit();

        mScene.destroy();

        mHolder.removeCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.v(LOGCLASS, "surfaceChanged");

        mGViewThread.start();

    }

    public void resume() {
        Log.v(LOGCLASS, "Resume");
        mHolder = getHolder();
        mHolder.addCallback(this);

        mGViewThread = new GViewThread(mHolder);
    }

    public void pause() {

        Log.v(LOGCLASS, "Pause");
        mGViewThread.exit();
        mHolder.removeCallback(this);

        if (android.os.Build.VERSION.SDK_INT <= 16) {
            mScene.destroy();
        }
    }

    @Override
    @NonNull
    public boolean onTouchEvent(MotionEvent event) {

        if (mGViewThread != null) {
            if (mGViewThread.onTouchEvent(event) == false)
                return super.onTouchEvent(event);
            else
                return true;
        } else {
            Log.v(LOGCLASS, "No GThread instance found.");
            return super.onTouchEvent(event);
        }
    }

    // indica se la scena è pronta.
    public boolean isValid() {
        return (mScene != null);
    }

    public GItem getItem(int index) {
        return mScene.get(index);
    }

//    @Override
//    public boolean onCheckIsTextEditor() {
//        return true;
//    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        if (mReceiver != null) {
            return mReceiver.onCreateInputConnection(outAttrs);
        } else
            return null;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        return super.onKeyPreIme(keyCode, event);
    }

    public void setInputReceiver(GText item) {
        mReceiver = item;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

}