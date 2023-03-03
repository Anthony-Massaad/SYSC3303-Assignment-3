package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Class RPC control. Common functions used between the Server and Client
 * @author Anthony Massaad (ID: 101150282) Assignment 3 SYSC3303
 *
 */
public class CommonRPCImpl {
	
	protected String systemName;
	protected DatagramSocket socket;
	protected DatagramPacket receivePacket; 
	
	/**
	 * Constrcutor for COmmonRPCImpl. 
	 * Initializes socket given the port
	 * @param systemName String, the system name 
	 * @param port Integer, the port number for the socket
	 */
	public CommonRPCImpl(String systemName, int port) {
		this.systemName = systemName; 
		
		try {
			this.socket = new DatagramSocket(port);
			this.socket.setSoTimeout(Helper.TIMEOUT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(this.systemName + " Started");
	}
	
	/**
	 * Closes all sockets and exits program
	 */
	protected void closeSockets() {
		this.socket.close();
		System.exit(1);
	}
	
	/**
	 * Send messages between ports give the message and destination port
	 * @param messageByte byte[], the message to be sent in bytes
	 * @param destinationPort Integer, the destination port to send the message
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void send(byte[] messageByte, int destinationPort) throws IOException, InterruptedException {
		DatagramPacket sendPacket; 
		byte ackdata[] = new byte[Helper.SIZE];
		
		// sending packet request
		sendPacket = new DatagramPacket(messageByte, messageByte.length, InetAddress.getLocalHost(), destinationPort);
		Log.logSendMsg(this.systemName, sendPacket);
		Thread.sleep(Helper.SLEEP);		// slow things down
		this.socket.send(sendPacket);
		System.out.println("Packet sent.");
		
		// receiving acknowledgement
		System.out.print("\nReceiving Acknowledgement of message sent\n");
		this.receivePacket = new DatagramPacket(ackdata, ackdata.length);
		socket.receive(this.receivePacket);
		Log.logReceiveMsg(this.systemName, this.receivePacket);
		
	}
	
	/**
	 * continously sends a request for data. If the data is NA (AKA nothing) given from the Intermediate Task, 
	 * it will loop back and request again after sleeping for 1000ms. Otherwise it will break out of the loop.
	 * @param byteSize Interger, the size of the buffer data for receiving data
	 * @param destinationPort Integer, the destination port to send a request to.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected void receive(int byteSize, int destinationPort) throws IOException, InterruptedException {
		DatagramPacket requestPacket;
		byte data[];
		while (true) {
			data = new byte[byteSize];
			this.receivePacket = new DatagramPacket(data, data.length);
			byte[] requestBytes = Helper.REQUESTING.getBytes();
			requestPacket = new DatagramPacket(requestBytes, requestBytes.length, InetAddress.getLocalHost(), destinationPort);
			
			this.socket.send(requestPacket);
			this.socket.receive(receivePacket);
			
			if (!new String(this.receivePacket.getData(), 0, this.receivePacket.getLength()).equals(Helper.NOTHING)) {
				break;
			}
			Thread.sleep(1000);
		}
		
		Log.logReceiveMsg(this.systemName, this.receivePacket);
		
	}
	
}
