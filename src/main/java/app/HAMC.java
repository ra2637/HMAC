package app;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;

import algo.HmacSha256;
import algo.VerifyHmac;
import utils.HexBinary;

public class HAMC {
	/**
	 * General process for HMAC create and verify
	 * 
	 * @param parameters
	 *            keyFile messageFile outputFile
	 * @param isCreate
	 * @throws Exception
	 */

	public HAMC(List<String> parameters, boolean isCreate) throws Exception {
		// set up parameters
		if (parameters.size() != 3) {
			throw new InputMismatchException("Insufficient parameters: inputFile keyFile outputFile mode");
		}
		String keyFileStr = parameters.get(0);
		String messageFileStr = parameters.get(1);
		String outputFileStr = parameters.get(2);

		
		// get key and message
		byte[] key = Files.readAllBytes(Paths.get(keyFileStr));
		byte[] messages = Files.readAllBytes(Paths.get(messageFileStr));

		// run HMAC sha-256
		String finalResult;
		if (isCreate) {
			HmacSha256 hmacSha256 = new HmacSha256(key, messages);
			finalResult = HexBinary.encode(hmacSha256.HmacSHA256());
		} else {
			VerifyHmac verify = new VerifyHmac(key, messages);
			finalResult = HexBinary.encode(verify.HmacSHA256());
		}
		
		// write to output file
		File outputFile = new File(outputFileStr);
		if (outputFile.exists()) {
			System.out.println("Overwrite existed output file: " + outputFileStr);
		}
		outputFile.createNewFile();
		FileWriter fileWriter = new FileWriter(outputFile);
		fileWriter.write(finalResult);
		fileWriter.close();
	}

}
