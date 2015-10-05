package toms.tviews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;

public class TestEasyAdapterActivity extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_easy_adapter);

		// Initialize list view with the TEasyAdapter.
		ListView lv = (ListView)findViewById(R.id.testEasyAdapterList);

		// Create the TEasyAdapter implementation
		TestEasyAdapter myAdapter = new TestEasyAdapter();

		// call the init method
		myAdapter.init(getApplicationContext(), R.layout.test_easy_adapter_line);

		// this implementation need the parent activity to launch activities
		myAdapter.setParentActivity(this);

		for (int i = 0; i<10; i++) {
			myAdapter.addItem("label"+i, "field"+i);
		}

		// list view allow descendant to have priority on focus (edittext)
		//lv.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		//lv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        lv.setItemsCanFocus(true);

		// associate the adapter to the listview.
		lv.setAdapter(myAdapter);
	}
}
