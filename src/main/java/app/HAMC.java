package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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

		// check if key file is existed
		File keyFile = new File(keyFileStr);
		if (!keyFile.exists()) {
			throw new InputMismatchException("keyFile is not existed: " + keyFile);
		}

		// check if message file is existed
		File messageFile = new File(messageFileStr);
		if (!messageFile.exists()) {
			throw new InputMismatchException("messageFile is not existed: " + messageFile);
		}
		
		// get key and message
		BufferedReader bufferReader = new BufferedReader(new FileReader(keyFile));
		String key = bufferReader.readLine();
		bufferReader.close();
		
		bufferReader = new BufferedReader(new FileReader(messageFile));
		String messages = bufferReader.readLine();
		bufferReader.close();

		// run HMAC sha-256
		String finalResult;
		if (isCreate) {
			HmacSha256 hmacSha256 = new HmacSha256(key, messages);
			finalResult = hmacSha256.HmacSHA256();
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
