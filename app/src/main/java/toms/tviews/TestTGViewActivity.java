package toms.tviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import toms.lib.libtgview.GView;

public class TestTGViewActivity extends AppCompatActivity {

    private GView mGraph;

    private TestScene myScene;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tgview);

        // Get the graph id.
        mGraph = (GView) findViewById(R.id.tgraphview);

        myScene = new TestScene(this, mGraph);

        myScene.setSpacing(5);
        myScene.setMinValue(-10);
        myScene.setMaxValue(10);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGraph.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGraph.pause();
    }

}
