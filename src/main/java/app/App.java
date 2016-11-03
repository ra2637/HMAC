package app;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;


/**
 *  Handle input args
 *  ./sha256 password keyFile
 *  ./hmac create keyFile messageFile outputFile
 *  ./hmac verify keyFile messageFile outputFile
 *
 */
public class App {
	public static enum Command {SHA256, HMAC}
	public static enum SubCommand {CREATE, VERIFY}
	
    public static void main( String[] args ) {
		try {
			// check command valid
			if (args.length < 1) {
				throw new InputMismatchException("Insufficient parameters.");
			}

			try {
				Command command = Command.valueOf(args[0].toUpperCase());
				List<String> parameters = Arrays.asList(args);
				parameters = parameters.subList(1, args.length);
				
				switch (command) {
				case SHA256:
					new SHA256(parameters);
					break;
				case HMAC:
					SubCommand subCommand = SubCommand.valueOf(parameters.get(0));
					parameters = parameters.subList(1, args.length);
					switch (subCommand) {
					case CREATE:
						new HAMC(parameters, true);
						break;
					case VERIFY:
						new HAMC(parameters, false);
						break;
					}
				}
			} catch (IllegalArgumentException e) {
				throw new InputMismatchException("Invalid command: " + args[0]);
			} 
			catch (Exception e) {
				e.printStackTrace();;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
    }
}
