package com.wistron.WiGallery;

import java.util.List;


/**
 * Copyright (c) 2012 Wistron SWPC
 * All rights reserved.
 * @author Cocoonshu
 * @date 2012-04-19 10:26:41
 * @comment 
 * @purpose Multi-operation temp data bean
 * @detail 
 */
public class MultiTempData {
	
	private int          mSelectedFileNum  = 0;     //选中的文件个数
	private int          mSucessedFileNum  = 0;     //操作成功的文件个数
	private int          mFailedFileNum    = 0;     //操作失败的文件个数
	private int          mOperationPercent = 0;     //操作进度
	private boolean      mIsDelete         = false; //是否为删除操作
	private boolean      mIsSetFavorite    = false; //是否为设置喜好操作
	private boolean      mIsRemoveFavorite = false; //是否为移出喜好操作
	private boolean      mIsMoveToInternal = false; //是否为移动至内部空间操作
	private boolean      mIsMoveToExternal = false; //是否为移动至外部空间操作
	private List<String> mSelectedFileList = null;  //选中的文件列表
	
	/**
	 * @return the mSelectedFileNum
	 */
	public int getSelectedFileNum() {
		return mSelectedFileNum;
	}
	/**
	 * @param mSelectedFileNum the mSelectedFileNum to set
	 */
	public void setSelectedFileNum(int selectedFileNum) {
		this.mSelectedFileNum = selectedFileNum;
	}
	/**
	 * @return the mSucessedFileNum
	 */
	public int getSucessedFileNum() {
		return mSucessedFileNum;
	}
	/**
	 * @param mSucessedFileNum the mSucessedFileNum to set
	 */
	public void setSucessedFileNum(int sucessedFileNum) {
		this.mSucessedFileNum = sucessedFileNum;
	}
	/**
	 * @return the mFailedFileNum
	 */
	public int getFailedFileNum() {
		return mFailedFileNum;
	}
	/**
	 * @param mFailedFileNum the mFailedFileNum to set
	 */
	public void setFailedFileNum(int failedFileNum) {
		this.mFailedFileNum = failedFileNum;
	}
	/**
	 * @return the mOperationPercent
	 */
	public int getOperationPercent() {
		return mOperationPercent;
	}
	/**
	 * @param mOperationPercent the mOperationPercent to set
	 */
	public void setOperationPercent(int operationPercent) {
		this.mOperationPercent = operationPercent;
	}
		
	/**
	 * @return the mIsDelete
	 */
	public boolean isDelete() {
		return mIsDelete;
	}
	/**
	 * @param mIsDelete the mIsDelete to set
	 */
	public void setIsDelete(boolean isDelete) {
		this.mIsDelete = isDelete;
	}
	/**
	 * @return the mIsSetFavorite
	 */
	public boolean isSetFavorite() {
		return mIsSetFavorite;
	}
	/**
	 * @param mIsSetFavorite the mIsSetFavorite to set
	 */
	public void setIsSetFavorite(boolean isSetFavorite) {
		this.mIsSetFavorite = isSetFavorite;
		this.mIsRemoveFavorite = false;
	}
	/**
	 * @return the mIsRemoveFavorite
	 */
	public boolean isRemoveFavorite() {
		return mIsRemoveFavorite;
	}
	/**
	 * @param mIsRemoveFavorite the mIsRemoveFavorite to set
	 */
	public void setIsRemoveFavorite(boolean isRemoveFavorite) {
		this.mIsRemoveFavorite = isRemoveFavorite;
		this.mIsSetFavorite = false;
	}
	/**
	 * 
	 * @return
	 */
	public boolean isFavorite(){
		return mIsSetFavorite | mIsRemoveFavorite;
	}
	/**
	 * @return the mIsMoveToInternal
	 */
	public boolean isMoveToInternal() {
		return mIsMoveToInternal;
	}
	/**
	 * @param mIsMoveToInternal the mIsMoveToInternal to set
	 */
	public void setIsMoveToInternal(boolean isMoveToInternal) {
		this.mIsMoveToInternal = isMoveToInternal;
		this.mIsMoveToExternal = false;
	}
	/**
	 * @return the mIsMoveToExternal
	 */
	public boolean isMoveToExternal() {
		return mIsMoveToExternal;
	}
	/**
	 * @param mIsMoveToExternal the mIsMoveToExternal to set
	 */
	public void setIsMoveToExternal(boolean isMoveToExternal) {
		this.mIsMoveToExternal = isMoveToExternal;
		this.mIsMoveToInternal = false;
	}
	/**
	 * @return mIsMoveToInternal | mIsMoveToExternal
	 */
	public boolean isMoveTo() {
		return mIsMoveToInternal | mIsMoveToExternal;
	}
	/**
	 * @return the mSelectedFileList
	 */
	public List<String> getSelectedFileList() {
		return mSelectedFileList;
	}
	/**
	 * @param mSelectedFileList the mSelectedFileList to set
	 */
	public void setSelectedFileList(List<String> selectedFileList) {
		this.mSelectedFileList = selectedFileList;
	}
	/**
	 * 清空数据
	 * 还原默认值
	 */
	public void clear(){
		this.mSelectedFileNum  = 0;
		this.mSucessedFileNum  = 0;
		this.mFailedFileNum    = 0;
		this.mOperationPercent = 0;
		this.mIsSetFavorite    = false;
		this.mIsRemoveFavorite = false;
		this.mIsMoveToInternal = false;
		this.mIsMoveToExternal = false;
		if(this.mSelectedFileList != null){
			this.mSelectedFileList.clear();
			this.mSelectedFileList = null;
		}
	}
	
	
}
