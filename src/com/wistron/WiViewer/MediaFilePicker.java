package com.wistron.WiViewer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utilities.CSStaticData;
import android.util.Log;

/**
 * 读取指定文件夹，为PhotoViewer提供图片路徑
 * 
 * @author hubert
 * @since 2011-09-08 09:26:45
 * 
 */
public class MediaFilePicker
{
	public static final int TYPE_MOVIE = 0x01;
	public static final int TYPE_PICTURE = 0x02;
	public static final int TYPE_UNKNOWN = 0x03;
	private List<String> m_filelist = new ArrayList<String>(); // 媒体文件列表
	private String m_currentfilename = null; // 当前文件名称
	private int m_currentfileindex = 0; // 当前文件（在文件列表中的索引）
	private int m_filecounts = 0; // 文件总数
	private List<String> suffixList = new ArrayList<String>();
	private String mediaLib = null; // 默认的媒体文件夹
	private String TAG = " MediaFilePicker";
	private FileOperation fileOperation;
	private List<File> fileList = new ArrayList<File>();
	// 文件种类有待补充
	private String[] extensionMov =
	{ "mp4", "3gp", "3g2", "ts", "mkv", "m4v", "webm", "avi", "xvid", "divx",
			"wmv", "asf" };// ts mkv
	private String[] extensionPic =
	{ "jpg", "jpeg", "png", "bmp", "jps", "gif", "mpo", "webp", "wbmp", "jpe" };// gif

	/**
	 * 根據文件列表和当前文件路径实例化
	 * 
	 * @author WH1107011
	 * @param List
	 *            <String> fileList
	 * @param String
	 *            currentFileName
	 */
	public MediaFilePicker(List<String> fileList, String currentFileName)
	{
		if (fileList == null)
			return;

		this.m_filelist = fileList;
		this.m_currentfilename = currentFileName;
		this.m_filecounts = fileList.size();
		setCurrentFileName(currentFileName);// 设置当前索引
	}

	public MediaFilePicker(List<String> fileList, int index)
	{
		if (fileList == null)
			return;

		this.m_filelist = fileList;
		this.m_filecounts = fileList.size();
		this.m_currentfileindex = index;
		if (index == -1)
		{
			this.m_currentfilename = null;
		} else
		{
			this.m_currentfilename = m_filelist.get(index);
		}
	}

	public MediaFilePicker(String currentFileName)
	{
		if (currentFileName == null)
			return;

		List<String> fileList = new ArrayList<String>();
		fileList.add(currentFileName);
		this.m_filelist = fileList;
		this.m_currentfilename = currentFileName;
		this.m_filecounts = fileList.size();
		setCurrentFileName(currentFileName);// 设置当前索引
	}

	public void updateCurrentFilePath(String filepath)
	{
		if (filepath != null)
		{
			m_filelist.set(m_currentfileindex, filepath);
			m_currentfilename = filepath;
		}
	}

	/**
	 * 在媒体链表中过滤出视频文件
	 * 
	 * @return
	 */
	public ArrayList<String> getVideoList()
	{
		ArrayList<String> mVideoList = new ArrayList<String>();
		for (int i = 0; i < m_filelist.size(); i++)
		{
			if (getMediaType(i) == TYPE_MOVIE)
			{
				mVideoList.add(m_filelist.get(i));
			}
		}
		return mVideoList;
	}

	public MediaFilePicker(int viewState, String currentFileName)
			throws IOException
	{

		// 扫描默认媒体目录
		mediaLib = TDStaticData.ROOT_DIR;

		if (!new File(mediaLib).exists())
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "ROOT_DIR is not exist");
			return;
		} else if (new File(mediaLib).list() == null)
		{
			if (CSStaticData.DEBUG)
				Log.e(TAG, "ROOT_DIR is empty");
			return;
		}

		fileOperation = new FileOperation(mediaLib);

		if (viewState == TDStaticData.VIEWMODE_ALL_VIEW)
		{
			suffixList.clear();
			suffixList.add(".jps");
			suffixList.add(".jpg");
			suffixList.add(".jpeg");
			suffixList.add(".png");
			suffixList.add(".mpo");
			suffixList.add(".bmp");
			suffixList.add(".webp");

			suffixList.add(".mp4");
			suffixList.add(".3gp");
			suffixList.add(".3g2");
			suffixList.add(".m4v");
			suffixList.add("webm");

			fileList = fileOperation.getFileListBySuffix(suffixList, null);
			m_filecounts = fileList.size();
		}
		if (viewState == TDStaticData.VIEWMODE_IMAGE_VIEW)
		{
			suffixList.clear();
			suffixList.add(".jps");
			suffixList.add(".jpg");
			suffixList.add(".jpeg");
			suffixList.add(".png");
			suffixList.add(".mpo");
			suffixList.add(".bmp");
			suffixList.add(".gif");
			suffixList.add(".webp");
			fileList = fileOperation.getFileListBySuffix(suffixList, null);
			m_filecounts = fileList.size();
		}
		if (viewState == TDStaticData.VIEWMODE_VIDEO_VIEW)
		{
			suffixList.clear();
			suffixList.add(".mp4");
			suffixList.add(".3gp");
			suffixList.add(".3g2");
			suffixList.add(".m4v");
			suffixList.add("webm");
			fileList = fileOperation.getFileListBySuffix(suffixList, null);
			m_filecounts = fileList.size();
		}

		for (int i = 0; i < fileList.size(); i++)
		{
			m_filelist.add(fileList.get(i).getAbsolutePath().toString());
		}
		if (currentFileName == null)
		{
			currentFileName = m_filelist.get(0);
		}

		setCurrentFileName(currentFileName);

	}

	/**
	 * 清空文件列表
	 */
	public void clear()
	{
		m_filecounts = 0;
		m_filelist.clear();
		m_currentfileindex = 0;
		m_currentfilename = null;
	}

	/**
	 * 得到文件路径列表
	 */
	public List<String> getfilepathList()
	{
		return this.m_filelist;

	}

	public void setCurrentFileName(String currentFileName)
	{
		this.m_currentfilename = currentFileName;
		// 把当前文件设置为传入的内容
		for (int i = 0; i < m_filelist.size(); i++)
		{
			if (m_filelist.get(i).equals(currentFileName))
			{
				m_currentfileindex = i;
				break;
			}
		}
	}

	/**
	 * 获取下一张图片的文件路径
	 * 
	 * @return 下一张图片的文件路径
	 */

	public String getNextBitmap()
	{

		if (m_filelist != null)
		{
			m_currentfileindex++; // 当前文件索引自增
			m_currentfileindex = m_currentfileindex % m_filecounts;
			m_currentfilename = m_filelist.get(m_currentfileindex);

		}
		return m_currentfilename;

	}

	/**
	 * 得到下一张图片的文件路径，而不改变当前文件索引序号
	 */
	public String getNextBitmapforBuf()
	{

		String string = null;
		if (m_filelist != null)
		{
			int i = m_currentfileindex + 1;
			i = i % m_filecounts;
			string = m_filelist.get(i);

			if (CSStaticData.DEBUG)
				Log.e(TAG, "next index =" + i);
		}
		return string;

	}

	/**
	 * 获取上一张图片的文件路径
	 * 
	 */
	public String getPreBitmap()
	{
		if (m_filelist != null)
		{
			m_currentfileindex--; // 当前文件索引自减
			m_currentfileindex = (m_currentfileindex + m_filecounts)
					% m_filecounts;
			if (m_currentfileindex < 0)
			{
				m_currentfileindex = m_filecounts;
			}
			m_currentfilename = m_filelist.get(m_currentfileindex);

		}
		return m_currentfilename;

	}

	/**
	 * 得到上一张图片的文件路径，而不改变当前文件索引序号
	 */
	public String getPreBitmapforBuf()
	{
		String string = null;
		if (m_filelist != null)
		{
			int i = m_currentfileindex - 1;
			i = (i + m_filecounts) % m_filecounts;
			if (i < 0)
			{
				i = m_filecounts;
			}

			if (CSStaticData.DEBUG)
				Log.e(TAG, "pre index =" + i);

			string = m_filelist.get(i);
		}
		return string;

	}

	/**
	 * 获取第一张图片的文件路径
	 * 
	 */
	public String getFirBitmap()
	{
		if (m_filelist != null)
		{

			m_currentfilename = m_filelist.get(m_currentfileindex);
		}
		return m_currentfilename;

	}

	/**
	 * 获取当前图片的文件路径
	 * 
	 */

	public String getCurrentFileName()
	{
		// return m_filelist.get(m_currentfileindex);
		return m_currentfilename;
	}

	/**
	 * 获取当前图片的文件序号
	 * 
	 */

	public int getCurrentFileIndex()
	{
		return m_currentfileindex;
	}

	/**
	 * 获取相应文件序号的文件名
	 * 
	 */
	public String getFileName(int index)
	{
		return m_filelist.get(index);
	}

	/**
	 * 获取当前文件的大小
	 * 
	 * @return 文件大小
	 */
	public long getCurFileSize()
	{
		return new File(getCurrentFileName()).length();
	}

	public long getCurFileTime()
	{
		return new File(getCurrentFileName()).lastModified();
	}

	/**
	 * 获取文件名
	 * 
	 * @return
	 */
	public String getCurFileName()
	{
		return new File(getCurrentFileName()).getName();
	}

	/**
	 * 获取文件列表大小
	 * 
	 * @return 文件数目
	 */
	public int getFileCounts()
	{
		return m_filecounts;
	}

	/**
	 * 返回文件类型
	 */

	private int getMediaType(int index)
	{
		int type = TYPE_UNKNOWN;
		String str = null;
		if (m_filelist != null && m_filelist.size() > m_currentfileindex)
		{
			str = m_filelist.get(index);
			for (int i = 0; i < extensionMov.length; i++)
			{
				if (str.substring(str.length() - extensionMov[i].length())
						.toLowerCase().equals(extensionMov[i]))
				{
					type = TYPE_MOVIE;
					break;
				}
			}
			for (int i = 0; i < extensionPic.length; i++)
			{
				if (str.substring(str.length() - extensionPic[i].length())
						.toLowerCase().equals(extensionPic[i]))
				{
					type = TYPE_PICTURE;
					break;
				}
			}
		} else
		{
			type = TYPE_UNKNOWN;
		}

		return type;
	}

	/**
	 * 在文件列表中删除当前文件路径，如果不是最后一个则将下一文件路径作为当前路径
	 */
	public void deleteFile()
	{
		if (m_currentfileindex == -1)
		{
			return;
		}
		this.m_filelist.remove(m_currentfileindex);// 删除文件路径
		this.m_filecounts = m_filelist.size();// 更新文件数目
		if (m_filecounts != 0)
		{
			if (m_currentfileindex == m_filecounts)
			{
				m_currentfileindex--;
				m_currentfileindex = (m_currentfileindex + m_filecounts)
						% m_filecounts;
			} else
			{
				m_currentfileindex = m_currentfileindex % m_filecounts;

			}
			this.m_currentfilename = m_filelist.get(m_currentfileindex);// 更新当前文件
		} else
		{
			m_currentfilename = null;
			m_currentfileindex = -1;
		}
	}

	/**
	 * 在文件列表中删除当前文件路径，如果不是最后一个则将下一文件路径作为当前路径
	 */
	public void deleteFile(int index)
	{
		if (index == m_currentfileindex)
		{
			deleteFile();
		} else
		{
			this.m_filelist.remove(index);// 删除文件路径
			if (index < m_currentfileindex)
			{
				m_currentfileindex--;
				m_currentfilename = m_filelist.get(m_currentfileindex);
			}
			this.m_filecounts = m_filelist.size();// 更新文件数目
		}
	}

	public void deleteFileList(List<String> deletelist)
	{
		if (deletelist != null && deletelist.size() > 0)
		{
			for (int i = 0; i < deletelist.size(); i++)
			{
				String temp = deletelist.get(i);
				int a = m_filelist.size();
				for (int j = 0; j < a; j++)
				{
					if (temp.equals(m_filelist.get(j)))
					{
						deleteFile(j);
						break;
					}
				}
			}
		}
	}

	public void addFile(String filepath)
	{
		m_filelist.add(m_currentfileindex + 1, filepath);
		m_filecounts++;
	}
}
