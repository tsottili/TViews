package toms.lib.libtviews;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter test for an easytouse listview.
 * Created by toms on 08/09/15
 *
 * README:
 * derive your own adapter from this, and implement the
 * updateValue method
 *
 * public void updateValue(int position, int resource, View v)
 * .    position: the row to be updated
 *      resource: the resource int of the view to be updated
 *      v:        the view to be updated (cast it to what it is).
 */
public abstract class TEasyAdapter extends BaseAdapter {

	// Object containing layout information
	protected TLayoutInspector mInspector;

	// Current context
	protected Context mContext;

	// Row layout resource
	protected int mResource;

	// Set to true if convert view was NULL in the getView..
	protected boolean mbFirstTime;

	// Init the easy adapter. Parameters are the current context and the line layout resource
	public boolean init(Context context, int resource) {

		// save context
		mContext = context;

		// save layout resource
		mResource = resource;

		// create the Layout Inspector (used in getview)
		mInspector = new TLayoutInspector(context, resource);

		// Parse the resource layout.
		if (mInspector.parse(mResource) == false)
		{
			mInspector = null;
			return false;
		}

		// Default value
		mbFirstTime = false;

		return true;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Default value
		mbFirstTime = false;

		if( convertView == null ){
			// if convert view is null, we need to ask for the view to use.
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(mResource, parent, false);

			// ConvertView was null
			mbFirstTime = true;
		}

		for (int i = 0; i < mInspector.count(); i++)
		{
			// Find the view of each element of the line
			View v = convertView.findViewById(mInspector.getId(i));
			if (v != null) {
				// And ask for update.
				updateValue(position, mInspector.getId(i), v, mbFirstTime);
			}
		}

		// done
		return convertView;

	}

	// Implement this method in your adapter.
	//     position: the row to be updated
	//     resource: the resource int of the view to be updated
	//     v:        the view to be updated (cast it to what it is).
	abstract public void updateValue(int position, int resource, View v, boolean bFirstTime);

}