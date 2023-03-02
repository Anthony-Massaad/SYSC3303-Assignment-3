package assignment3;

import java.io.*;
import java.net.*;

/**
 * Client Class to send read or write requests to the host! 
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 2
 *
 */
public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendAndReceiveSocket;
	
	/**
	 * Constructor for a Client
	 */
	public Client() {
		try {
			// Construct a datagram socket and bind it to any available
			this.sendAndReceiveSocket = new DatagramSocket();
			// Set timeout on the socket to ensure it dies if it doesn't receive anything
			// in n milliseconds
			this.sendAndReceiveSocket.setSoTimeout(Helper.TIMEOUT);
			System.out.println("Client sendAndReceiveSocket Port: " + this.sendAndReceiveSocket.getLocalPort() + "\n");
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}
	}

	/**
	 * Close all open sockets
	 */
	private void closeSockets() {
		this.sendAndReceiveSocket.close();
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
			System.exit(1);
		}

		byte data[] = outputStream.toByteArray();

		// Add the data produced in a send packet pointed to port 23
		try {
			this.sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}

		Log.logSendMsg("Client", this.sendPacket);

		// Slow things down
		try {
			Thread.sleep(Helper.SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}

		// Send the datagram packet to the host via the sendAndReceiveSocket.
		try {
			this.sendAndReceiveSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}
		System.out.println("Packet sent.\n");
	}

	/**
	 * Method for receving responses
	 */
	private void receiveResponse() {
		// Data received should be 4 bytes from the server //
		byte data[] = new byte[4];
		this.receivePacket = new DatagramPacket(data, data.length);
		
		
		// Receive Continuous 
		
		
		
		
		
		//

		// Block until a datagram packet is received from receiveSocket.
//		try {
//			System.out.println("Client: Waiting for response..."); // so we know we're waiting
//			this.sendAndReceiveSocket.receive(this.receivePacket);
//		} catch (IOException e) {
//			System.out.print("IO Exception: likely:");
//			System.out.println("Receive Socket Timed Out.\n" + e);
//			e.printStackTrace();
//			this.closeSockets();
//			System.exit(1);
//		}

		Log.logReceiveMsg("Client", this.receivePacket);
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
		Client c = new Client();
		c.sendAndReceive();
	}

}
