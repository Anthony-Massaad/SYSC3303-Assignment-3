package assignment3;

/**
 * Intermediate Host class for receiving and sending to Client
 * As well as sending and receiving for Server
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 2
 */
public class IntermediateHost{
	/**
	 * Constuctor for Intermediate Host
	 * Initializes the receive socket to point at port 23 where the Client will send
	 * Initializes a send and receive socket for itself
	 */
	public IntermediateHost() {
		Thread m1T = new Thread(new IntermediateTask(Helper.PORT_INTERM1));
		Thread m2T = new Thread(new IntermediateTask(Helper.PORT_INTERM2));
		m1T.setName("Intermediate Host Task 1: Server -> Client");
		m2T.setName("Intermediate Host Task 2: Client -> Server");
		m1T.start();
		m2T.start();
		System.out.println("Intermediate Host Started");
	}
	
	/**
	 * Initialize the host and launch the sendAndReceive method
	 * @param args String[], receives arguments if needed
	 */
	public static void main(String args[]) {
		 new IntermediateHost();
	}



}
