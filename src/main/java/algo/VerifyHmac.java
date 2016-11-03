package algo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import utils.HexBinary;

/**
 * Using java lib to execute HmacSHA256
 * @author Yuntai
 */
public class VerifyHmac {
	String key, message;

	public VerifyHmac(String key, String message) {
		this.key = key;
		this.message = message;
	}

	public byte[] HmacSHA256() throws Exception  {
	     String algorithm="HmacSHA256";
	     Mac mac = Mac.getInstance(algorithm);
	     mac.init(new SecretKeySpec(HexBinary.decode(key), algorithm));
	     return mac.doFinal(message.getBytes("UTF8"));
	}
}
