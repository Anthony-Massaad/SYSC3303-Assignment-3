package assignment3;

import java.net.DatagramPacket;

/**
 * Global logger class to print send and receive messages
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class Log {

	/**
	 * Print the formarted messages for sending. Print all the data in the packet
	 * @param systemName String, the name of the system
	 * @param packet DatagramPacket, the packet to collect information from
	 */
	public static void logSendMsg(String systemName, DatagramPacket packet) {
		// Process the send datagram.
		System.out.println(systemName + ": Preparing to send packet:");
		System.out.println("To host -> " + packet.getAddress());
		System.out.println("Destination port -> " + packet.getPort());
		int len = packet.getLength();
		System.out.println("Length of data -> " + len);
		System.out.println("Send data as a string -> " + new String(packet.getData(), 0, len));
		System.out.println("Send data as bytes -> " + Helper.printBytes(packet.getData()));
	}

	/**
	 * Print the formarted messages for receiving. Print all the data in the packet
	 * @param systemName String, the name of the system
	 * @param packet DatagramPacket, the packet to collect information from
	 */
	public static void logReceiveMsg(String systemName, DatagramPacket packet) {
		// Process the received datagram.
		System.out.println(systemName + ": Packet received:");
		System.out.println("From host -> " + packet.getAddress());
		System.out.println("From Host port -> " + packet.getPort());
		int len = packet.getLength();
		System.out.println("Length of data -> " + len);
		System.out.println("Receive data as a String -> " + new String(packet.getData(), 0, len));
		System.out.println("Receive data as Bytes -> " + Helper.printBytes(packet.getData()) + "\n");
	}

}
