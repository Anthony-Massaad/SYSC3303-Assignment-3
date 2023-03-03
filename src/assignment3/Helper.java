package assignment3;

/**
 * Helper class to store global methods and constants
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 2
 */
public class Helper {
	public final static int SLEEP = 3000;
	public final static int SIZE = 31;
	public final static int TIMEOUT = 15000;
	public final static String REQUESTING = "req";
	public final static byte[] ACKNOWLEDGE = "ack".getBytes();
	public final static String NOTHING = "na";
	
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
