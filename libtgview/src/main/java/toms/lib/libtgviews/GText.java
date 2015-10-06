package toms.lib.libtgviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.text.Editable;
import android.text.InputType;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by toms on 27/07/14.
 * Definizione della classe GText
 */
public class GText extends GRect implements IGInput {

    protected GStrokeRect mBorder;
    protected boolean mbHasBorder;

    protected GFillRect mBackground;
    protected boolean mbHasBackground;

    protected String mText;

    protected Paint mPText;

    protected Paint.Align mHTextAlignement;
    protected Paint.Align mVTextAlignement;

    protected GPoint mTextOrigin = null;

    protected boolean mbUpdateTextPosition = false;
    protected boolean mbUpdateBorderRect = false;
    protected boolean mbUpdateBackgroundRect = false;

    protected boolean mbSoftkeyboardFastInput = true;

    protected boolean mbAutoclose = false;
    protected Editable mEditable;


    public GText(GView view, String Id) {
        super(view, Id);

        mbHasBorder = false;
        mBorder = new GStrokeRect(view, ".$$BRD");

        mbHasBackground = false;
        mBackground = new GFillRect(view, ".$$BG");

        mPText = new Paint();
        mPText.setColor(Color.BLACK);

        mHTextAlignement = Paint.Align.CENTER;
        mVTextAlignement = Paint.Align.CENTER;

        mTextOrigin = new GPoint(view, ".$$ORG");

        mEditable = new Editable.Factory().newEditable("");
    }

    public GText(GView view, String Id, int color) {
        this(view, Id);
        setColor(color);
    }

    public GText(GView view, String Id, RectF r, int color) {
        this(view, Id, color);

        mTopLeft = new GPoint(view, Id.concat(".$$TL"), r.left, r.top);
        mBottomRight = new GPoint(view, (".$$BR"), r.right, r.bottom);

        mbUpdateTextPosition = true;
    }

    public GText(GView view, String Id, String s, RectF r, int color) {
        this(view, Id, r, color);
        mText = s;

        mEditable.clear();
        mEditable.append(mText);

        UpdateTextPosition();

    }

    protected void UpdateTextPosition() {

        float ypos;
        float xpos;

        if (mHTextAlignement == Paint.Align.CENTER) {
            xpos = mTopLeft.x + (mBottomRight.x - mTopLeft.x) / 2;
            mPText.setTextAlign(mHTextAlignement);
        } else if (mHTextAlignement == Paint.Align.RIGHT) {
            xpos = mBottomRight.x;
            mPText.setTextAlign(mHTextAlignement);
            if (mbHasBorder)
                xpos -= mBorder.getStrokeSize();
        } else {
            xpos = mTopLeft.x;
            mPText.setTextAlign(mHTextAlignement);
            if (mbHasBorder)
                xpos += mBorder.getStrokeSize();
        }

        if (mVTextAlignement == Paint.Align.CENTER) {
            ypos = mTopLeft.y + height() / 2;
        } else if (mVTextAlignement == Paint.Align.RIGHT) {
            Rect bounds = new Rect();
            mPText.getTextBounds(mText, 0, mText.length(), bounds);

            ypos = mBottomRight.y - bounds.height() / 2;
            if (mbHasBorder)
                ypos -= mBorder.getStrokeSize();
        } else {
            Rect bounds = new Rect();
            mPText.getTextBounds(mText, 0, mText.length(), bounds);

            ypos = mTopLeft.y + bounds.height();
            if (mbHasBorder)
                ypos += mBorder.getStrokeSize();

        }

        // Tutto questo per calcolare l'origine del testo
        mTextOrigin.x = xpos;
        mTextOrigin.y = ypos;
    }

    protected void SynchronizeBorderRect() {
        mBorder.setRect(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y);
        mBorder.setRoundRect(mbIsRound, mRadiusX, mRadiusY);
    }

    protected void SynchronizeBackgroundRect() {
        mBackground.setRect(mTopLeft.x, mTopLeft.y, mBottomRight.x, mBottomRight.y);
        mBackground.setRoundRect(mbIsRound, mRadiusX, mRadiusY);
    }

    public Paint.Align getHTextAlignment() {
        return mHTextAlignement;
    }

    public void setHTextAlignment(Paint.Align align) {
        mHTextAlignement = align;
        mbUpdateTextPosition = true;
    }

    public Paint.Align getVTextAlignment() {
        return mVTextAlignement;
    }

    public void setVTextAlignment(Paint.Align align) {
        mVTextAlignement = align;
        mbUpdateTextPosition = true;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        //mEditable.clear();
        //mEditable.append(mText);
    }

    public void setBorder(boolean bVal, int size, int color) {
        mbHasBorder = bVal;
        mBorder.setStrokeSize(size);
        mBorder.setColor(color);

        mbUpdateTextPosition = true;

        if (bVal) {
            mbUpdateBorderRect = true;
        }
    }

    public void setBackground(boolean bVal, int color) {
        mbHasBackground = bVal;
        mBackground.setColor(color);

        mbUpdateTextPosition = true;

        if (bVal) {
            mbUpdateBackgroundRect = true;
        }
    }

    public float getTextSize() {
        return mPText.getTextSize();
    }

    public void setTextSize(float textsize) {
        mPText.setTextSize(textsize);
    }

    public int getTextColor() {
        return mPText.getColor();
    }

    public void setTextColor(int color) {
        mPText.setColor(color);
    }

    // Aggiorna anche il bordo e richiama la setrect
    public void SetRect(RectF r) {
        super.setRect(r);
        mbUpdateBorderRect = true;
        mbUpdateBackgroundRect = true;
    }

    // Aggiorna anche il bordo e richiama la setrect
    public void setRect(int l, int t, int r, int b) {
        super.setRect(l, t, r, b);
        mbUpdateBorderRect = true;
        mbUpdateBackgroundRect = true;
    }

    public void draw(Canvas c) {
        if (mbHasBackground) {
            if (mbUpdateBackgroundRect) {
                SynchronizeBackgroundRect();
                mbUpdateBackgroundRect = false;
            }

            mBackground.draw(c);
        }

        if (mbUpdateTextPosition) {
            UpdateTextPosition();
            mbUpdateTextPosition = false;
        }

        if (mbHasBorder) {
            if (mbUpdateBorderRect == true) {
                SynchronizeBorderRect();
                mbUpdateBorderRect = false;
            }

            mBorder.draw(c);
        }

        if (mText != null)
            c.drawText(mText, mTextOrigin.x, mTextOrigin.y, mPText);

       /* Paint p = new Paint();
        p.setColor(Color.RED);
        c.drawPoint(mTextOrigin.x, mTextOrigin.y, p);*/
    }

    public void save(DataOutputStream ds) throws IOException {
        super.save(ds);

        mBorder.save(ds);

        ds.writeBoolean(mbHasBorder);

        mBackground.save(ds);

        ds.writeBoolean(mbHasBackground);

        ds.writeUTF(mText);
        ds.writeInt(mPText.getColor());
        ds.writeFloat(mPText.getTextSize());

        int iValue = 0;

        if (mHTextAlignement == Paint.Align.LEFT) {
            iValue = 1;
        } else if (mHTextAlignement == Paint.Align.CENTER) {
            iValue = 2;
        } else {
            iValue = 3;
        }

        ds.writeInt(iValue);

        iValue = 0;
        if (mVTextAlignement == Paint.Align.LEFT) {
            iValue = 1;
        } else if (mVTextAlignement == Paint.Align.CENTER) {
            iValue = 2;
        } else {
            iValue = 3;
        }

        ds.writeInt(iValue);

        mTextOrigin.save(ds);

        ds.writeBoolean(mbSoftkeyboardFastInput);
        ds.writeBoolean(mbAutoclose);
    }

    public void load(DataInputStream ds) throws IOException {
        super.load(ds);

        mBorder.load(ds);
        mbHasBorder = ds.readBoolean();

        mBackground.load(ds);

        mbHasBackground = ds.readBoolean();

        mText = ds.readUTF();
        mEditable.clear();
        mEditable.append(mText);

        mPText.setColor(ds.readInt());
        mPText.setTextSize(ds.readFloat());

        int iValue = ds.readInt();
        switch (iValue) {
            case 1: {
                mHTextAlignement = Paint.Align.LEFT;
            }
            break;
            case 2: {
                mHTextAlignement = Paint.Align.CENTER;
            }
            break;
            case 3: {
                mHTextAlignement = Paint.Align.RIGHT;
            }
            break;
        }

        iValue = ds.readInt();

        switch (iValue) {
            case 1: {
                mVTextAlignement = Paint.Align.LEFT;
            }
            break;
            case 2: {
                mVTextAlignement = Paint.Align.CENTER;
            }
            break;
            case 3: {
                mVTextAlignement = Paint.Align.RIGHT;
            }
            break;
        }

        mTextOrigin.load(ds);

        mbSoftkeyboardFastInput = ds.readBoolean();
        mbAutoclose = ds.readBoolean();

        // force graphical update
        mbUpdateTextPosition = true;
        mbUpdateBorderRect = true;
        mbUpdateBackgroundRect = true;
    }

    public boolean isInside(float x, float y) {
        RectF boundF;

        if (mbHasBackground) {
            boundF = mBackground.getRectF();
        } else if (mbHasBorder) {
            boundF = mBorder.getRectF();
        } else {
            Rect bound = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length() - 1, bound);
            boundF = new RectF(bound);
            boundF.offset(mTextOrigin.x, mTextOrigin.y);
        }

        return boundF.contains(x, y);
    }

    @Override
    public void getResult(String sText) {
        setText(sText);
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        getEditorInfo(outAttrs);
        GInputConnection gic = new GInputConnection(mView, false);
        gic.setReceiver(this);

        return gic;
    }

    @Override
    public void openInputSource(boolean bAutoclose) {

        mbAutoclose = bAutoclose;

        mView.setFocusable(true);
        mView.setFocusableInTouchMode(true);

        mView.requestFocus();
        mView.setInputReceiver(this);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean b = imm.showSoftInput(mView, 0, new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                int out = 0;
                switch (resultCode) {
                    case InputMethodManager.RESULT_UNCHANGED_SHOWN: {
                        out = 1;
                    }
                    break;
                    case InputMethodManager.RESULT_UNCHANGED_HIDDEN: {
                        out = 2;
                    }
                    break;
                    case InputMethodManager.RESULT_SHOWN: {
                        out = 3;
                    }
                    break;
                    case InputMethodManager.RESULT_HIDDEN: {
                        out = 4;
                    }
                    break;
                    default: {
                        out = 5;
                    }
                }
            }
        });

        CompletionInfo[] ci = new CompletionInfo[2];
        ci[0] = new CompletionInfo(1,0,"ugo");
        ci[1] = new CompletionInfo(2,1,"uga");
        imm.displayCompletions(mView, ci);
    }

    @Override
    public void closeInputSource() {

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0, new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                int out = 0;
                switch (resultCode) {
                    case InputMethodManager.RESULT_UNCHANGED_SHOWN: {
                        out = 1;
                    }
                    break;
                    case InputMethodManager.RESULT_UNCHANGED_HIDDEN: {
                        out = 2;
                    }
                    break;
                    case InputMethodManager.RESULT_SHOWN: {
                        out = 3;

                    }
                    break;
                    case InputMethodManager.RESULT_HIDDEN: {
                        out = 4;
                    }
                    break;
                    default: {
                        out = 5;
                    }
                }
            }
        });

        mView.clearFocus();
        mView.setFocusable(false);
        mView.setFocusableInTouchMode(false);
    }

    public void setSoftkeyboardFastInput(boolean bEnable) {
        mbSoftkeyboardFastInput = bEnable;
    }

    // Restituisce la struttura parametri per tastiera ime.
    public void getEditorInfo(EditorInfo outAttrs) {
        // outAttrs.actionLabel = "action label";
        outAttrs.hintText = getText();
        outAttrs.initialCapsMode = 0;
        outAttrs.initialSelEnd = getText().length();
        outAttrs.initialSelStart = 0;
        // outAttrs.label = "label";

        //outAttrs.imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED | EditorInfo.IME_FLAG_NO_EXTRACT_UI;
        //outAttrs.imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED;
        outAttrs.imeOptions = EditorInfo.IME_NULL;

        if (mbSoftkeyboardFastInput) {
            outAttrs.inputType = InputType.TYPE_CLASS_TEXT;  //| InputType.TYPE_TEXT_FLAG_NO_SUGGESTION;
        } else {
            outAttrs.inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }


        //
        // outAttrs.inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
    }

    public boolean isAutoclose() {
        return mbAutoclose;
    }

    public Editable getEditable() {
        return mEditable;
    }

    public void performEditorAction()
    {
        if (mbAutoclose) {
            closeInputSource();
        }
        return;
    };



}
