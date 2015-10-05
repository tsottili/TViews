package toms.lib.libtcommon;


/**
 * Created by toms on 27/08/14.
 * Codici di errore generici della librera TCore.
 */
public enum TErrorCode {
    SUCCESS(0),
    ACTIVITY_OWNER_NOT_DEFINED(1),
    FILE_NOT_FOUND(2),
    ERROR_READ_FILE(3),
    ERROR_WRITE_FILE(4),;


    private final int mCode;

    protected String mMessage;

    protected int mIntParameter;

    private TErrorCode(int code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String str) {
        mMessage = str;
    }

    public int getIntParameter() {
        return mIntParameter;
    }

    public void setIntParameter(int iPar) {
        mIntParameter = iPar;
    }
}
