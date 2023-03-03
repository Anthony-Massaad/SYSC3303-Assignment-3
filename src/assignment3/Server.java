package assignment3;

import java.io.*;
import java.net.*;

/**
 * Server class continously request for data and responding accordingly
 * Depending on the message passed from the client
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class Server {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket; 
	
	/**
	 * Constructor for the server
	 */
	public Server() {
		try {
			// initialize socket and port
			this.socket = new DatagramSocket(Helper.PORT_SERVER);
			this.socket.setSoTimeout(Helper.TIMEOUT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server Started");
	}

	/**
	 * Close all open sockets
	 */
	private void closeSockets() {
		this.socket.close();
		System.exit(1);
	}

	/**
	 * Determines where the end of the string is given data in bytes 
	 * @param data byte[], the data received in bytes
	 * @param position Integer, the start position to iterate in the array of bytes
	 * @return position Integer, the end position where the string ends 
	 */
	private int findString(byte[] data, int position) {
		for (int i = position; i < data.length; i++) {
			if (data[i] == (byte) 0) {
				break;
			}
			position++;
		}
		return position;
	}

	/**
	 * Parse the data to ensure it is valid. It is valid if:
	 * <ul>
	 * <li>first two byes are 0 and 1, or 0 and 2</li>
	 * <li>Some text</li>
	 * <li>Byte after is 0</li>
	 * <li>Some text </li>
	 * <li>Byte after is 0</li>
	 * </ul>
	 * @param data byte[], the data received to the server
	 * @return True if it is a read request otherwise false
	 * @throws Exception and quits the program if the message received is invalid
	 */
	private boolean parseData(byte[] data) throws Exception {
		boolean isReadRequest;
		int position;
		
		// ensure that the first byte is 0, otherwise throws an error
		if (data[0] != (byte) 0) {
			this.closeSockets();
			throw new Exception("first byte not 0");
		}
		
		// Check if the second byte is 1 or 2 to determine if it is 
		// a Read request or Write Request respectively 
		// Otherwise throws an error
		if (data[1] == (byte) 1) {
			isReadRequest = true;
		} else if (data[1] == (byte) 2) {
			isReadRequest = false;
		} else {
			this.closeSockets();
			throw new Exception("second byte is not 1 or 2 meaning invalid");
		}
		
		// Determine the end position of the first string 
		position = 3;
		position = findString(data, position);

		// Determines if the byte after the end of string is 0, otherwise throws error
		if (data[position] != (byte) 0) {
			this.closeSockets();
			throw new Exception("byte after first String is not 0");
		}

		// Determine the end position of the second string 
		position++;
		position = findString(data, position);
		
		// Determines if the byte after the end of string is 0, otherwise throws error
		if (data[position] != (byte) 0) {
			this.closeSockets();
			throw new Exception("byte after second String is not 0");
		}

		return isReadRequest;
	}

	/**
	 * Method for receiving and sending back
	 * @throws InterruptedException 
	 */
	private void receiveAndEcho() {
		// Receive Continuous 
		while (true) {
			byte data[] = new byte[Helper.SIZE];
			this.receivePacket = new DatagramPacket(data, data.length);
			byte[] requestBytes = Helper.REQUESTING.getBytes();
			
			try {
				DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, InetAddress.getLocalHost(), Helper.PORT_INTERM2);
				// System.out.println("Sending " + new String(requestBytes, 0, requestBytes.length) + " to port " + Helper.PORT_INTERM2);
				this.socket.send(requestPacket);
				this.socket.receive(this.receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.closeSockets();
			}
			
			if (!new String(this.receivePacket.getData(), 0, this.receivePacket.getLength()).equals(Helper.NOTHING)) {
				break;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.closeSockets();
			}
		}

		Log.logReceiveMsg("Server", this.receivePacket);

		// PARSE DATA
		boolean isReadRequest = false;
		try {
			isReadRequest = this.parseData(this.receivePacket.getData());
		} catch (Exception e2) {
			e2.printStackTrace();
			this.closeSockets();
		}
		
		// Send data back according if it is read or write request.
		// Read = 0 3 0 1. Write = 0 4 0 0 
		byte[] sendData;
		if (isReadRequest) {
			sendData = new byte[] {(byte) 0, (byte) 3, (byte) 0, (byte) 1};
		} else {
			sendData = new byte[] {(byte) 0, (byte) 4, (byte) 0, (byte) 0};
		}

		// Create send packet 
		this.sendPacket = new DatagramPacket(sendData, sendData.length, this.receivePacket.getAddress(), Helper.PORT_INTERM1);

		Log.logSendMsg("Server", this.sendPacket);

		// Slow things down
		try {
			Thread.sleep(Helper.SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.closeSockets();
			
		}

		// Send the packet to the designated port via the send socket
		try {
			// sending packet of message
			this.socket.send(this.sendPacket);
			System.out.println("Packet Sent");
			// receiving acknowledgement
			System.out.print("\nReceiving Acknowledgement of message sent\n");
			byte data[] = new byte[Helper.SIZE];
			this.receivePacket = new DatagramPacket(data, data.length);
			this.socket.receive(this.receivePacket);
			Log.logReceiveMsg("Server", this.receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			this.closeSockets();
			
		}

	}

	/**
	 * Method that is called to start the receive and send
	 */
	public void receiveAndSend() {
		while (true) {
			System.out.println("------------------------");
			this.receiveAndEcho();
		}
	}

	/**
	 * Initialize and launch the server
	 * @param args String[], adds arguments if needed 
	 */
	public static void main(String args[]) {
		Server c = new Server();
		c.receiveAndSend();
	}

}
