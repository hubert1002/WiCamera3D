package com.wistron.WiViewer;

import com.tridef3d.hardware.Display3D;

import android.media.MediaPlayer;

public interface VideoService
{
   boolean isPlaying();
   int getCurrentPosition();//返回当前播放进度
   int getDuration();//返回总时长
   void pause();//暂停
   void resume();//在activity onResume时调用，Onpause时调用suspend
   void	 seekTo(int msec);//跳转播放进度
   void	 setOnCompletionListener(MediaPlayer.OnCompletionListener l);
   void	 setOnErrorListener(MediaPlayer.OnErrorListener l);
   void	 setOnPreparedListener(MediaPlayer.OnPreparedListener l);
   void	 setVideoPath(String path);
   void	 start(); //播放
   void	 stopPlayback();//停止播放，一般在退出时调用，会销毁播放状态
   void	 suspend();
   void force3DMode(int mode );//设置3D控件显示模式
}
