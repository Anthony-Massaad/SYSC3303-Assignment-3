package assignment3;

/**
 * Intermediate Host is in charge of handling the sub tasks and handling the constant
 * polls from the client and server.
 * @author Anthony Massaad (ID: 101150282) SYSC 3303 Assignment 3
 */
public class IntermediateHost{
	
	/**
	 * Constuctor for Intermediate Host
	 * Creates 2 threads to send and receive messages between 
	 * <ul>
	 * <li>Server -> Client</li>
	 * <li>Client -> Server</li>
	 * </ul>
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
