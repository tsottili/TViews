package toms.lib.libtcommon;

import android.app.Activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by toms on 21/09/13.
 * Open an InternalFile for reading operation
 */
public class TInternalFileReader extends TInternalFileManager
{

	protected FileInputStream m_fs = null;


	public TInternalFileReader(Activity owner)
	{
		super(owner);
	}


	public boolean OpenFile(String strFilename)
	{
		try
		{
			m_fs = m_Owner.openFileInput(strFilename);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public int ReadElement()
	{
		byte[] bytes = new byte[4];

		try
		{
			m_fs.read(bytes,0,4);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}


		int iElem = bytes[0] +
				bytes[1] * 0xFF +
				bytes[2] * 0xFFFF+
				bytes[3] * 0xFFFFFF;

		return iElem;
	}

	public String ReadElement(int iLen)
	{

		byte[] bytes = new byte[iLen];

		try
		{
			m_fs.read(bytes,0,iLen);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		String str = new String(bytes);
		return str;
	}

	public void ReadElement(ArrayList<String> sa)
	{
		int sa_size = ReadElement();

		for (int i = 0; i < sa_size; i++)
		{
			int strlength = ReadElement();
			String sElem = ReadElement(strlength);

			sa.add(i,sElem);
		}
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
