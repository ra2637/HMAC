package algo;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import algo.Sha256;
import utils.BitHelper;
import utils.HexBinary;

public class TestSha256 {

	@Test
	public void TestPadding() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		byte[] expected = new byte[512 / 8];
		int[] abcBit = { 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1 };
		for (int i = 0; i < abcBit.length; i++) {
			if (abcBit[i] == 1) {
				BitHelper.setBit(expected, i);
			}
		}
		BitHelper.setBit(expected, 512 - 5);
		BitHelper.setBit(expected, 512 - 4);
		Sha256 sha256 = new Sha256("abc");
		Method method = Sha256.class.getDeclaredMethod("padding", null);
		method.setAccessible(true);
		byte[] result = (byte[]) method.invoke(sha256, null);
		assertArrayEquals(expected, result);
	}

	@Test
	public void TestDigest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String expect = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad".toUpperCase();
		Sha256 sha256 = new Sha256("abc");
		assertEquals(expect, sha256.digest());
		
		sha256= new Sha256("afd;lkbamn.,4q35");
		expect = "d8a4cdb8643335d3f0221c79b6b0bb8197e90a2146b4149f67a7e0a5f6059a0c".toUpperCase();
		assertEquals(expect, sha256.digest());
	}

}
