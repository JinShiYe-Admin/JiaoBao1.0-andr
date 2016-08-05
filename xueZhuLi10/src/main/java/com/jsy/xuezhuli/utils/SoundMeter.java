package com.jsy.xuezhuli.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import com.jsy_jiaobao.main.JSYApplication;

import java.io.IOException;

public  class SoundMeter {
	private MediaRecorder mRecorder = null;

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			mRecorder.setOutputFile(JSYApplication.getInstance().AV_PATH+"/"+name);
			try {
				mRecorder.prepare();
				mRecorder.start();

			} catch (IllegalStateException | IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}
}