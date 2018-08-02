package com.lgb.myfitness.audio;

import com.lgb.myfitness.global.Global;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

public class RecordTask extends AsyncTask<Void, Object, Void> {
	
	private String TAG = "RecordTask";
	
	Activity mMainActivity;

	boolean isRecord;
	private AudioRecord recAudioRecord;
	private int recBufSize;
	private int SampleRate = 8000;
	private int recSource = MediaRecorder.AudioSource.MIC;
	private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
	private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
	private AudioDecode mDecoder;
	private boolean datagetok;

	// private ArrayList<short[]> inBuf = new ArrayList<short[]>();
	public RecordTask(Activity mainActivity) {
		isRecord = true;
		mMainActivity = mainActivity;
		mDecoder = new AudioDecode();
	}

	@Override
	protected Void doInBackground(Void... params) {

		recBufSize = AudioRecord.getMinBufferSize(SampleRate, channelConfig, mAudioFormat);
		if (recBufSize < 4096) {
			recBufSize = 4096;
		}
			
		recAudioRecord = new AudioRecord(recSource, SampleRate, channelConfig, mAudioFormat, recBufSize);
		datagetok = false;
		try {

			short[] buffer = new short[recBufSize];
			short[] buffer1 = new short[recBufSize];
			short[] buffer2 = new short[recBufSize];
			short[] buffer3 = new short[recBufSize];
			short[] buffer4 = new short[recBufSize];
			short[] buffer5 = new short[recBufSize];
			short[] buffer6 = new short[recBufSize];
			short[] buffer7 = new short[recBufSize];
			short[] buffer8 = new short[recBufSize];
			short[] buffer9 = new short[recBufSize];
			short[] buffer10 = new short[recBufSize];
			short[] buffer11 = new short[recBufSize];

			recAudioRecord.startRecording();

			while (isRecord) {

				int bufferReadSize0 = recAudioRecord.read(buffer, 0, recBufSize);
				int bufferReadSize1 = recAudioRecord.read(buffer1, 0, recBufSize);
				int bufferReadSize2 = recAudioRecord.read(buffer2, 0, recBufSize);
				int bufferReadSize3 = recAudioRecord.read(buffer3, 0, recBufSize);
				int bufferReadSize4 = recAudioRecord.read(buffer4, 0, recBufSize);
				int bufferReadSize5 = recAudioRecord.read(buffer5, 0, recBufSize);
				int bufferReadSize6 = recAudioRecord.read(buffer6, 0, recBufSize);
				int bufferReadSize7 = recAudioRecord.read(buffer7, 0, recBufSize);
				int bufferReadSize8 = recAudioRecord.read(buffer8, 0, recBufSize);
				int bufferReadSize9 = recAudioRecord.read(buffer9, 0, recBufSize);
				int bufferReadSize10 = recAudioRecord.read(buffer10, 0, recBufSize);
				int bufferReadSize11 = recAudioRecord.read(buffer11, 0, recBufSize);

//				System.out.println("------------ size ------------");
//				System.out.println(bufferReadSize0 + "\n" + bufferReadSize1 + "\n"
//						+ bufferReadSize2 + "\n" + bufferReadSize3 + "\n"
//						+ bufferReadSize4 + "\n" + bufferReadSize5 + "\n"
//						+ bufferReadSize6 + "\n" + bufferReadSize7 + "\n"
//						+ bufferReadSize8 + "\n" + bufferReadSize9 + "\n"
//						+ bufferReadSize10 + "\n" + bufferReadSize11 + "\n");
				
				
				fft2decode(buffer, bufferReadSize0);
				// 传递解码成功的数据
				if (mDecoder.waiting2read) {
					final Intent intent = new Intent(Global.ACTION_DECODER_OK);
					intent.putExtra("result", mDecoder.rec_data);
					mMainActivity.sendBroadcast(intent);
					Log.i(TAG, "fft2decode buffer send decoder ok.");
					mDecoder.waiting2read = false;
					datagetok = true;
					
					stopAndRelease(this, recAudioRecord);
					return null;
				}
				// if(datagetok != true)
				{
					fft2decode(buffer1, bufferReadSize1);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer1 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
				// if(datagetok != true)
				{
					fft2decode(buffer2, bufferReadSize2);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer2 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
//				if (datagetok != true) 
				{
					fft2decode(buffer3, bufferReadSize3);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer3 send decoder3 ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
				// if(datagetok != true)
				{
					fft2decode(buffer4, bufferReadSize4);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer4 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
				// if(datagetok != true)
				{
					fft2decode(buffer5, bufferReadSize5);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer5 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
				// if(datagetok != true)
				{
					fft2decode(buffer6, bufferReadSize6);
					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer6 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}
				// if(datagetok != true)
				{
					fft2decode(buffer7, bufferReadSize7);

					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer7 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}

				// if(datagetok != true)
				{
					fft2decode(buffer8, bufferReadSize8);

					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer8 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}

				// if(datagetok != true)
				{
					fft2decode(buffer9, bufferReadSize9);

					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer9 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}

				// if(datagetok != true)
				{
					fft2decode(buffer10, bufferReadSize10);

					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer10 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}

				// if(datagetok != true)
				{
					fft2decode(buffer11, bufferReadSize11);

					// 传递解码成功的数据
					if (mDecoder.waiting2read) {
						final Intent intent = new Intent(Global.ACTION_DECODER_OK);
						intent.putExtra("result", mDecoder.rec_data);
						mMainActivity.sendBroadcast(intent);
						Log.i(TAG, "fft2decode buffer11 send decoder ok.");
						mDecoder.waiting2read = false;
						datagetok = true;
						
						stopAndRelease(this, recAudioRecord);
						return null;
					}
				}

				// if(!datagetok)
				{
					final Intent intent = new Intent(Global.ACTION_TIME_OUT);
					intent.putExtra("TimeOut", 0);
					mMainActivity.sendBroadcast(intent);

				}
				isRecord = false;
//				this.cancel(true);

			}

		} catch (Throwable t) {
			Log.e("AudioRecord", "Recording Failed");
		}

//		recAudioRecord.stop();
//		recAudioRecord.release();
		
		stopAndRelease(this, recAudioRecord);
		return null;
	}
	
	
	private void stopAndRelease(AsyncTask asyncTask, AudioRecord audioRecord) {
		asyncTask.cancel(true);
		
		audioRecord.stop();
		audioRecord.release();
	}
	

	private void fft2decode(short[] buf2decode, int length) {
		int count = 0;

		// 8个8个这样处理
		for (int j = 0; j < length; j = j + 8) {

			FFT mFft = new FFT(8);
			for (int ii = 0; ii < 8; ii++) {
				mFft.dataR[ii] = (double) (buf2decode[j + ii]);
				mFft.dataI[ii] = 0;
			}
			mFft.fft();

			double[] dbuf = new double[4];
			byte maxDoubleIndex = 0;
			for (byte ii = 0; ii < 4; ii++) {
				dbuf[ii] = ((Math.sqrt(mFft.dataR[ii] * mFft.dataR[ii]
						+ mFft.dataI[ii] * mFft.dataI[ii])));
				if (dbuf[maxDoubleIndex] < dbuf[ii])
					maxDoubleIndex = ii;
			}
			// Log.d("fft", "fft: " + dbuf[0] + " " + dbuf[1] + " " + dbuf[2]
			// + " " + dbuf[3]);
			// Log.d("maxCount", "maxIndex: " + maxDoubleIndex + "Count:" +
			// count);
			// dat[count] = maxDoubleIndex;
			count++;
			mDecoder.Decode(maxDoubleIndex);
		}
		Log.d("qc", "stopfft  " + count);

	}

}