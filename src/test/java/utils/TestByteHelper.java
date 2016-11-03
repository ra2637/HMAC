package utils;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TestByteHelper {
	public static byte[] setBitArray(int[] intArr){
		int byteLength = intArr.length/8;
		byte[] result = new byte[byteLength];
		for (int i = 0; i < intArr.length; i++) {
			if (intArr[i] == 1) {
				BitHelper.setBit(result, i);
			}
		}
		return result;
	};
	
	@Test
	public void testIntToByteArray() {
		int[] expectedInt = {
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,
				0,0,0,1,1,0,0,0
		};
		byte[] expected = setBitArray(expectedInt);
		byte[] result = ByteHelper.longToByteArray(24);
		assertArrayEquals(expected, result);
	}
}
