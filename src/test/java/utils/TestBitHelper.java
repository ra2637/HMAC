package utils;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TestBitHelper {
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
	public void TestRightRotate(){
		// original 1,0,0,0,1,0,1,0
		// right rotate 3 0,1,0,1,0,0,0,1
		int[] originalInt = {1,0,0,0,1,0,1,0};
		byte[] original = TestBitHelper.setBitArray(originalInt);
		int[] expectedInt = {0,1,0,1,0,0,0,1};
		byte[] expected = TestBitHelper.setBitArray(expectedInt);
		
		assertArrayEquals(expected, BitHelper.rightRotate(original, 3));
		
	}
}
