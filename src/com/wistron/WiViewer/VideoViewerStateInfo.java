package com.wistron.WiViewer;

import java.util.List;

import android.R.integer;
public class VideoViewerStateInfo
{
    public int getmCurrentFileIndex()
	{
		return mCurrentFileIndex;
	}
	public boolean ismIs3DMode()
	{
		return mIs3DMode;
	}
	public List<String> getmFileList()
	{
		return mFileList;
	}
	public void setmCurrentFileIndex(int mCurrentFileIndex)
	{
		this.mCurrentFileIndex = mCurrentFileIndex;
	}
	public void setmIs3DMode(boolean mIs3DMode)
	{
		this.mIs3DMode = mIs3DMode;
	}
	public void setmFileList(List<String> mFileList)
	{
		this.mFileList = mFileList;
	}
    public int getPlayState()
	{
		return mPlayState;
	}
	public int getmCurrentProgress()
	{
		return mCurrentProgress;
	}
	public void setmIsplay(int mPlayState)
	{
		this.mPlayState = mPlayState;
	}
	public void setmCurrentProgress(int mCurrentProgress)
	{
		this.mCurrentProgress = mCurrentProgress;
	}
	private int mCurrentFileIndex=0;
	private int mPlayState=0;
    private int mCurrentProgress=0;
    private boolean mIs3DMode=false;
    private List<String> mFileList=null;
    public boolean isMute()
	{
		return isMute;
	}
	public boolean isFullScreen()
	{
		return isFullScreen;
	}
	public void setMute(boolean isMute)
	{
		this.isMute = isMute;
	}
	public void setFullScreen(boolean isFullScreen)
	{
		this.isFullScreen = isFullScreen;
	}
	private boolean isMute=false;
    private boolean isFullScreen=true;
    public int getM_init_playmode()
	{
		return m_init_playmode;
	}
	public void setM_init_playmode(int m_init_playmode)
	{
		this.m_init_playmode = m_init_playmode;
	}
	private int  m_init_playmode=0;
    public boolean isDeleteBoxShow()
	{
		return isDeleteBoxShow;
	}
	public boolean isFileInfoBoxShow()
	{
		return isFileInfoBoxShow;
	}
	public void setDeleteBoxShow(boolean isDeleteBoxShow)
	{
		this.isDeleteBoxShow = isDeleteBoxShow;
	}
	public void setFileInfoBoxShow(boolean isFileInfoBoxShow)
	{
		this.isFileInfoBoxShow = isFileInfoBoxShow;
	}
	private boolean isDeleteBoxShow=false;
    private boolean isFileInfoBoxShow=false;
	private int mSeekbarMode=0;
    public int getmSeekbarMode()
	{
		return mSeekbarMode;
	}
	public void setmSeekbarMode(int mSeekbarMode)
	{
		this.mSeekbarMode = mSeekbarMode;
	}
	public int getmDuring()
	{
		return mDuring;
	}
	public void setmDuring(int mDuring)
	{
		this.mDuring = mDuring;
	}
	private int mDuring=0;
	public VideoViewerStateInfo(int mCurrentFileIndex,int mCurrentProgress,int mPlayState,boolean mIs3DMode,List<String> mFileList,boolean ismute,boolean isfullscreen,int m_init_playmode,boolean isdeleteshow,boolean isinfoshow,int seekbarmode,int during)
    {
    	this.mCurrentFileIndex=mCurrentFileIndex;
    	this.mPlayState=mPlayState;
    	this.mCurrentProgress=mCurrentProgress;
    	this.mIs3DMode=mIs3DMode;
    	this.mFileList=mFileList;
    	this.isMute=ismute;
    	this.isFullScreen=isfullscreen;
    	this.m_init_playmode=m_init_playmode;
    	this.isDeleteBoxShow=isdeleteshow;
    	this.isFileInfoBoxShow=isinfoshow;
    	this.mSeekbarMode=seekbarmode;
    	this.mDuring=during;
    }
    
}
