package algo;

import java.util.InputMismatchException;

import utils.ByteHelper;
import utils.HexBinary;

/**
 * Using self implemented lib to execute HmacSHA256
 * @author Yuntai
 */
public class HmacSha256 {
	String key, message;

	public HmacSha256(String key, String message) {
		this.key = key;
		this.message = message;
		
	}
	
	public String HmacSHA256() throws Exception  {
		// check key length
		byte[] keyBytes = HexBinary.decode(key);
		if (keyBytes.length*8 > 512) {
			Sha256 sha = new Sha256(key);
			keyBytes = HexBinary.decode(sha.digest());
		} else {
			byte[] tmp = new byte[64];
			for (int i = 0; i < keyBytes.length; i++) {
				tmp[i] = keyBytes[i];
			}
			keyBytes = tmp;
		}
		
		byte[] oKey_pad = ByteHelper.xORByteArray(keyBytes, getPadding(keyBytes.length, (byte)0x5C));
		byte[] iKey_pad = ByteHelper.xORByteArray(keyBytes, getPadding(keyBytes.length, (byte)0x36));
		String iKey_pad_message = HexBinary.encode(iKey_pad)+message;
		Sha256 sha1 = new Sha256(iKey_pad_message);
		String oKey_pad_message = HexBinary.encode(oKey_pad)+sha1.digest(); 
		Sha256 sha2 = new Sha256(oKey_pad_message);
		return sha2.digest();
		
//		byte[] oKey_pad = ByteHelper.xORByteArray(getPadding(keyBytes.length, (byte)0x5C), keyBytes);
//		byte[] iKey_pad = ByteHelper.xORByteArray(getPadding(keyBytes.length, (byte)0x36), keyBytes);
//		byte[] iKey_pad_message = ByteHelper.concatByte(iKey_pad, message.getBytes());
//		Sha256 sha1 = new Sha256(HexBinary.encode(iKey_pad_message));
//		byte[] oKey_pad_message = ByteHelper.concatByte(oKey_pad, sha1.digest().getBytes()); 
//		Sha256 sha2 = new Sha256(HexBinary.encode(oKey_pad_message));
//		return sha2.digest();
	}
	
	private byte[] getPadding(int byteSize, byte padding) {
		byte[] result = new byte[byteSize];
		for (int i = 0; i < result.length; i++) {
			result[i] = padding;
		}
		return result;
	}

}
