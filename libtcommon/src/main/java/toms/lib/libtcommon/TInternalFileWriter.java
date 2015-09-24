package toms.lib.libtcommon;

import android.app.Activity;
import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by toms on 04/08/13.
 */
public class TInternalFileWriter extends TInternalFileManager
{

	protected FileOutputStream m_fs = null;

	public TInternalFileWriter(Activity owner)
	{
		super(owner);
	}

	public boolean OpenFile(String strFilename)
	{
		try
		{
			m_fs = m_Owner.openFileOutput(strFilename, Context.MODE_PRIVATE);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public boolean WriteElement(int iElem)
	{
		byte[] bytes = new byte[4];

		for (int i = 0; i < 4; i++)
		{
			bytes[i] = (byte)(iElem & 0xFF);
			iElem = iElem >> 8;
		}

		try
		{
			m_fs.write(bytes,0,4);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public boolean WriteElement(String Element)
	{
		try
		{
			byte[] b = new byte[100];
			b = Element.getBytes();
			m_fs.write(Element.getBytes(),0,Element.length());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public boolean WriteElement(ArrayList<String> sa)
	{
		if (sa.size() > 0)
		{
			WriteElement(sa.size());
			for (int i = 0; i < sa.size(); i++)
			{
				int strlength = sa.get(i).length();
				WriteElement(strlength);
				WriteElement(sa.get(i));
			}
		}
		return true;
	}


	public void CloseFile()
	{
		try
		{
			m_fs.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

