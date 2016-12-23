package app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.List;

import algo.Sha256;
import utils.HexBinary;

public class SHA256 {
	/**
	 * Using SHA256 to generate hash key from password.
	 * 
	 * @param parameters
	 *            password keyfile
	 * @throws Exception
	 */
	public SHA256(List<String> parameters) {
		// check args are valid
		if (parameters.size() != 2) {
			throw new InputMismatchException("Insufficient parameters: password keyFile");
		}

		String passwordStr = parameters.get(0);
		String keyFileStr = parameters.get(1);

		try {
			// hash password
			Sha256 sha256 = new Sha256(passwordStr.getBytes(StandardCharsets.UTF_8));
			byte[] result = sha256.digest();

			// write to outputfile
			File keyFile = new File(keyFileStr);
			if (keyFile.exists()) {
				System.out.println("Overwrite existed output file: " + keyFileStr);
			}
			keyFile.createNewFile();
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(keyFile));
	        writer.write(result);
	        writer.close();
	        System.out.println(HexBinary.encode(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
