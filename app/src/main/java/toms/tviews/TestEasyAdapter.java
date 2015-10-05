package toms.tviews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import toms.lib.libtviews.TEasyAdapter;

/**
 * Created by toms on 24/09/15.
 */
public class TestEasyAdapter extends TEasyAdapter {

	protected Activity mActivity;
	protected ListView mLv;

    protected ArrayList<String> mLabels;
	protected ArrayList<String> mValues;

	int mCount = 0;

	public TestEasyAdapter()
	{
		mLabels = new ArrayList<>();
		mValues = new ArrayList<>();
        mActivity = null;
        mLv = null;
	}

	public void setParentActivity(Activity current)
	{
		mActivity = current;
	}

    public void setParentListView(ListView lv) {mLv = lv;}

	@Override
	public void updateValue(int position, int resource, View v,  boolean bFirstTime) {
		switch (resource)
		{
			case R.id.field_name:
			{
				TextView tv = (TextView)v;
				tv.setText(mLabels.get(position));
			}
			break;
			case R.id.field_value:
			{
				EditText et = (EditText)v;
                if (bFirstTime)
				{

                    et.setText(mLabels.get(position));
					et.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
                            EditText e = (EditText)v;
                            String s = e.getText().toString();
                            int a = 10;
						}
					});
					et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
                            EditText e = (EditText) v;
                            String s = e.getText().toString();
                            if (hasFocus) {

                            }
                        }
					});

				}
				//mValues.set(position, et.getText().toString());
			}
			break;
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void addItem(String label, String value)
	{
		mLabels.add(label);
		mValues.add(value);
		mCount++;
	}
}
