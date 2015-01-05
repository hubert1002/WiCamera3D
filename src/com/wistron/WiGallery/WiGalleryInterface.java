package com.wistron.WiGallery;

import java.util.List;

public class WiGalleryInterface {
			public static onGLItemListener      m_onGlItemListener      = null;
			public static onGLScenceListener    m_onGLScenceListener    = null;
			public static onGLMoveListener      m_onGLMoveListener      = null;
			public static onRequestListListener m_onRequestListListener = null;
			public static onDataListListener    m_onDataListListener    = null;
			public static BatchProcessCallBack  m_BatchProcessCallback  = null;
	
			public void setWiGalleryInterface(OnWiGalleryInterface listener) {
				m_onGlItemListener      = (onGLItemListener) listener;
				m_onGLScenceListener    = (onGLScenceListener) listener;
				m_onGLMoveListener      = (onGLMoveListener) listener;
				m_onRequestListListener = (onRequestListListener) listener;
				m_onDataListListener    = (onDataListListener) listener;
			}
			
			private interface OnWiGalleryInterface{}
			
			//批处理的回调
			public interface BatchProcessCallBack extends OnWiGalleryInterface{
				public void startProcess(int process, int max);
				public void inProcess(int process, int max);
				public void endProcess(int process, int max);
			}
	
			// 对 FileIcon 和 FileGroup 的操作回调
			public interface onGLItemListener extends OnWiGalleryInterface{
				/**
				 * FileIcon 被点击时，回调
				 * @pama fileItemName 被选中项的文件路径
				 */
				public void onFileClick(String fileItemName);
				/**
				 * GroupItem 被点击时，回调
				 * @param groupItemName 被选中项的Name
				 */
				public void onGroupClick(String groupItemName);
				/**
				 * 进入和退出多选模式，回调
				 * @param isMultiSelection
				 */
				public void onMultiSelectionModeChanged(boolean isMultiSelection);
				/**
				 * FileIcon 和 FileGroup 被删除时，回调
				 */
				public void onDelete(List<String> deleteFileList, boolean fromuesr);
			}

			// GL场景变化时回调
			public interface onGLScenceListener extends OnWiGalleryInterface{
				/**
				 * 排序方式改变时，回调
				 */
				public void onSortOrderChanged();
				/**
				 * 视图模式改变时，回调
				 */
				public void onViewModeChangded();
				/**
				 * 归类模式改变时，回调
				 */
				public void onGroupModeChanged();
				/**
				 * 从花跳到盒子，盒子跳到花时，回调
				 */
				public void onScenceChanged();
				/**
				 * 场景创建完成时，回调
				 */
				public void onScenceCreated();
				/**
				 * 场景销毁完成时，回调
				 */
				public void onScenceDestoryed();
			}

			// GL场景运动时回调
			public interface onGLMoveListener extends OnWiGalleryInterface{
				/**
				 * 开始运动时，回调
				 */
				public void onMoveStart(int currentRow, int rows);
				/**
				 * 运动时，回调
				 */
				public void onMoveing(int currentRow, int rows);
				/**
				 * 运动结束时，回调
				 */
				public void onMoveEnd(int currentRow, int rows);
				/**
				 * 设置滑动条
				 */
				public void setScrollBar(int currentRow, int rows);
			}
			
			// 请求文件列表操作结束后回调
			public interface onRequestListListener extends OnWiGalleryInterface{
				/**
				 * 读取请求文件列表操作结束后回调
				 */
				public void onRequestListReadCompleted();
				/**
				 * 写入请求文件列表操作结束后回调
				 */
				public void onRequestListWriteCompleted();
			}
			
			// 主数据列表操作时回调
			public interface onDataListListener extends OnWiGalleryInterface{
				/**
				 * 填充数据结束时，回调
				 */
				public void onDataFillCompleted();
			}
}
