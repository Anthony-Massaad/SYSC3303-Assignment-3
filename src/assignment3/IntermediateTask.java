package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * In charge of storing and sending messages from client -> server and vice versa
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class IntermediateTask implements Runnable{
	
	private DatagramSocket socket; 
	private DatagramPacket receivedPacket, storedPacket; 
	private final int port; 
	
	/**
	 * Constructor for the IntermediateTask
	 * @param port Integer, the port number that the class is associated with
	 */
	public IntermediateTask(int port) {
		this.port = port; 
		try {
			this.socket = new DatagramSocket(this.port);
			this.socket.setSoTimeout(Helper.TIMEOUT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.storedPacket = null;
	}
	
	private void closeSockets() {
		this.socket.close();
		System.exit(1);
	}
	
	/**
	 * Method for reusable sending packets
	 * @param packet DatagramPacket, the packet to send
	 */
	private void sendPacket(DatagramPacket packet) {
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.closeSockets(); 
		}
	}
	
	/**
	 * Overidden run method from Runnable
	 * Handles receive and send messages to server and client
	 */
	@Override
	public void run() {
		while (true) {
			byte data[] = new byte[Helper.SIZE];
			this.receivedPacket = new DatagramPacket(data, data.length);
			// receive data from either client or server
			// data can be a request, or a message to store
			try {
				this.socket.receive(this.receivedPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.closeSockets(); 
			}
			
			if (new String(this.receivedPacket.getData(), 0, this.receivedPacket.getLength()).equals(Helper.REQUESTING)){
				// request message impl
				// message to send or just acknowledge
				if (this.storedPacket == null) {
					// acknowledge that the request was sent but there is nothing stored currently 
                    System.out.println(Thread.currentThread().getName()+ " which is on port " + this.port + " request received from port " + this.receivedPacket.getPort() + ". No Message");
					byte nothingBytes[] = Helper.NOTHING.getBytes();
					try {
						this.sendPacket(new DatagramPacket(nothingBytes, nothingBytes.length, InetAddress.getLocalHost(), this.receivedPacket.getPort()));
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						this.closeSockets();
					}
					
				}else {
					// send and set to null
					// if the stored packet was from the server, switch the port to point to the client. otherwise, 
					// switch the port to point to the server (was sent from the client) 
                    System.out.println(Thread.currentThread().getName()+ " which is on port " + this.port + " request received from port " + this.receivedPacket.getPort() + ". Message to be sent");
					this.storedPacket.setPort(this.storedPacket.getPort() == Helper.PORT_SERVER ? Helper.PORT_CLIENT : Helper.PORT_SERVER);
					this.sendPacket(this.storedPacket);
					this.storedPacket = null; 
				}
				
			} else {
				// Storing Message passed impl
				// acknowledge and store
                System.out.println(Thread.currentThread().getName()+ " which is on port " + this.port + " message received from port " + this.receivedPacket.getPort() + ". Storing");
				try {
					this.sendPacket(new DatagramPacket(Helper.ACKNOWLEDGE, Helper.ACKNOWLEDGE.length, InetAddress.getLocalHost(), this.receivedPacket.getPort()));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.closeSockets();
				}
				this.storedPacket = this.receivedPacket;
			}
			
			
			
		}
	}
	
	
	
	
	
}
