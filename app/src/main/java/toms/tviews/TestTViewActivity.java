package toms.tviews;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import toms.lib.libtviews.TGraph;
import toms.lib.libtviews.TSignal;
import toms.lib.libtcommon.TTimer;

public class TestTViewActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test_tview);

	    // Initialize list view with the TEasyAdapter.
	    ListView lv = (ListView)findViewById(R.id.testListView);

	    // Create the TEasyAdapter implementation
	    ConcreteAdapter mAdapter = new ConcreteAdapter();

	    // call the init method
        mAdapter.init(getApplicationContext(), R.layout.list_item);

	    // this implementation need the parent activity to launch activities
	    mAdapter.setParentActivity(this);

	    // add the list items
		mAdapter.addItem("TGraph", R.drawable.abc_btn_check_material, TGraphActivity.class);
	    mAdapter.addItem("next view goes here", R.drawable.abc_btn_check_material, null);

	    lv.setAdapter(mAdapter);
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


