package com.lgb.myfitness.audio;

public class WaveMaker {

    /*注意下一下正弦波数表的使用前提必须是音频采样率在22K的时候，否则，无法使用这个数表进行音频通信！！！*/
    /** 正弦波数表的长度*/
    public static final int START_H = 99;
    public static final int START_L = 99;
    public static final int DATA_H = 12;
    public static final int DATA_L0 = 13;
    public static final int DATA_L1 = 38;
    /** 正弦波数表 */
    public static final byte start_H[] = { 
    	(byte) 0x7F, (byte) 0x83,
	    (byte) 0x87, (byte) 0x8B, (byte) 0x8F, (byte) 0x93, (byte) 0x97,
	    (byte) 0x9A, (byte) 0x9E, (byte) 0xA2, (byte) 0xA6, (byte) 0xAA,
	    (byte) 0xAE, (byte) 0xB1, (byte) 0xB5, (byte) 0xB9, (byte) 0xBC,
	    (byte) 0xC0, (byte) 0xC3, (byte) 0xC7, (byte) 0xCA, (byte) 0xCD,
	    (byte) 0xD0, (byte) 0xD3, (byte) 0xD6, (byte) 0xD9, (byte) 0xDC,
	    (byte) 0xDE, (byte) 0xE1, (byte) 0xE4, (byte) 0xE6, (byte) 0xE8,
	    (byte) 0xEA, (byte) 0xEC, (byte) 0xEE, (byte) 0xF0, (byte) 0xF2,
	    (byte) 0xF4, (byte) 0xF5, (byte) 0xF7, (byte) 0xF8, (byte) 0xF9,
	    (byte) 0xFA, (byte) 0xFB, (byte) 0xFC, (byte) 0xFC, (byte) 0xFD,
	    (byte) 0xFD, (byte) 0xFD, (byte) 0xFD, (byte) 0xFD, (byte) 0xFD,
	    (byte) 0xFD, (byte) 0xFD, (byte) 0xFC, (byte) 0xFC, (byte) 0xFB,
	    (byte) 0xFA, (byte) 0xF9, (byte) 0xF8, (byte) 0xF7, (byte) 0xF5,
	    (byte) 0xF4, (byte) 0xF2, (byte) 0xF0, (byte) 0xEE, (byte) 0xEC,
	    (byte) 0xEA, (byte) 0xE8, (byte) 0xE6, (byte) 0xE4, (byte) 0xE1,
	    (byte) 0xDE, (byte) 0xDC, (byte) 0xD9, (byte) 0xD6, (byte) 0xD3,
	    (byte) 0xD0, (byte) 0xCD, (byte) 0xCA, (byte) 0xC7, (byte) 0xC3,
	    (byte) 0xC0, (byte) 0xBC, (byte) 0xB9, (byte) 0xB5, (byte) 0xB1,
	    (byte) 0xAE, (byte) 0xAA, (byte) 0xA6, (byte) 0xA2, (byte) 0x9E,
	    (byte) 0x9A, (byte) 0x97, (byte) 0x93, (byte) 0x8F, (byte) 0x8B,
	    (byte) 0x87, (byte) 0x83 };

    public static final byte data_H[] = { (byte) 0x7F, (byte) 0x9F,
	    (byte) 0xBE, (byte) 0xD8, (byte) 0xEC, (byte) 0xF9, (byte) 0xFD,
	    (byte) 0xF9, (byte) 0xEC, (byte) 0xD8, (byte) 0xBE, (byte) 0x9F,
	    (byte) 0x7F };
    public static final byte data_L0[] = { (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80 };
    public static final byte data_L1[] = { (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80 };
    public static final byte start_L[] = { (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80,
	    (byte) 0x80, (byte) 0x80 };

    // 根据待编码的数据进行编码，其实就是将波形进行整合，方便后面的AudioTrack进行播放
    public static byte[] makeWave(byte[] wave, int length, byte sendData[]) {
		int increase_step;
		int p;
		// wave 0 - 999 value confirm
		for (p = 0; p < 1000; p++) {
			wave[p] = 127;
		}
		
		// wave 1000 - 1024 confirm
		for (; p < 1025; p++) {
			wave[p] = (byte) (127 + 1000 - p);
		}
		    
		// 引导码
		increase_step = start_H.length;
		// wave 加入 start_H
		System.arraycopy(start_H, 0, wave, p, increase_step);
		
		p = p + increase_step;
		increase_step = start_L.length;
		// wave 加入 start_L
		System.arraycopy(start_L, 0, wave, p, increase_step);
		p = p + increase_step;
	
		for (int i = 0; i < sendData.length; i++) {
			// byte 转化成 int
		    int tempInt = byte2int(sendData[i]);
		    for (int j = 0; j < 8; j++) {
		    	// 判断第0位
				if ((tempInt & 0x01) == 0x01) {
				    // 1
				    increase_step = data_H.length;
				    System.arraycopy(data_H, 0, wave, p, increase_step);
				    p = p + increase_step;
				    increase_step = data_L1.length;
				    System.arraycopy(data_L1, 0, wave, p, increase_step);
				    p = p + increase_step;
				} else {
				    // 0
				    increase_step = data_H.length;
				    System.arraycopy(data_H, 0, wave, p, increase_step);
				    p = p + increase_step;
				    increase_step = data_L0.length;
				    System.arraycopy(data_L0, 0, wave, p, increase_step);
				    p = p + increase_step;
				}
				// 右移一位
				tempInt = tempInt >> 1;
		    }
	
		}
		
		increase_step = data_H.length;
		System.arraycopy(data_H, 0, wave, p, increase_step);
		
		p = p + increase_step;
		increase_step = data_L1.length;
		System.arraycopy(data_L1, 0, wave, p, increase_step);
		
		// 校验码
		p = p + increase_step;
		int temp = 0x80 / (length - p);
		for (; p < length; p++) {
			wave[p] = (byte) (0x80 + (length - p) * temp);
		}
		    
		return wave;
    }

    //java里面的Byte是带符号数，不能直接进行解码运算所以我先将其转成正数
    private static int byte2int(byte pram) {
		if (pram >= 0)
		    return pram;
		else {
		    return 256 + pram;
		}
    }

}
