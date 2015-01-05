/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wistron.WiCamera;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Plays an AssetFileDescriptor, but does all the hard work on another thread so
 * that any slowness with preparing or loading doesn't block the calling thread.
 */
public class SoundPlayer {
	private static final String TAG = "SoundPlayer";
	// private Thread mThread;
	private MediaPlayer mPlayer;
	private int mPlayCount = 0;
	private boolean mExit;
	private AssetFileDescriptor mAfd;
	private int mAudioStreamType;

	// 播放声音
	public void run() {
		// while (true) {
		try {
			if (mPlayer == null) {
				MediaPlayer player = new MediaPlayer();
				player.setAudioStreamType(mAudioStreamType);
				player.setDataSource(mAfd.getFileDescriptor(),
						mAfd.getStartOffset(), mAfd.getLength());
				player.setLooping(false);
				player.prepare();
				mPlayer = player;
				mAfd.close();
				mAfd = null;
			}
			mPlayer.start();
		} catch (Exception e) {
			Log.e(TAG, "Error playing sound", e);
		}
		// }
	}

	// 构造方法
	public SoundPlayer(AssetFileDescriptor afd) {
		mAfd = afd;
		mAudioStreamType = AudioManager.STREAM_MUSIC;
	}

	// 传入路径
	public SoundPlayer(AssetFileDescriptor afd, boolean enforceAudible,
			int count) {
		mAfd = afd;
		this.mPlayCount = count;
		if (enforceAudible) {
			mAudioStreamType = 7; // AudioManager.STREAM_SYSTEM_ENFORCED;
									// currently hidden API.
		} else {
			mAudioStreamType = AudioManager.STREAM_MUSIC;
		}
	}

	/**
	 * 播放视频文件
	 */
	public void play() {
		run();
		synchronized (this) {
			mPlayCount++;
			notifyAll();
		}
	}

	// 释放资源
	public void release() {
		// if (mThread != null) {
		synchronized (this) {
			mExit = true;
			notifyAll();
		}
		if (mAfd != null) {
			try {
				mAfd.close();
			} catch (IOException e) {
			}
			mAfd = null;
		}
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
}
