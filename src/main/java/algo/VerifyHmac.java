package algo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Using java lib to execute HmacSHA256
 * @author Yuntai
 */
public class VerifyHmac {
	byte[] key, message;

	public VerifyHmac(byte[] key, byte[] message) {
		this.key = key;
		this.message = message;
	}

	public byte[] HmacSHA256() throws Exception  {
	     String algorithm="HmacSHA256";
	     Mac mac = Mac.getInstance(algorithm);
	     mac.init(new SecretKeySpec(key, algorithm));
	     return mac.doFinal(message);
	}
}
