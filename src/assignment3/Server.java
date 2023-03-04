package assignment3;

import java.io.*;

/**
 * Server class continously request for data and responding accordingly
 * Depending on the message passed from the client
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class Server extends CommonRPCImpl{
	
	/**
	 * Constructor for the server
	 */
	public Server(int port, String systemName) {
		super(systemName, port);
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
			throw new Exception("second byte is not 1 or 2 meaning invalid");
		}
		
		// Determine the end position of the first string 
		position = 3;
		position = findString(data, position);

		// Determines if the byte after the end of string is 0, otherwise throws error
		if (data[position] != (byte) 0) {
			throw new Exception("byte after first String is not 0");
		}

		// Determine the end position of the second string 
		position++;
		position = findString(data, position);
		
		// Determines if the byte after the end of string is 0, otherwise throws error
		if (data[position] != (byte) 0) {
			throw new Exception("byte after second String is not 0");
		}

		return isReadRequest;
	}
	
	/**
	 * receive data from the Intermediate Host
	 */
	private void receiveData() {
		// Receive Continuous 
		try {
			this.receive(Helper.SIZE, Helper.PORT_INTERM2);
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			this.closeSockets();
		}
	}

	/**
	 * Method for receiving and sending back
	 */
	private void parseAndSendData() {
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
		
		// send message and receive acknowledgement
		try {
			this.send(sendData, Helper.PORT_INTERM1);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
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
			this.receiveData();
			this.parseAndSendData();
		}
	}

	/**
	 * Initialize and launch the server
	 * @param args String[], adds arguments if needed 
	 */
	public static void main(String args[]) {
		Server c = new Server(Helper.PORT_SERVER, "Server");
		c.receiveAndSend();
	}

}
