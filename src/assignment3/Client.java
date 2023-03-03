package assignment3;

import java.io.*;

/**
 * Client Class to send read or write requests to the host
 * and continuously poll for a data from the host! 
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 *
 */
public class Client extends CommonRPCImpl{

	/**
	 * Constructor for a Client
	 */
	public Client(int port, String systemName) {
		super(systemName, port);
	}

	/**
	 * Method for sending requests to the Host
	 * Will construct the msg in bytes add it to the send packet
	 * @param request Integer, determines which request the client sends. 
	 * 0 = read, 1 = write, anything else is invalid
	 */
	private void sendRequest(int request) {
		String fileName = "test.txt";
		byte fileNameMsg[] = fileName.getBytes();
		String mode = "ocTEt";
		byte modeMsg[] = mode.getBytes();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Write the necessary bytes if it is a read request, write request
		// or invalid request
		// 0 = read, 1 = write, anything else is invalid
		try {
			if (request == 0) {
				// READ
				outputStream.write(new byte[] {(byte) 0, (byte) 1});
			} else if (request == 1) {
				// WRITE
				outputStream.write(new byte[] { (byte) 0, (byte) 2 });
			} else {
				// INVALID
				outputStream.write(new byte[] { (byte) -1, (byte) -1 });
			}

			outputStream.write(fileNameMsg);
			outputStream.write(new byte[] { (byte) 0 });
			outputStream.write(modeMsg);
			outputStream.write(new byte[] { (byte) 0 });
		} catch (IOException e) {
			e.printStackTrace();
			this.closeSockets();
		}

		byte data[] = outputStream.toByteArray();
		
		try {
			this.send(data, Helper.PORT_INTERM2);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSockets();
		}
		
	}

	/**
	 * Method for receving responses
	 */
	private void receiveResponse() {
		// Receive Continuous from the host until a valid
		// receive message is given
		try {
			this.receive(4, Helper.PORT_INTERM1);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSockets();
		}
		
	}

	/**
	 * Send and Receive responses from the host
	 * Will iterate 11 times, alternating through read and write requests
	 * at the 11th time, will send an invalid request which will kill it after
	 * timeout
	 */
	public void sendAndReceive() {
		int i = 0;
		while (i <= 11) {
			int request = i % 2;
			if (i == 11) {
				request = -1;
			}
			System.out.println("REQUEST: " + request + ", index: " + i);
			System.out.println("---------------------");
			this.sendRequest(request);
			this.receiveResponse();
			i++;
		}
		this.closeSockets();
	}

	/**
	 * Initialize and launch the client
	 * @param args String[], takes arguements if needed
	 */
	public static void main(String args[]) {
		Client c = new Client(Helper.PORT_CLIENT, "Client");
		c.sendAndReceive();
	}

}
