/*
 * Created on 01-Mar-2016
 */
package rmi;

//import java.net.MalformedURLException;
//import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
//import java.util.Arrays;

//import com.sun.org.apache.xerces.internal.util.SecurityManager;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private boolean[] receivedMessages;
	int numRecieved = 0;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == -1){
			receivedMessages = new boolean[msg.totalMessages];
			totalMessages = msg.totalMessages;
			numRecieved = 0;

		}
		numRecieved++;

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = true;
		System.out.println(msg.messageNum);

		


		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(numRecieved == totalMessages){
			int droppedMsgs = 0;
			System.out.println("Messages lost:");
			for(int count = 0; count < receivedMessages.length; ++count) {
				if (!receivedMessages[count])
					System.out.println(count);
					droppedMsgs++;
			}
			System.out.println("Recieved " + numRecieved + " out of " + totalMessages + " : " + ((double)droppedMsgs/(double)totalMessages)*100 + "% lost.");
			totalMessages = -1;
		}

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}


		// TO-DO: Instantiate the server class

		try{
			String name = "testrun";
			rmis = new RMIServer();
			rebindServer(name, rmis);
			System.err.println("Server ready.");
		} catch (RemoteException e){
			System.err.println("Server could not initialise. Error: " + e.toString());
			e.printStackTrace();
		}


	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(serverURL, server);
		}catch(RemoteException e){
			System.out.println("Error binding server/creating registry: " + e.toString());
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
	}
}
