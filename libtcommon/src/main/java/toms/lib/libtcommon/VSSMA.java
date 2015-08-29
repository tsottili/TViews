package toms.lib.libtcommon;

/**
 * Very Simple Simple Moving Average
 * Created by toms on 29/08/15.
 */
public class VSSMA {

    // SMA samples (cyclic)
    protected float mData[];

    // Actual SMA value
    protected float mMedia;

    // SMA period (number of samples)
    protected int mPeriod;

    // Internal, insert index in mdata
    private int mInsertIndex;

    // Constructor period is mandatory
    public VSSMA(int period)
    {
        // Period can't be lesser than one.
        if (period <= 0)
            period = 1;

        mData = new float[period];
        mPeriod = period;
        mInsertIndex = 0;
        mMedia = 0;
    }

    // Add a sample and return insert index.
    public int Add(float fValue)
    {
        if (mInsertIndex >= mPeriod)
        {
            mInsertIndex = 0;
        }

        float fVal = fValue/mPeriod;
        mMedia = mMedia - mData[mInsertIndex] + fVal;
        mData[mInsertIndex]= fVal;

        mInsertIndex++;

        // Restituisce l'indice di inserimento.
        return mInsertIndex-1;
    }

    // SMA value
    public float get()
    {
        return mMedia;
    }
}