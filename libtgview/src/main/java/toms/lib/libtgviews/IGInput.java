package toms.lib.libtgviews;

import android.text.Editable;
import android.view.inputmethod.EditorInfo;

/**
 * Created by toms on 05/10/14.
 * interface per ricevere input dalla GInputConnection
 */
public interface IGInput {

    // Chiamata dalla commitText di InputConnection
    public void getResult(String sText);

    // Richiesta di apertura della risorsa di input
    public void openInputSource(boolean bAutoClose);

    // Richiesta di chiusura della risorsa di input
    public void closeInputSource();

    // Restituisce la struttura parametri per tastiera ime.
    public void getEditorInfo(EditorInfo outAttrs);

    public String getText();

    public boolean isAutoclose();

    public Editable getEditable();

    public void performEditorAction();
}
