package toms.lib.libtgviews;

/**
 * Created by toms on 04/10/14.
 */
public class GThread extends Thread {

    protected boolean mbRunning = false;

    public GThread() {
        super();
        mbRunning = false;
    }


    @Override
    public void start()
    {
        if (!mbRunning)
            super.start();
    }

    public void exit()
    {
        if (mbRunning)
        {
            mbRunning = false;
            try {
                this.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean running()
    {
        return mbRunning;
    }


}
