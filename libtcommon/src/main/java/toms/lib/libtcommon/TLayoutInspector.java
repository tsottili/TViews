package toms.lib.libtcommon;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * TLayoutInspector: analyze an xml resource file, and give back infos.
 * Created by toms on 10/09/15.
 */
public class TLayoutInspector {

	// Target resource id.
	protected int mResource = -1;

	// current context
	protected Context mContext = null;

	ArrayList<TLayoutInspectorNode> mList = null;

	// Main constructor
	public TLayoutInspector(Context context, int iResource)
	{
		mResource = iResource;
		mContext = context;
		mList = new ArrayList<>();

	}

	// parse the resource file.
	public boolean parse(int test) {
		if ((mResource == -1) || (mContext == null)) {
			Log.v("TLayoutInspector::parse", "missing data");
			return false;
		}

		XmlResourceParser xmlp = mContext.getResources().getLayout(mResource);
		try {
			int iCurrentLevel = 0;

			// First token
			int type = xmlp.next();

			// Must be the start
			if (type != XmlPullParser.START_DOCUMENT)
			{
				return false;
			}

			// go on until documents end.
			type = xmlp.next();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
					case XmlPullParser.START_TAG: {

						TLayoutInspectorNode node = new TLayoutInspectorNode();

						node.mName = xmlp.getName();
						node.mLevel = iCurrentLevel;
						node.mId = -1;

						// search the id.
						int nCount = xmlp.getAttributeCount();
						for (int iCount = 0; iCount < nCount; iCount++)
						{
							String sName = xmlp.getAttributeName(iCount);
							if (sName.compareTo("id") == 0) {
								//ID found!
								node.mId = xmlp.getAttributeResourceValue(iCount, -1);
							}
						}

						iCurrentLevel++;

					}
					break;
					case XmlPullParser.END_TAG: {
						iCurrentLevel--;
					}
					break;
					default:

				}
				type = xmlp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		printLogCat();
		return true;
	}

	private void printLogCat()
	{
		for (int i = 0; i < mList.size(); i++)
		{
			String sLogMessage = "";
			for (int iLevel = 0; iLevel < mList.get(i).mLevel; iLevel++)
			{
				sLogMessage += " ";
			}

			sLogMessage+=mList.get(i).mName;


			Log.v("TLayoutInspector", sLogMessage);

		}

	}

}


