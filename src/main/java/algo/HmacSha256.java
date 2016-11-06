package algo;

import utils.ByteHelper;

/**
 * Using self implemented lib to execute HmacSHA256
 * @author Yuntai
 */
public class HmacSha256 {
	byte[] key, message;

	public HmacSha256(byte[] key, byte[] message) {
		this.key = key;
		this.message = message;
		
	}
	
	public byte[] HmacSHA256() throws Exception  {
		// check key length
		if (key.length*8 > 512) {
			Sha256 sha = new Sha256(key);
			key = sha.digest();
		} else {
			byte[] tmp = new byte[64];
			for (int i = 0; i < key.length; i++) {
				tmp[i] = key[i];
			}
			key = tmp;
		}
		
		byte[] oKey_pad = ByteHelper.xORByteArray(getPadding(key.length, (byte)0x5C), key);
		byte[] iKey_pad = ByteHelper.xORByteArray(getPadding(key.length, (byte)0x36), key);
		byte[] iKey_pad_message = ByteHelper.concatByte(iKey_pad, message);
		Sha256 sha1 = new Sha256(iKey_pad_message);
		byte[] oKey_pad_message = ByteHelper.concatByte(oKey_pad, sha1.digest()); 
		Sha256 sha2 = new Sha256(oKey_pad_message);
		return sha2.digest();
		
		
	}
	
	private byte[] getPadding(int byteSize, byte padding) {
		byte[] result = new byte[byteSize];
		for (int i = 0; i < result.length; i++) {
			result[i] = padding;
		}
		return result;
	}

}
