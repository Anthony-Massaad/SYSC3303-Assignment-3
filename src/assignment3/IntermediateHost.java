package assignment3;

import java.io.*;
import java.net.*;

/**
 * Intermediate Host class for receiving and sending to Client
 * As well as sending and receiving for Server
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 2
 */
public class IntermediateHost {
	private DatagramPacket receivedClientPacket, receivedServerPacket; 
	private DatagramSocket receiveSocketClient, receiveSocketServer;

	/**
	 * Constuctor for Intermediate Host
	 * Initializes the receive socket to point at port 23 where the Client will send
	 * Initializes a send and receive socket for itself
	 */
	public IntermediateHost() {
		try {
			this.receiveSocketClient = new DatagramSocket(23);
			this.receiveSocketServer = new DatagramSocket(50);
			// Set timeout on the sockets to ensure it dies if it doesn't receive anything
			// in n milliseconds
//			this.receiveClientSocket.setSoTimeout(Helper.TIMEOUT);
//			this.sendAndReceiveSocket.setSoTimeout(Helper.TIMEOUT);
//			this.receiveServerSocket.setSoTimeout(Helper.TIMEOUT);
//			System.out.println("Intermediate Host sendAndReceiveSocket Port: " + this.sendAndReceiveSocket.getLocalPort() + "\n");

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Closes all the open sockets
	 */
	private void closeSockets() {
//		this.receiveServerSocket.close();
//		this.receiveClientSocket.close();
	}
	
	public synchronized static DatagramPacket rpc_send() {
		
	}

	/**
	 * Method for receiving the client's requests
	 */
	private synchronized void clientRequest() {
		byte data[] = new byte[Helper.SIZE];
		this.receivedClientPacket = new DatagramPacket(data, data.length);
		// RECEIVE FROM PORT 23, and will wait until a packet is sent
		try {
			System.out.println("Waiting for A Packet");
			this.receiveSocketClient.receive(this.receivedClientPacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("[Intermediate Host] Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}
		// Save the client's port to use for later
//		this.savedClientPort = this.receivedClientPakcet.getPort();

		Log.logReceiveMsg("Intermediate Host", this.receivedClientPacket);
	}
	
	private synchronized void serverResponse() {
		byte data[] = new byte[Helper.SIZE];
		this.receivedServerPacket = new DatagramPacket(data, data.length);
		// RECEIVE FROM PORT 23, and will wait until a packet is sent
		try {
			System.out.println("Waiting for A Packet");
			this.receiveSocketServer.receive(this.receivedServerPacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("[Intermediate Host] Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			this.closeSockets();
			System.exit(1);
		}

		Log.logReceiveMsg("Intermediate Host", this.receivedServerPacket);
	}


	/**
	 * Continuous method for receiving and sending
	 */
	public void sendAndReceive() {
		while (true) {
			System.out.println("------------------------");
			this.clientRequest();
			this.serverResponse();
		}
	}

	/**
	 * Initialize the host and launch the sendAndReceive method
	 * @param args String[], receives arguments if needed
	 */
	public static void main(String args[]) {
		IntermediateHost iH = new IntermediateHost();
		iH.sendAndReceive();
	}
}
