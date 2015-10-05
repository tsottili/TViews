package toms.lib.libtgviews;

/**
 * Created by toms on 26/10/14.
 */

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;

public class GInputConnection extends BaseInputConnection {

    private static final String LOGCLASS = "GInputConnection2";

    protected Editable mEditable;

    protected int mInitComposingIndex = -1;
    protected int mEndComposingIndex = -1;
    protected int mCursorPosition = 0;

    protected boolean mbIsComposing = false;

    // Oggetto che ha richiesto l'input
    protected IGInput mReceiver;


    public GInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
    }

    public void setReceiver(IGInput item) {
        Log.v(LOGCLASS, "Set editable.");
        mEditable = new Editable.Factory().newEditable(item.getText()); //item.getEditable();
        mReceiver = item;
    }

    //  The default implementation places the given text into the editable, replacing
    //  any existing composing text.
    //  The new text is marked as in a composing state with the composing style.
    //
    //  Parameters
    //  text 	The composing text with styles if necessary. If no style object attached to the text, the default style for composing text is used. See Spanned for how to attach style object to the text. SpannableString and SpannableStringBuilder are two implementations of the interface Spanned.
    //  newCursorPosition 	The new cursor position around the text. If > 0, this is relative to the end of the text - 1; if <= 0, this is relative to the start of the text. So a value of 1 will always advance you to the position after the full text being inserted. Note that this means you can't position the cursor within the text, because the editor can make modifications to the text you are providing so it is not possible to correctly specify locations there.
    //  Returns
    //  true on success, false if the input connection is no longer valid.
    @Override
    public boolean setComposingText(CharSequence text,
                                    int newCursorPosition) {

        Log.v(LOGCLASS, "composing = " + text.toString() + " cursor = " + newCursorPosition);

        if (mbIsComposing) {
            mEditable.replace(mInitComposingIndex, mEndComposingIndex, text);
            mEndComposingIndex = mInitComposingIndex + text.length();
        } else {
            mEditable.append(text);
            mInitComposingIndex = mEditable.length() - text.length();
            mEndComposingIndex = mEditable.length();
            mbIsComposing = true;
        }

        mCursorPosition = newCursorPosition;

        Log.v(LOGCLASS, "current value = " + mEditable.toString());
        mReceiver.getResult(mEditable.toString());

        return true;
    }


    //  Mark a certain region of text as composing text.
    //  If there was a composing region, the characters are left as they were and the
    //  composing span removed, as if finishComposingText() has been called.
    //  The default style for composing text is used.
    //
    //  The passed indices are clipped to the contents bounds.
    //  If the resulting region is zero-sized, no region is marked and the effect is
    //  the same as that of calling finishComposingText(). The order of start and end
    //  is not important.
    //  In effect, the region from start to end and the region from end to start is the same.
    //  Editor authors, be ready to accept a start that is greater than end.
    //
    //  Since this does not change the contents of the text, editors
    //  should not call updateSelection(View, int, int, int, int) and IMEs
    //  should not receive onUpdateSelection(int, int, int, int, int, int).
    //
    //  This has no impact on the cursor/selection position. It may result in the cursor being anywhere inside or outside the composing region, including cases where the selection and the composing region overlap partially or entirely.
    //
    //  Parameters
    //  start 	the position in the text at which the composing region begins
    //  end 	the position in the text at which the composing region ends
    //  Returns
    //  true on success, false if the input connection is no longer valid.
    @Override
    public boolean setComposingRegion(int i, int i2) {

        Log.v(LOGCLASS, "setComposingRegion: i=" + i + " i2=" + i2);

        mInitComposingIndex = i;
        mEndComposingIndex = i2;

        mbIsComposing = true;

        Log.v(LOGCLASS, "ComposingRegion: " + mEditable.subSequence(i, i2));

        return true;
        // if (android.os.Build.VERSION.SDK_INT >= 11) {
        //     return super.setComposingRegion(i, i2);
        // } else {
        //     return false;
        // }
    }

    // The default implementation removes the composing state from the current editable text.
    // In addition, only if dummy mode, a key event is sent for the new text and the current
    // editable buffer cleared.
    @Override
    public boolean finishComposingText() {
        Log.v(LOGCLASS, "finishComposingText = " + mEditable.toString());

        mbIsComposing = false;
        mReceiver.getResult(mEditable.toString());

        return true;
    }


    // Default implementation replaces any existing composing text with the given text.
    // In addition, only if dummy mode, a key event is sent for the new text and the current editable buffer cleared.
    //
    // Parameters
    // text 	            The text to commit. This may include styles.
    // newCursorPosition 	The new cursor position around the text, in Java characters.
    //                      If > 0, this is relative to the end of the text - 1;
    //                      if <= 0, this is relative to the start of the text.
    //                      So a value of 1 will always advance the cursor to the position after
    //                      the full text being inserted. Note that this means you can't position
    //                      the cursor within the text, because the editor can make modifications
    //                      to the text you are providing so it is not possible to correctly
    //                      specify locations there.
    // Returns
    //                      true on success, false if the input connection is no longer valid.
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        Log.v(LOGCLASS, "commit = " + text.toString() + " new cursor position = " + newCursorPosition);

        if (mbIsComposing) {
            Log.v(LOGCLASS, "composing: init " + mInitComposingIndex + " end " + mEndComposingIndex);
            mEditable.replace(mInitComposingIndex, mEndComposingIndex, text);
            mInitComposingIndex = -1;
            mEndComposingIndex = -1;
            mbIsComposing = false;
        } else {
            Log.v(LOGCLASS, "appending " + text);
            mEditable.append(text);
        }

        mCursorPosition = newCursorPosition;
        mReceiver.getResult(mEditable.toString());

        return true;
    }

    @Override
    public boolean beginBatchEdit() {
        Log.v(LOGCLASS, "beginBatchEdit");
        return true;
    }

    @Override
    public boolean endBatchEdit() {

        Log.v(LOGCLASS, "endBatchEdit");
        return false;
    }

    @Override
    public boolean sendKeyEvent(KeyEvent keyEvent) {
        Log.v(LOGCLASS, "sendKeyEvent: " + keyEvent.toString());

        switch (keyEvent.getAction()) {
            case KeyEvent.ACTION_UP: {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.KEYCODE_DEL: {
                        if (mEditable.length() > 0) {
                            mEditable.delete(mEditable.length() - 1, mEditable.length());
                            mReceiver.getResult(mEditable.toString());
                        }
                    }
                    break;
                }
            }
            break;
        }
        return true;
    }


    //  The default implementation returns the given amount of text from the current
    //  cursor position in the buffer.

    //  Parameters
    //    length 	The expected length of the text.
    //    flags 	Supplies additional options controlling how the text is returned.
    //              May be either 0 or GET_TEXT_WITH_STYLES.
    //
    //  Returns
    //  the text before the cursor position; the length of the returned text might be less than n.
    @Override
    public CharSequence getTextBeforeCursor(int i, int i2) {

        Log.v(LOGCLASS, "getTextBeforeCursor: i=" + i + " i2=" + i2);


        String s = mEditable.toString();

        if (s.length() > i) {
            s = s.substring(s.length() - i, s.length());
        }

        Log.v(LOGCLASS, "TextBeforeCursor: " + s);
        return s;

    }

    //    The default implementation returns the given amount of text from the current
    //    cursor position in the buffer.
    //
    //    Parameters
    //    length 	The expected length of the text.
    //    flags 	Supplies additional options controlling how the text is returned.
    //              May be either 0 or GET_TEXT_WITH_STYLES.
    //
    //    Returns
    //    the text after the cursor position; the length of the returned text might be less than n.

    @Override
    public CharSequence getTextAfterCursor(int i, int i2) {
        Log.v(LOGCLASS, "getTextAfterCursor: i=" + i + " i2=" + i2);

        return "";
    }


    // The default implementation performs the deletion around the current selection
    // position of the editable text.
    //        Parameters
    // beforeLength 	The number of characters to be deleted before the current cursor position.
    // afterLength 	    The number of characters to be deleted after the current cursor position.
    //         Returns
    // true on success, false if the input connection is no longer valid.
    @Override
    public boolean deleteSurroundingText(int i, int i2) {
        Log.v(LOGCLASS, "deleteSurroundingText: i=" + i + " i2=" + i2);


        if (mCursorPosition >= 0) {
            int istart = 0;
            int iend = mEditable.length();

            istart = iend - i;
            mEditable.delete(istart, iend);
            mbIsComposing = false;
        }

        return true;
    }

    @Override
    public Editable getEditable() {
        return mEditable;
    }

    @Override
    public boolean performEditorAction(int actionCode) {
        Log.v(LOGCLASS, "performEditorAction");

        if (mReceiver != null)
        {
            mReceiver.performEditorAction();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean performContextMenuAction(int i) {
        Log.v(LOGCLASS, "performContextMenuAction");
        return true;
    }


    @Override
    public boolean clearMetaKeyStates(int i) {
        Log.v(LOGCLASS, "clearMetaKeyStates");
        return true;
    }

    @Override
    public boolean reportFullscreenMode(boolean b) {
        Log.v(LOGCLASS, "reportFullscreenMode = " + b);
        return true;
    }

    @Override
    public boolean performPrivateCommand(String s, Bundle bundle) {
        Log.v(LOGCLASS, "performPrivateCommand");
        return true;
    }

    @Override
    public CharSequence getSelectedText(int i) {
        Log.v(LOGCLASS, "getSelectedText");
        if (android.os.Build.VERSION.SDK_INT >= 9) {
            return super.getSelectedText(i);
        } else {
            return "";
        }
    }

    @Override
    public int getCursorCapsMode(int i) {
        Log.v(LOGCLASS, "getCursorCapsMode");
        return 0;
    }


    // Parameters
    // request 	Description of how the text should be returned. ExtractedTextRequest
    // flags 	Additional options to control the client, either 0 or GET_EXTRACTED_TEXT_MONITOR.
    // Returns
    // an ExtractedText object describing the state of the text view and containing the extracted
    // text itself, or null if the input connection is no longer valid of the editor can't comply
    // with the request for some reason.
    @Override
    public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
        Log.v(LOGCLASS, "getExtractedText numchar=" + request.hintMaxChars + " flags " + flags);

        ExtractedText et = new ExtractedText();
        et.text = mEditable.toString();
        return et;
    }


    // Invia suggerimenti per il completamento, dal receiver all'input connection.
    @Override
    public boolean commitCompletion(CompletionInfo text) {
        Log.v(LOGCLASS, "commitCompletion");

        return super.commitCompletion(text);
    }

    // Invia una correzione per il testo corrent, dal receiver all'input connection
    @Override
    public boolean commitCorrection(CorrectionInfo correctionInfo) {
        Log.v(LOGCLASS, "commitCorrection");

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            return super.commitCorrection(correctionInfo);
        } else {
            return false;
        }
    }

    @Override
    public boolean setSelection(int i, int i2) {
        Log.v(LOGCLASS, "setSelection");
        return super.setSelection(i, i2);
    }
}
