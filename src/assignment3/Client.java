package assignment3;

import java.io.*;
import java.net.*;

/**
 * Client Class to send read or write requests to the host
 * and continuously poll for a data from the host! 
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 *
 */
public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	
	/**
	 * Constructor for a Client
	 */
	public Client() {
		try {
			// Construct a datagram socket and bind it to any available
			this.socket = new DatagramSocket(Helper.PORT_CLIENT);
			// Set timeout on the socket to ensure it dies if it doesn't receive anything
			// in n milliseconds
			this.socket.setSoTimeout(Helper.TIMEOUT);
			System.out.println("Client sendAndReceiveSocket Port: " + this.socket.getLocalPort() + "\n");
		} catch (SocketException se) { // Can't create the socket.
			se.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}
	}

	/**
	 * Close all open sockets and terminate program
	 */
	private void closeSockets() {
		this.socket.close();
		System.exit(1);
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

		// Add the data produced in a send packet pointed to port 23
		try {
			this.sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), Helper.PORT_INTERM2);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			this.closeSockets();
		}

		Log.logSendMsg("Client", this.sendPacket);

		// Slow things down
		try {
			Thread.sleep(Helper.SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.closeSockets();
		}

		// Send the datagram packet to the host via the sendAndReceiveSocket.
		try {
			this.socket.send(sendPacket);
			System.out.println("Packet sent.");
			// receiving acknowledgement
			System.out.print("\nReceiving Acknowledgement of message sent\n");
			byte ackdata[] = new byte[Helper.SIZE];
			this.receivePacket = new DatagramPacket(ackdata, ackdata.length);
			this.socket.receive(this.receivePacket);
			Log.logReceiveMsg("Client", this.receivePacket);
			
		} catch (IOException e) {
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
		while (true) {
			byte data[] = new byte[4];
			this.receivePacket = new DatagramPacket(data, data.length);
			byte[] requestBytes = Helper.REQUESTING.getBytes();
			
			try {
				// request for data
				DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, InetAddress.getLocalHost(), Helper.PORT_INTERM1);
				this.socket.send(requestPacket);
				this.socket.receive(this.receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.closeSockets();
			}
			
			// exit loop when data received is not nothing
			if (!new String(this.receivePacket.getData(), 0, this.receivePacket.getLength()).equals(Helper.NOTHING)) {
				break;
			}
			
			// slow down the program
			try {
				Thread.sleep(Helper.SLEEP);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.closeSockets();
			}
			
		}
		
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
