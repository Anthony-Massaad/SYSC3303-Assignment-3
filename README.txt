SYSC3303A Assignment 3
--------------------------------------------------------------------
### Assignment Description

This application demonstrates communication using DatagramSockets and DatagramPackets between 3 different subsystems: Client, Server and Intermediate Host. 
It is to demonstrate a synchronous communication to establish a two-way channel from the Client and to the Server using remote procedure calls. This will be done
by transferring data from the Client to the SErver and vice versa via Intermediate Host.

--------------------------------------------------------------------
### Files Included

This Assignment consist of 7 java files

- Client.java: is in charge of generating either a read or write request in bytes to send to the Intermediate host, which will be picked up by the Server. It will also 
continuously request Data from the Intermediate Host. 

- Server.java: responsible for continuously request data from the Intermediate Host, and parsing the data to determine if it is 
a read or write request from the Client. For read, it will send a response with 0 3 0 1. For Write, it will 
send a response with 0 4 0 0.

- CommonRPCImpl.java: consist of commonality rpc calls for the Server and Client (i.e sockets, packets, send call, receive call). It will be the superclass for the Server and Client. 

- IntermediateHost.java: is the intermediate channel between the Server and Client. It will consist of two threads of Intermediate Task that will communication between Server and Client.

- IntermediateTask.java: a java class that implements Runnable, and is in charge of storing data and communication between the Server and Client. 

- Helper.java: The Helper class consist of constants that each subsystem uses, port numbers, as well as a method for printing a byte array into bytes. 

- Log.java: consist of two methods that will take in the subsystem name and the packet to use. It will log what is in the 
respective packet that is being sent or is being received. 

--------------------------------------------------------------------
### Instructions

1. Open Eclipse IDE and import the project by going to File -> Import -> Projects from Folder or Archive and select the submitted archive file. 

2. To run the program, each subsystem consist of a main method to launch itself. Simply Run in order:
- The Intermediate Host
- The Server
- The Client 

--------------------------------------------------------------------
### Answers to the questions

Question 1: Why did I suggest that you use more than one thread for the implementation of the Intermediate task?

It was suggested to use more than one thread for the implementation of the Intermediate task because it allows for separation between the two communication channel of the 
Client and Server. By creating two threads, we now have a channel for Client -> Server and a channel for Server -> Client. This decouples the program, and separates the channels to two 
different instances, which creates cleaner and a more scalable application. 

Question 2: Is it necessary to use synchronized in the intermediate task? Explain.

Yes, it is necessary to use synchronized in the intermediate task as it prevents both the Client and Server from accessing the resource at the same time. 
By allowing synchronization, we can now handle several threads at once, which in this case is the Client and Server, from requesting and sending to the shared 
message which is the intermediate task. 

--------------------------------------------------------------------
### Credits

Anthony Massaad (ID: 101150282)