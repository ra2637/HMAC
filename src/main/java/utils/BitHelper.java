package utils;


public class BitHelper {

	public static boolean getBit(byte[] byteArray, int index){
		if(index >= byteArray.length*8 || index < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int arrayIndex = index/8;
		index = index%8;
		return ((byteArray[arrayIndex] << index) & 0x80) != 0;
	}
	
	// set a bit to 1 in byteArray
	public static void setBit(byte[] byteArray, int index){
		if(index >= byteArray.length*8 || index < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int arrayIndex = index/8;
		index = index%8;
		byte tmpByte = (byte) (0x80 >>> index);
		byteArray[arrayIndex] = (byte) (byteArray[arrayIndex] | tmpByte);
	}
	
	public static byte[] leftShiftBit(byte[] byteArray, int shiftNum) {
		byte[] result = new byte[byteArray.length];
		int totalBits = byteArray.length*8;
		for (int i = shiftNum; i < totalBits; i++) {
			if (BitHelper.getBit(byteArray, i)) {
				BitHelper.setBit(result, i-shiftNum);
			}
		}
		return result;
	}
	
	public static byte[] rightRotate(byte[] byteArray, int rotateNum) {
		byte[] result = new byte[byteArray.length];
		int totalBits = byteArray.length*8;
		rotateNum = rotateNum % totalBits;
		for (int i = 0; i < totalBits; i++) {
			int rotateIndex = totalBits-rotateNum+i;
			rotateIndex %= totalBits;
			if(BitHelper.getBit(byteArray, rotateIndex)){
				BitHelper.setBit(result, i);
			}
		}
		return result;
	}

	public static byte[] rigthShiftBit(byte[] byteArray, int shiftNum) {
		byte[] result = new byte[byteArray.length];
		int totalBits = byteArray.length*8;
		for (int i = 0; i < totalBits-shiftNum; i++) {
			if(BitHelper.getBit(byteArray, i)){
				BitHelper.setBit(result, i+shiftNum);
			}
		}
		return result;
	}
}
