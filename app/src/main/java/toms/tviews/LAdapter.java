package toms.tviews;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import toms.lib.libtcommon.TLayoutInspector;

/**
 * Adapter test for an easytouse listview.
 * Created by toms on 08/09/15.
 */
public class LAdapter extends BaseAdapter {

	protected int mCount = 0;

	protected TLayoutInspector mInspector;

	void init(Context context, int resource) {
		mInspector = new TLayoutInspector(context, resource);

		mInspector.parse(R.id.item_icon);


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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//LayoutInflater inflater;
		//inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//View rowView = inflater.inflate(mResource, parent, false);

		return null;
	}
}
