package assignment3;

/**
 * Helper class to store global methods and constants
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class Helper {
	// General constants used throughout the program
	public final static int SLEEP = 1000;
	public final static int SIZE = 31;
	public final static int TIMEOUT = 30000;
	
	// messages that are passed
	public final static String REQUESTING = "req"; // request
	public final static byte[] ACKNOWLEDGE = "ack".getBytes(); // acknowledge in bytes
	public final static String NOTHING = "na"; // nothing to send
	
	// PORTS
	public final static int PORT_INTERM1 = 5000; // server -> client
	public final static int PORT_INTERM2 = 3500; // client -> server
	public final static int PORT_SERVER = 2500;
	public final static int PORT_CLIENT = 1500;

	/**
	 * Print the data of bytes as hex format
	 * @param bytes byes[], the bytes array
	 * @return String, the formated string containing the bytes
	 */
	public static String printBytes(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte aByte : bytes) {
			result.append(String.format("%x", aByte));
		}
		return result.toString();
	}
}
