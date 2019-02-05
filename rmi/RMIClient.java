/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.NotBoundException;
//import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		int numMessages = Integer.parseInt(args[1]);

		// Initialise Security Manager
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

		// Bind to RMIServer
		try {
			Registry registry = LocateRegistry.getRegistry(args[0]);
			RMIServerI stub = (RMIServerI) registry.lookup("RMIServer");
			long startTime = System.nanoTime();

			for(short tries = 0; tries < numMessages; ++tries) {
				MessageInfo msg = new MessageInfo(numMessages, (int)tries);
				stub.receiveMessage(msg);
			}
			long endTime = System.nanoTime();
			System.err.println("Time elapsed (ms): " + ((endTime - startTime)/1000000));
		} catch (RemoteException e) {
			System.err.println("Error invoking remote procedure: " + e.toString()); 
			e.printStackTrace(); 
		} catch(NotBoundException e){
			System.err.println("Registry lookup failed:  " + e.toString()); 
			e.printStackTrace();
		}
	}
}
