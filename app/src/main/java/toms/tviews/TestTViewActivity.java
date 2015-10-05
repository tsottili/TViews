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
	    ConcreteAdapter myAdapter = new ConcreteAdapter();

	    // call the init method
        myAdapter.init(getApplicationContext(), R.layout.list_item);

	    // this implementation need the parent activity to launch activities
	    myAdapter.setParentActivity(this);

	    // add the list items
		myAdapter.addItem("TGraph", R.drawable.abc_btn_check_material, TGraphActivity.class);
	    myAdapter.addItem("TestEasyAdapter", R.drawable.abc_btn_check_material, TestEasyAdapterActivity.class);
        myAdapter.addItem("TestTGViewActivity", R.drawable.abc_item_background_holo_dark , TestTGViewActivity.class);
	    myAdapter.addItem("next view goes here", R.drawable.abc_btn_check_material, null);

	    for (int i = 0; i < 100; i++) {
	        String s = "line " + i;
		    myAdapter.addItem(s, R.drawable.abc_btn_check_material + i, null);
	    }


	    lv.setAdapter(myAdapter);
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


