package toms.tviews;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import toms.lib.libtviews.TGraph;
import toms.lib.libtviews.TSignal;
import toms.lib.libtcommon.TTimer;

public class TestTViewActivity extends AppCompatActivity {

    TGraph mGraph = null;
    TSignal mySig = null;
    TSignal mySig2 = null;

    double x = 0;

    TTimer mTTimer = new TTimer()
    {
        @Override
        public void tick() {
            if (mySig != null) {

                if (x > mGraph.getXPositiveLimit()) {
                    mySig.clear();
                    mySig2.clear();
                    x = mGraph.getXNegativeLimit();
                }

                double y = Math.sin(x);

                boolean bMoveTo = (x==mGraph.getXNegativeLimit());
                mySig.addPoint((float)x, (float)y, bMoveTo);


                double x2 = -x;
                double y2 = Math.cos(x2);
                mySig2.addPoint((float)x2, (float)y2, bMoveTo);

                x=x+0.1;
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tview);

        mGraph = (TGraph)findViewById(R.id.graphview);


        x = mGraph.getXNegativeLimit();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLUE);

        mySig = new TSignal(p);

        Paint p2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p2.setStyle(Paint.Style.STROKE);
        p2.setColor(Color.RED);
        mySig2= new TSignal(p2);


        mGraph.AddSignal(mySig);
        mGraph.AddSignal(mySig2);

        mySig.enableDraw(true);
        mySig2.enableDraw(true);

        mTTimer.start(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_tview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}


