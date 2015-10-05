package toms.lib.libtcommon;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by toms on 04/08/13.
 * Scrittura di un file dalla memoria interna.
 */

public class TInternalFileWriter extends TInternalFileManager {

    protected FileOutputStream m_fs = null;

    protected DataOutputStream mOutputStream = null;


    public TInternalFileWriter(Context c) {
        super(c);
    }

    public boolean open(String strFilename) {
        try {
            m_fs = mContext.openFileOutput(strFilename, Context.MODE_PRIVATE);
            mOutputStream = new DataOutputStream(m_fs);

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.FILE_NOT_FOUND;
            return false;
        }
    }

    public DataOutputStream getOutputStream() {
        return mOutputStream;
    }

    public boolean WriteInt(int iElem) {
        if (mOutputStream == null) return false;

        try {
            mOutputStream.writeInt(iElem);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_WRITE_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
            return false;
        }

    }

    public boolean WriteFloat(float fElem) {
        if (mOutputStream == null) return false;

        try {
            mOutputStream.writeFloat(fElem);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_WRITE_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
            return false;
        }

    }

    public boolean WriteString(String Element) {

        if (mOutputStream == null) return false;

        try {
            mOutputStream.writeUTF(Element);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_WRITE_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
            return false;
        }
    }

    public boolean WriteBoolean(boolean bVal) {
        if (mOutputStream == null) return false;

        try {
            mOutputStream.writeBoolean(bVal);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_WRITE_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
            return false;
        }
    }

    public void close() {
        try {
            mOutputStream.flush();
            mOutputStream.close();
            m_fs.flush();
            m_fs.close();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_WRITE_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
        }
    }
}
