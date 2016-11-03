package algo;

import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;

import utils.BitHelper;
import utils.ByteHelper;
import utils.HexBinary;

/**
 * @author yuntai
 */

public class Sha256 {
	private static final byte[][] initHash = { 
		{ (byte) 0x6a, (byte) 0x09, (byte) 0xe6, (byte) 0x67 },
		{ (byte) 0xbb, (byte) 0x67, (byte) 0xae, (byte) 0x85 },
		{ (byte) 0x3c, (byte) 0x6e, (byte) 0xf3, (byte) 0x72 },
		{ (byte) 0xa5, (byte) 0x4f, (byte) 0xf5, (byte) 0x3a },
		{ (byte) 0x51, (byte) 0x0e, (byte) 0x52, (byte) 0x7f },
		{ (byte) 0x9b, (byte) 0x05, (byte) 0x68, (byte) 0x8c },
		{ (byte) 0x1f, (byte) 0x83, (byte) 0xd9, (byte) 0xab },
		{ (byte) 0x5b, (byte) 0xe0, (byte) 0xcd, (byte) 0x19 } 
	};

	private static final byte[][] constantK = {
		HexBinary.decode("428a2f98"), HexBinary.decode("71374491"), HexBinary.decode("b5c0fbcf"), HexBinary.decode("e9b5dba5"),
		HexBinary.decode("3956c25b"), HexBinary.decode("59f111f1"), HexBinary.decode("923f82a4"), HexBinary.decode("ab1c5ed5"),
		HexBinary.decode("d807aa98"), HexBinary.decode("12835b01"), HexBinary.decode("243185be"), HexBinary.decode("550c7dc3"),
		HexBinary.decode("72be5d74"), HexBinary.decode("80deb1fe"), HexBinary.decode("9bdc06a7"), HexBinary.decode("c19bf174"),
		HexBinary.decode("e49b69c1"), HexBinary.decode("efbe4786"), HexBinary.decode("0fc19dc6"), HexBinary.decode("240ca1cc"),
		HexBinary.decode("2de92c6f"), HexBinary.decode("4a7484aa"), HexBinary.decode("5cb0a9dc"), HexBinary.decode("76f988da"),
		HexBinary.decode("983e5152"), HexBinary.decode("a831c66d"), HexBinary.decode("b00327c8"), HexBinary.decode("bf597fc7"),
		HexBinary.decode("c6e00bf3"), HexBinary.decode("d5a79147"), HexBinary.decode("06ca6351"), HexBinary.decode("14292967"),
		HexBinary.decode("27b70a85"), HexBinary.decode("2e1b2138"), HexBinary.decode("4d2c6dfc"), HexBinary.decode("53380d13"),
		HexBinary.decode("650a7354"), HexBinary.decode("766a0abb"), HexBinary.decode("81c2c92e"), HexBinary.decode("92722c85"),
		HexBinary.decode("a2bfe8a1"), HexBinary.decode("a81a664b"), HexBinary.decode("c24b8b70"), HexBinary.decode("c76c51a3"),
		HexBinary.decode("d192e819"), HexBinary.decode("d6990624"), HexBinary.decode("f40e3585"), HexBinary.decode("106aa070"),
		HexBinary.decode("19a4c116"), HexBinary.decode("1e376c08"), HexBinary.decode("2748774c"), HexBinary.decode("34b0bcb5"),
		HexBinary.decode("391c0cb3"), HexBinary.decode("4ed8aa4a"), HexBinary.decode("5b9cca4f"), HexBinary.decode("682e6ff3"),
		HexBinary.decode("748f82ee"), HexBinary.decode("78a5636f"), HexBinary.decode("84c87814"), HexBinary.decode("8cc70208"),
		HexBinary.decode("90befffa"), HexBinary.decode("a4506ceb"), HexBinary.decode("bef9a3f7"), HexBinary.decode("c67178f2")
	};

	private byte[] password;

	public Sha256(String password) {
		this.password = password.getBytes(StandardCharsets.UTF_8);
		// TODO: check if msg bits < 2^64 
	}

	public String digest() {
		byte[] paddingPassword = padding();
		byte[][] hash = Sha256.initHash;
		for (int i = 0; i < paddingPassword.length; i+=64) {
			byte[] block = ByteHelper.subByte(paddingPassword, i, 64);
			byte[][] newHash = hashBlock(getBlockWords(block), hash);
			
			for (int j = 0; j < newHash.length; j++) {
				newHash[j] = ByteHelper.mod2Power32(ByteHelper.byteToInt(newHash[j])+ByteHelper.byteToInt(hash[j]));
			}
			hash = newHash;
		}
		
		String result = "";
		for (int i = 0; i < hash.length; i++) {
			result += HexBinary.encode(hash[i]); 
			
		}
		return result;
	}

	private byte[][] hashBlock(byte[][] blockWords, byte[][] initHash){
		// run 64 rounds
		byte[][] hash = initHash.clone();
		for (int i = 0; i < 64; i++) {
				//  T1 = h + Σ1(e) + Ch(e, f, g) + Ki + Wi
				//	T2 = Σ0(a) + Maj(a, b, c)
				//	h = g
				//	g = f
				//	f = e
				//	e = d + T1
				//	d = c
				//	c = b
				//	b = a
				//	a = T1 + T2
			byte[] T1 = caculateT1(hash, constantK[i], blockWords[i]);
			byte[] T2 = caculateT2(hash);
			hash = new byte[][]{
					ByteHelper.mod2Power32(ByteHelper.byteToInt(T1)+ByteHelper.byteToInt(T2)), //a
					hash[0],	//b
					hash[1], 	//c
					hash[2],	//d
					ByteHelper.mod2Power32(ByteHelper.byteToInt(hash[3])+ByteHelper.byteToInt(T1)),	//e
					hash[4],
					hash[5],
					hash[6],
			};
		}
		
		return hash;
	}
	
	/**
	 * Add padding for password
	 * @return padding password that can be divided by 512 bits
	 */
	private byte[] padding() {
		// calculate padding bits count
		int l = password.length*8;
		int k = paddingK(l);
		int resultBits = l+1+k+64;
		
		byte[] result = new byte[resultBits/8];
		// copy password value to result
		for (int i = 0; i < l; i++) {
			if(BitHelper.getBit(password, i)) {
				BitHelper.setBit(result, i);
			} 
		}
		
		// append 1 after initial password
		BitHelper.setBit(result, l);
		
		// set the last 64 bits (8 bytes) for password length in bit
		byte[] lengthPadding = ByteHelper.longToByteArray(l);
		for (int i = 0; i < 8; i++) {
			result[i+result.length-8] = lengthPadding[i];
		}
		return result;
	}
	
	/**
	 * Get the k for padding
	 * @return how many K bits should be add 
	 */
	private int paddingK(int l) {
		int k=-1;
		for (int i = 0;; i++) {
			k = 512*i+448-l-1;
			if (k >= 0) {
				return k;
			}
		}
	}
	
	/**
	 * Get 64 words of a block
	 * @param block: 512 bits (64 bytes)
	 * @return words array[64][4] of the block: 64 words, each word is 32bit (4 byte)
	 */
	private byte[][] getBlockWords(byte[] block){
		if(block.length != 64) {
			throw new InputMismatchException("expect block size: 64, get "+block.length);
		}
		
		byte[][] result = new byte[64][4];
		for (int i = 0; i < result.length; i++) {
			if (i < 16) {
				for (int j = 0; j < 4; j++) {
					result[i][j] = block[i*4+j]; 
				}
			} else {
				result[i] = caclulateWord(result[i-2], result[i-7], result[i-15], result[i-16]);
			}
		}
		return result;
	}
	
	/**
	 * Calculate block words after 16
	 * @param words for calculate word
	 * @return word
	 */
	private byte[] caclulateWord(byte[] w_2, byte[] w_7, byte[] w_15, byte[] w_16){
		// Wi = σ1(Wi−2) + Wi−7 + σ0(Wi−15) + Wi−16
		// σ0(X) = RotR(X, 7) ⊕ RotR(X, 18) ⊕ ShR(X, 3),
		// σ1(X) = RotR(X, 17) ⊕ RotR(X, 19) ⊕ ShR(X, 10),
		byte[] rotRX1 = BitHelper.rightRotate(w_2, 17);
		byte[] rotRX2 = BitHelper.rightRotate(w_2, 19);
		byte[] shR = BitHelper.rigthShiftBit(w_2, 10);
		byte[] sigma1 = ByteHelper.xORByteArray(rotRX1, rotRX2);
		sigma1 = ByteHelper.xORByteArray(sigma1, shR);
		
		rotRX1 = BitHelper.rightRotate(w_15, 7);
		rotRX2 = BitHelper.rightRotate(w_15, 18);
		shR = BitHelper.rigthShiftBit(w_15, 3);
		byte[] sigma0 = ByteHelper.xORByteArray(rotRX1, rotRX2);
		sigma0 = ByteHelper.xORByteArray(sigma0, shR);
		
		byte result[] = ByteHelper.mod2Power32(ByteHelper.byteToInt(sigma1)+ByteHelper.byteToInt(w_7));
		result = ByteHelper.mod2Power32(ByteHelper.byteToInt(result)+ByteHelper.byteToInt(sigma0));
		result = ByteHelper.mod2Power32(ByteHelper.byteToInt(result)+ByteHelper.byteToInt(w_16));
		return result;
	}
	
	
	private byte[] caculateT1(byte[][] initHash, byte[] K, byte[] W){
		//  T1 = h + Σ1(e) + Ch(e, f, g) + Ki + Wi
		byte[] e = initHash[4];
		byte[] f = initHash[5];
		byte[] g = initHash[6];
		byte[] h = initHash[7];
		
		//	Σ1(X) = RotR(X, 6) ⊕ RotR(X, 11) ⊕ RotR(X, 25),
		byte[] rotR1 = BitHelper.rightRotate(e, 6);
		byte[] rotR2 = BitHelper.rightRotate(e, 11);
		byte[] rotR3 = BitHelper.rightRotate(e, 25);
		byte[] sigma1 =  ByteHelper.xORByteArray(rotR1, rotR2);
		sigma1 = ByteHelper.xORByteArray(sigma1, rotR3);
		
		//  Ch(X, Y, Z) = (X ∧ Y ) ⊕ (-X ∧ Z),
		byte[] ef = ByteHelper.andByteArray(e, f);
		byte[] eg = ByteHelper.andByteArray(ByteHelper.complementByteArray(e), g);
		byte[] efg = ByteHelper.xORByteArray(ef, eg);
		
		byte[] result = ByteHelper.mod2Power32(ByteHelper.byteToInt(h)+ByteHelper.byteToInt(sigma1));
		result = ByteHelper.mod2Power32(ByteHelper.byteToInt(result)+ByteHelper.byteToInt(efg));
		result = ByteHelper.mod2Power32(ByteHelper.byteToInt(result)+ByteHelper.byteToInt(K));
		result = ByteHelper.mod2Power32(ByteHelper.byteToInt(result)+ByteHelper.byteToInt(W));
		return result;
	}
	
	private byte[] caculateT2(byte[][] initHash){
		//	T2 = Σ0(a) + Maj(a, b, c)
		byte[] a = initHash[0];
		byte[] b = initHash[1];
		byte[] c = initHash[2];
		
		//	Σ0(X) = RotR(X, 2) ⊕ RotR(X, 13) ⊕ RotR(X, 22),		
		byte[] rotR1 = BitHelper.rightRotate(a, 2);
		byte[] rotR2 = BitHelper.rightRotate(a, 13);
		byte[] rotR3 = BitHelper.rightRotate(a, 22);
		byte[] sigma0 =  ByteHelper.xORByteArray(rotR1, rotR2);
		sigma0 = ByteHelper.xORByteArray(sigma0, rotR3);
		
		//  Maj(X, Y, Z) = (X ∧ Y ) ⊕ (X ∧ Z) ⊕ (Y ∧ Z),
		byte[] ab = ByteHelper.andByteArray(a, b);
		byte[] ac = ByteHelper.andByteArray(a, c);
		byte[] bc = ByteHelper.andByteArray(b, c);
		byte[] abc = ByteHelper.xORByteArray(ab, ac);
		abc = ByteHelper.xORByteArray(abc, bc);
		
		return ByteHelper.mod2Power32(ByteHelper.byteToInt(sigma0)+ByteHelper.byteToInt(abc));
	}
}
