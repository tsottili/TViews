package toms.lib.libtcommon;

import android.os.Handler;
import android.os.Message;

/**
 * Created by toms on 05/08/15.
 * Basic timer class.
 */
public abstract class TTimer {

    // TTimer interval (ms)
    int mInterval = 0;

    // TTimer running command
    boolean mContinue = false;

    // TTimer runnning status
    boolean mRunning = false;

    // Constant for timer event,
    private final int TIMER_TICK_EVENT = 1;

    // Message receive handler. TTimer tick is called here.
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_TICK_EVENT:
                    // tick callback.
                    tick();
                    break;
            }

            // check the command
            if (mContinue)
            {
                // Prepare next execution
                Message m = mHandler.obtainMessage(TIMER_TICK_EVENT);
                mHandler.sendMessageDelayed(m, mInterval);
            }
            else
            {
                // Or notify the stop.
                mRunning = false;
            }
        }
    };

    // Set timer value (millisec)
    // Can be changed dinamically.
    public void setInterval(int interval)
    {
        mInterval = interval;
    }

    // Read actual timer interval value
    public int getInterval()
    {
        return mInterval;
    }

    // timer tick
    public abstract void tick();

    // Start the timer in continuos mode
    public void start(int interval)
    {
        start(interval, true);
    }

    // Stop the timer
    public void stop()
    {
        mContinue = false;

    }

    // Check timer runnign status
    public boolean isRunning()
    {
        return mRunning;
    }

    // Start the timer and let you choose between continuos or one shot mode.
    public void start(int interval, boolean bRepeat)
    {
        mInterval = interval;
        mContinue = bRepeat;

        Message m = mHandler.obtainMessage(TIMER_TICK_EVENT);
        mHandler.sendMessageDelayed(m, mInterval);

        mRunning = true;
    }

}
