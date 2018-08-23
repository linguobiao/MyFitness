package com.guoou.sdk.bean;

public class Cmd {

    public static byte[] getBpmCmd(int order) {
        byte[] value = initBaseCmd(6);
        value[4] = (byte) order;
        value[5] = getXor(value);
        return value;
    }

    public static byte[] getBpmLanguageCmd(int language) {
        byte[] value = initBaseCmd(7);
        value[4] = (byte) 0xa6;
        value[5] = (byte) language;
        value[6] = getXor(value);
        return value;
    }

    public static byte getXor(byte[] value) {
        byte xor = value[1];
        for (int i = 2; i < value.length - 1; i ++) {
            xor = (byte) (xor ^ value[i]);
        }
        return xor;
    }

    public static byte[] initBaseCmd(int length) {
        byte[] value = new byte[length];
        value[0] = (byte) 0x02;
        value[1] = (byte) 0x40;
        value[2] = (byte) 0xdc;
        value[3] = (byte) 0x01;
        return value;
    }
}
