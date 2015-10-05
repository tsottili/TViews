package toms.lib.libtcommon;

import android.content.Context;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by toms on 21/09/13.
 * Lettura di un file dalla memoria interna.
 */
public class TInternalFileReader extends TInternalFileManager {

    protected FileInputStream m_fs = null;

    protected DataInputStream mDataInputStream = null;

    public TInternalFileReader(Context c) {
        super(c);
    }


    public boolean open(String strFilename) {
        try {
            m_fs = mContext.openFileInput(strFilename);
            mDataInputStream = new DataInputStream(m_fs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.FILE_NOT_FOUND;
            m_ErrorCode.setMessage(e.getMessage());
        }

        return true;

    }

    public DataInputStream getInputStream() {
        return mDataInputStream;
    }

    public int ReadInt() {
        try {
            return mDataInputStream.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_READ_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(1);
            return 0;
        }
    }

    public float ReadFloat() {
        try {
            return mDataInputStream.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_READ_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(2);
            return 0f;
        }
    }

    public String ReadString() {
        try {
            return mDataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_READ_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(3);
            return "";
        }
    }

    public boolean ReadBoolean() {
        try {
            return mDataInputStream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_READ_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(4);
            return false;
        }
    }

    public void close() {
        try {
            mDataInputStream.close();
            m_fs.close();
        } catch (IOException e) {
            e.printStackTrace();
            m_ErrorCode = TErrorCode.ERROR_READ_FILE;
            m_ErrorCode.setMessage(e.getMessage());
            m_ErrorCode.setIntParameter(5);
        }
    }
}
