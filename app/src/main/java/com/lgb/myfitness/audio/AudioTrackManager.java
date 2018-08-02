package com.lgb.myfitness.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTrackManager {

	AudioTrack audioTrack;
	/** 总长度**/
	int length;
	/** 正弦波**/
	byte[] wave;
	public AudioTrackManager(){
	    
	}

	/**
	 * 设置频率
	 * @param rate
	 */
	public void setWavaData(byte dat[]){
		stop();
		  	// 
			length = android.media.AudioTrack.getMinBufferSize(22000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
				    AudioFormat.ENCODING_PCM_8BIT); 
			if(length < 19000) {
				length = 19000;
			}
			    
			audioTrack = new AudioTrack(
		    		AudioManager.STREAM_MUSIC, 
		    		22000,
		    		AudioFormat.CHANNEL_CONFIGURATION_MONO, // 单声道
		    		AudioFormat.ENCODING_PCM_8BIT, 
		    		length, 
		    		AudioTrack.MODE_STREAM);
		    // 左右声道 最大音量
		    audioTrack.setStereoVolume(1f, 1f);
		    wave = new byte[length];    
			    
			//产生调制后的波形
			wave = WaveMaker.makeWave(wave, length, dat);
	}

	/**
	 * 调制并发送数据
	 */
	public void SendDataInModulation(byte dat2send[]){
	    
	    setWavaData(dat2send);
	    
	    if(audioTrack != null){
	    	audioTrack.play();
	    }
		if(audioTrack != null){
			audioTrack.write(wave, 0, length);
		}
		
	}

	/**
	 * 停止播放
	 */
	public void stop(){
		if(audioTrack!=null){
		    audioTrack.stop();
		    audioTrack.release();
		    audioTrack=null;
		}
	}

	

}

