package com.lgb.myfitness.audio;


public class AudioDecode {
	private final int FFT_3K = 3; // 间隔
	private final int FFT_2K = 2; // 1
	private final int FFT_1K = 1; // 0
	private byte bitget; // 解码得到的Bit信息
	private byte bitreadycnt; // 用于起始位判断计数
	private byte lastValidBit; // 上一次收到有有效的bit信息
	private int currentByteValue; // 当前的Byte信息
	private byte bitForByteCount; // bit合成BYTE的计数，当计数达到8的时候证明一个字节接收完成
	private byte Bytecount; // 已经接收下来的字节计数
	private boolean ByteReadyFlag; // 字节开始接收标记，当收到起始位之后ByteReadyFlag为1
	byte buf[] = new byte[20]; // 接收下来的字节缓存这里定义了20字节的空间，实际只会有5个字节
	// ==> 举例
	// 如果一帧数据为 0xaa 0x01 0x01 0x01 0x01 0x01 0xaf
	// 0xaa 代表帧头，五个0x01代表实际有效的数据，0xaf代表校验和
	// 即 0xaf = 0xaa + 0x01 + 0x01 + 0x01 + 0x01 + 0x01 ;

	private boolean FrameRecvStartFlag;// 帧接收结束标记
	private boolean FrameRecvFinishFlag;// 帧接收结束标记
	private int FrameRecvCount;// 用于判断是否帧头来临
	private int lastbitvalue;


	byte rec_data[] = new byte[20]; // 接收完成数据缓存
	boolean waiting2read; // 解码成功标记，当本标记为1的时候表示完成一帧的数据接收，可以读取rec_data的数据

	public AudioDecode() {
		bitreadycnt = 0;
		ByteReadyFlag = false;
		waiting2read = false;
		FrameRecvStartFlag = false;
		FrameRecvCount = 0;
		lastbitvalue = 0;
	}

	public void init() {
		bitreadycnt = 0;
		ByteReadyFlag = false;
		waiting2read = false;
		FrameRecvStartFlag = false;
		FrameRecvCount = 0;
		lastbitvalue = 0;
	}

	// 解码函数，里面的逻辑还有待优化
	public void Decode(byte highestFreIndex) {
		if (highestFreIndex == FFT_1K) {
			bitget = 0; // bit0
		} else if (highestFreIndex == FFT_2K) {
			bitget = 1; // bit1
		} else if (highestFreIndex == FFT_3K) {
			bitget = 2; // 间隔码
		}
//		Log.d("AudioDebug", "bitget" + bitget);
		if (FrameRecvStartFlag == false) {
			// 还没收到帧头
			if (bitget == 2) {
				FrameRecvCount++;
				if (FrameRecvCount == 14) {
					// 连续接收到18个‘2’，证明收到帧头了
					FrameRecvStartFlag = true;
					ByteReadyFlag = true;
					FrameRecvCount = 0;
					Bytecount = 0;
					
					// 接下来要开始接收数据了，字节接收的变量初始化
					lastValidBit = 2;
					currentByteValue = 0;
					
//					Log.d("AudioDebug","Start Frame");
				}
				lastbitvalue = bitget;// 记录下本次收到的数据为2，方便容错
			} else {
				if ((lastbitvalue != 2) && (bitget != 2)) {
					// 上一次和这一次收到的都不是2
					FrameRecvCount = 0;
				} else {
					lastbitvalue = bitget;// 记录下这次的值，如果下次的值仍旧不是2，那么就认为没有收到帧头
				}

			}
		} else {
			// 接收完成帧头开始接收数据
			if (ByteReadyFlag == false) {
				// 还没收到起始位
				if (bitget == 2) {
					bitreadycnt++;
					if (bitreadycnt ==5) {
						ByteReadyFlag = true;
						bitForByteCount = 0;
						lastValidBit = 2;
					}
				} else {
					if((lastValidBit!=2)&&(bitget!=2))
						bitreadycnt = 0;
					lastValidBit = bitget;
				}
			} else {
				// 接收到起始位了
				if (bitget != 2 && lastValidBit == 2) {
					lastValidBit = bitget;
//					Log.d("AudioDebug","bit"+bitForByteCount+" :"+bitget);
					currentByteValue = currentByteValue * 2 + bitget;
					bitForByteCount++;
					if (bitForByteCount == 8) {
						bitForByteCount = 0;
//						Log.d("AudioDebug","Byte  :"+currentByteValue);
						buf[Bytecount] = (byte) currentByteValue;
						currentByteValue = 0;
						Bytecount++;
						if (Bytecount == 20) {
							FrameRecvFinishFlag = true;
							if ((buf[0] == (byte)0xaa)) {
								int sum = 0;//检验和
								for(int iii=0;iii<19;iii++)
								{
									if(buf[iii]<0)
									{
										sum = sum + 256 + buf[iii];
									}else {
										sum = sum + buf[iii];
									}
								}
								sum = sum % 256;
								if((byte)sum == buf[19])
								{
									for (int i = 0; i < 20; i++) {
										rec_data[i] = buf[i];
									}
									waiting2read = true;
								}
							}
							FrameRecvStartFlag = false;
							FrameRecvCount = 0;
						}
						bitreadycnt = 0;
						ByteReadyFlag = false;
					}

				} else {
					if((bitget!=2)&&(bitget == lastValidBit))
					{
						currentByteValue = (currentByteValue/2)*2+bitget;
					}else {
						lastValidBit = bitget;
					}
					if(bitget ==2){
						lastValidBit = 2;
					}
				}
//				if(bitget == 2)
//				{
//					FrameRecvCount ++;
//					if (FrameRecvCount == 18) {
//						// 连续接收到18个‘2’，证明收到帧头了
//						FrameRecvStartFlag = true;
//						ByteReadyFlag = true;
//						FrameRecvCount = 0;
//						Bytecount = 0;
//
//						// 接下来要开始接收数据了，字节接收的变量初始化
//						lastValidBit = 2;
//						currentByteValue = 2;
//					}
//					lastbitvalue = bitget;// 记录下本次收到的数据为2，方便容错
//				}else {
//					FrameRecvCount =0;
//				}
			}

		}
	}
}