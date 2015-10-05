package toms.lib.libtcommon;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by toms on 21/09/13.
 * Classe base per operazioni su file interni.
 */
public class TInternalFileManager {


    protected TErrorCode m_ErrorCode = TErrorCode.SUCCESS;

    Context mContext = null;


    public TInternalFileManager(Context c) {
        mContext = c;
    }

    public boolean FileExist(String sFileName) {
        if (mContext == null) {
            m_ErrorCode = TErrorCode.ACTIVITY_OWNER_NOT_DEFINED;
            return false;
        }

        m_ErrorCode = TErrorCode.SUCCESS;

        for (String filename : mContext.fileList()) {
            if (sFileName.compareTo(filename) == 0) {
                return true;
            }
        }

        m_ErrorCode = TErrorCode.FILE_NOT_FOUND;
        return false;
    }

    public TErrorCode GetErrorCode() {
        return m_ErrorCode;
    }

    public ArrayList<String> GetInternalFileList() {
        ArrayList<String> filelist = new ArrayList<String>();

        String[] file_list = mContext.fileList();
        for (int i = 0; i < file_list.length; i++) {
            filelist.add(i, file_list[i]);
        }
        return filelist;
    }

}
