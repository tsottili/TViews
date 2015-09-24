package toms.lib.libtcommon;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by toms on 21/09/13.
 * Base class for Internal File Management
 */
public class TInternalFileManager
{

	protected Activity m_Owner = null;


	protected int m_ErrorCode = 0;
	final protected int OPERATION_SUCCESS = 1;
	final protected int ACTIVITY_OWNER_NOT_DEFINED = -1;


	public TInternalFileManager(Activity owner)
	{
		m_Owner = owner;
	}

	public boolean FileExist(String sFileName)
	{
		if (m_Owner == null)
		{
			m_ErrorCode = ACTIVITY_OWNER_NOT_DEFINED;
			return false;
		}

		m_ErrorCode = OPERATION_SUCCESS;
		String[] file_list = m_Owner.fileList();

		for (String file : file_list) {
			if (sFileName.compareTo(file) == 0) {
				return true;
			}
		}

		return false;
	}

	public int GetErrorCode()
	{
		return m_ErrorCode;
	}

	public ArrayList<String> GetInternalFileList()
	{
		ArrayList<String> filelist = new ArrayList<>();

		String[] file_list = m_Owner.fileList();
		for (int i = 0; i < file_list.length; i++)
		{
			filelist.add(i, file_list[i]);
		}
		return filelist;
	}


}

