package utils;

import java.util.InputMismatchException;

public class ByteHelper {
	/**
	 * Take 1 byte to int
	 * 
	 * @param bytes
	 * @return int
	 */
	public static int byteToInt(byte[] b) {
		return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}

	public static byte[] intToByte(int value) {
		byte[] result = new byte[4];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte)(value >>> 8*(result.length-i-1));
		}
		return result;
	}
	
	public static byte[] longToByteArray(long value) {
		byte[] result = new byte[8];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte)(value >>> 8*(result.length-i-1));
		}
		return result;
	}
	
	
	public static byte[] subByte(byte[] b, int beginingIndex, int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = b[beginingIndex+i]; 
		}
		return result;
	}
	
	public static byte[] concatByte(byte[] originB, byte[] addB){
		if (originB == null) {
			return addB.clone();
		}
		
		byte[] result = new byte[originB.length+addB.length];
		for (int i = 0; i < result.length; i++) {
			if (i < originB.length) {
				result[i] = originB[i];	
			} else {
				result[i] = addB[i-originB.length];
			}
		}
		return result;
	}
	
	public static byte[] xORByteArray(byte[] a, byte[] b){
		if (a.length != b.length) {
			throw new InputMismatchException("byte array sizes are differnt");
		}
		byte[] result = new byte[a.length];
		for (int i = 0; i < b.length; i++) {
			result[i] = (byte) (a[i] ^ b[i]);
		}
		return result;
	}

	public static byte[] andByteArray(byte[] a, byte[] b) {
		if (a.length != b.length) {
			throw new InputMismatchException("byte array sizes are differnt");
		}
		byte[] result = new byte[a.length];
		for (int i = 0; i < b.length; i++) {
			result[i] = (byte) (a[i] & b[i]);
		}
		return result;
	}
	
	public static byte[] mod2Power32(byte[] value){
		byte[] result = new byte[value.length];
		for (int i = 0; i < value.length; i++) {
			result[i] = (byte) (value[i] & 0xFF);
		}
		return result;
	}
	
	public static byte[] complementByteArray(byte[] b){
		byte[] result = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			result[i] = (byte) ~b[i];
		}
		return result;
	}
	
	public static byte[] mod2Power32(int value){
		byte[] result = ByteHelper.intToByte(value);
		return mod2Power32(result);
	}

}
