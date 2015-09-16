package toms.tviews;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import toms.lib.libtviews.TEasyAdapter;

/**
 * Created by toms on 15/09/15.
 * TEasyAdapter implementation example.
 * Fill the list of the Views in this project
 */
public class ConcreteAdapter extends TEasyAdapter {

	protected ArrayList<String> mNames;
	protected ArrayList<Integer> mImages;
	protected ArrayList<Class<?>> mTryActivity;
	protected Activity mActivity;


	public ConcreteAdapter()
	{
		mNames = new ArrayList<>();
		mImages = new ArrayList<>();
		mTryActivity = new ArrayList<>();
	}

	public void setParentActivity(Activity current)
	{
		mActivity = current;
	}

	@Override
	public void updateValue(int position, int resource, View v) {
		if (resource == R.id.itemName)
		{
			TextView t = (TextView)v;

			t.setText(mNames.get(position));
			t.setTextColor(Color.RED);

		}
		else if (resource == R.id.item_icon)
		{
			((ImageView)v).setImageResource(mImages.get(position));
		}
		else if (resource == R.id.button_try)
		{
			final int currentPosition = position;
			Button b = (Button)v;

			b.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Class<?> cls = mTryActivity.get(currentPosition);
					if (cls != null) {
						Intent intent = new Intent(mActivity, cls);
						mActivity.startActivity(intent);
					}
				}
			});
		}
	}

	@Override
	public int getCount() {
		return mNames.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void addItem(String Name, int Image, Class<?> cls)
	{
		mNames.add(Name);
		mImages.add(Image);
		mTryActivity.add(cls);
	}
}
